package yandex.praktikum.ewmservice.services.privateuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yandex.praktikum.ewmservice.entities.*;
import yandex.praktikum.ewmservice.entities.dto.event.EventFullDto;
import yandex.praktikum.ewmservice.entities.dto.event.EventShortDto;
import yandex.praktikum.ewmservice.entities.dto.event.NewEventDto;
import yandex.praktikum.ewmservice.entities.dto.event.UpdateEventRequest;
import yandex.praktikum.ewmservice.entities.mappers.EventsMapper;
import yandex.praktikum.ewmservice.exceptions.*;
import yandex.praktikum.ewmservice.repositories.CategoriesRepository;
import yandex.praktikum.ewmservice.repositories.EventsRepository;
import yandex.praktikum.ewmservice.repositories.RequestsRepository;
import yandex.praktikum.ewmservice.repositories.UsersRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PrivateEventsService {

    private final EventsRepository eventsRepository;
    private final UsersRepository usersRepository;
    private final CategoriesRepository categoriesRepository;
    private final RequestsRepository requestsRepository;
    private final PrivateRequestsService privateRequestsService;
    public static final int NEW_EVENT_NOT_EARLIER_LIMIT = 2;
    public static final int NUMBER_OF_CONFIRMED_REQUESTS_IN_NEW_EVENT = 0;

    @Transactional
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        log.info("Создается новое событие {}", newEventDto.getTitle());
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(NEW_EVENT_NOT_EARLIER_LIMIT))) {
            throw new TooEarlyEventException();
        }

        if (newEventDto.getCategory() == null) {
            newEventDto.setCategory(0L);
        }
        Category category = categoriesRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(newEventDto.getCategory()));
        User user = usersRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        LocalDateTime createdOn = LocalDateTime.now();
        LocalDateTime publishedOn = LocalDateTime.now();
        EventFullDto eventFullDtoReturn = EventsMapper.eventToFullDto(
                eventsRepository.save(EventsMapper.fromNewEventDto(newEventDto, category, user, createdOn,
                        publishedOn)), NUMBER_OF_CONFIRMED_REQUESTS_IN_NEW_EVENT);
        log.info("Событие {} успешно создано", eventFullDtoReturn.getTitle());
        return eventFullDtoReturn;
    }

    @Transactional
    public EventFullDto update(Long userId, UpdateEventRequest updateEventRequest) {
        log.info("Обновляется событие {}", updateEventRequest.getTitle());
        Event eventFound = eventsRepository.findById(updateEventRequest.getEventId())
                .orElseThrow(() -> new EventNotFoundException(updateEventRequest.getCategory()));
        if (!eventFound.getInitiator().getId().equals(userId)) {
            throw new WrongUserException();
        }
        if (eventFound.getState().equals(State.PUBLISHED)) {
            throw new TooLateToChangeException();
        }
        if (updateEventRequest.getAnnotation() != null && !updateEventRequest.getAnnotation().isBlank()) {
            eventFound.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            eventFound.setCategory(categoriesRepository.findById(updateEventRequest.getCategory())
                    .orElseThrow(() -> new CategoryNotFoundException(updateEventRequest.getCategory())));
        }
        if (updateEventRequest.getDescription() != null && !updateEventRequest.getDescription().isBlank()) {
            eventFound.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getEventDate() != null) {
            if (updateEventRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(NEW_EVENT_NOT_EARLIER_LIMIT))) {
                throw new TooEarlyEventException();
            }
            eventFound.setEventDate(updateEventRequest.getEventDate());
        }
        if (updateEventRequest.getPaid() != null) {
            eventFound.setPaid(updateEventRequest.getPaid());
        }
        if (!Objects.equals(updateEventRequest.getParticipantLimit(), eventFound.getParticipantLimit())) {
            eventFound.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getTitle() != null && !updateEventRequest.getTitle().isBlank()) {
            eventFound.setTitle(updateEventRequest.getTitle());
        }
        log.info("Событие {} успешно обновлено", updateEventRequest.getTitle());
        return EventsMapper.eventToFullDto(eventFound, privateRequestsService.countConfirmedRequests(
                eventFound.getId()));
    }

    @Transactional
    public EventFullDto cancel(Long userId, Long eventId) {
        log.info("Отменяется событие {}", eventId);
        Event eventFound = eventsRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        if (!eventFound.getInitiator().getId().equals(userId)) {
            throw new WrongUserException();
        }
        eventFound.setState(State.CANCELED);
        eventsRepository.save(eventFound);
        return EventsMapper.eventToFullDto(eventFound, privateRequestsService.countConfirmedRequests(
                eventFound.getId()));
    }

    public List<EventShortDto> getAllOfUser(Long userId, Integer from, Integer size) {
        log.info("Получаем все события пользователя #{}", userId);
        List<Event> events = eventsRepository.findEventsByInitiator_Id(userId, PageRequest.of(from, size));
        List<ParticipationRequest> allRequestsOfEvents = requestsRepository.findParticipationRequestsByEventIn(events);
        return events.stream()
                .map((e) -> EventsMapper.eventToShortDto(e, Math.toIntExact(allRequestsOfEvents
                        .stream()
                        .filter(p -> p.getEvent().equals(e))
                        .count())))
                .collect(Collectors.toList());
    }

    public EventFullDto getById(Long userId, Long eventId) {
        log.info("Получаем событие #{} пользователя #{}", eventId, userId);
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new WrongUserException();
        }
        return EventsMapper.eventToFullDto(event, privateRequestsService.countConfirmedRequests(
                event.getId()));
    }

}
