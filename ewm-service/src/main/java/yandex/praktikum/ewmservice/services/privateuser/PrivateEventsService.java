package yandex.praktikum.ewmservice.services.privateuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yandex.praktikum.ewmservice.client.StatsWebClient;
import yandex.praktikum.ewmservice.entities.Category;
import yandex.praktikum.ewmservice.entities.Comment;
import yandex.praktikum.ewmservice.entities.Event;
import yandex.praktikum.ewmservice.entities.ParticipationRequest;
import yandex.praktikum.ewmservice.entities.State;
import yandex.praktikum.ewmservice.entities.User;
import yandex.praktikum.ewmservice.entities.dto.event.EventFullDto;
import yandex.praktikum.ewmservice.entities.dto.event.EventFullDtoWithComments;
import yandex.praktikum.ewmservice.entities.dto.event.EventShortDto;
import yandex.praktikum.ewmservice.entities.dto.event.NewEventDto;
import yandex.praktikum.ewmservice.entities.dto.event.UpdateEventRequest;
import yandex.praktikum.ewmservice.entities.dto.statistics.EndpointHitDto;
import yandex.praktikum.ewmservice.entities.dto.statistics.StatsRequestDto;
import yandex.praktikum.ewmservice.entities.dto.statistics.ViewStatsDto;
import yandex.praktikum.ewmservice.entities.mappers.CommentsMapper;
import yandex.praktikum.ewmservice.entities.mappers.EventsMapper;
import yandex.praktikum.ewmservice.exceptions.CategoryNotFoundException;
import yandex.praktikum.ewmservice.exceptions.EventNotFoundException;
import yandex.praktikum.ewmservice.exceptions.TooEarlyEventException;
import yandex.praktikum.ewmservice.exceptions.TooLateToChangeException;
import yandex.praktikum.ewmservice.exceptions.UserNotFoundException;
import yandex.praktikum.ewmservice.exceptions.WrongUserException;
import yandex.praktikum.ewmservice.repositories.CategoriesRepository;
import yandex.praktikum.ewmservice.repositories.CommentsRepository;
import yandex.praktikum.ewmservice.repositories.EventsRepository;
import yandex.praktikum.ewmservice.repositories.RequestsRepository;
import yandex.praktikum.ewmservice.repositories.UsersRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final CommentsRepository commentsRepository;
    private final StatsWebClient statsWebClient;
    private final PrivateRequestsService privateRequestsService;
    public static final long NEW_EVENT_NOT_EARLIER_LIMIT = 2L;
    public static final long NUMBER_OF_CONFIRMED_REQUESTS_IN_NEW_EVENT = 0L;

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
        return EventsMapper.eventToFullDto(eventFound, privateRequestsService.countConfirmedRequests(
                eventFound.getId()));
    }

    public List<EventShortDto> getAllOfUser(Long userId, Integer from, Integer size,
                                            HttpServletRequest httpServletRequest) {
        log.info("Получаем все события пользователя #{}", userId);
        statsWebClient.addHit(new EndpointHitDto(
                null,
                httpServletRequest.getRequestURI(),
                httpServletRequest.getRemoteAddr(),
                LocalDateTime.now()
        ));
        List<Event> events = eventsRepository.findEventsByInitiator_Id(userId, PageRequest.of(from, size));
        Map<String, Event> eventsWithUris = new HashMap<>();
        for (Event event : events) {
            eventsWithUris.put("/events/" + event.getId(), event);
        }
        StatsRequestDto statsRequestDto = StatsRequestDto.builder()
                .withStart(LocalDateTime.MIN)
                .withEnd(LocalDateTime.now())
                .withUris(new ArrayList<>(eventsWithUris.keySet()))
                .withUnique(false)
                .build();
        List<ViewStatsDto> stats = statsWebClient.getViews(statsRequestDto);
        Map<Long, Long> eventsViewsStats = new HashMap<>();
        for (ViewStatsDto viewStatsDto : stats) {
            if (viewStatsDto.getUri() != null) {
                eventsViewsStats.put(eventsWithUris.get(viewStatsDto.getUri()).getId(), viewStatsDto.getHits());
            }
        }
        List<ParticipationRequest> allRequestsOfEvents = requestsRepository.findParticipationRequestsByEventIn(events);
        Map<Event, Long> collect = allRequestsOfEvents.stream()
                .collect(Collectors.groupingBy(ParticipationRequest::getEvent, Collectors.counting()));
        List<EventShortDto> eventShortDtos = events.stream()
                .map(e -> EventsMapper.eventToShortDto(e, collect.get(e)))
                .collect(Collectors.toList());
        if (eventsViewsStats.size() > 0) {
            for (EventShortDto eventShortDto : eventShortDtos) {
                eventShortDto.setViews(eventsViewsStats.get(eventShortDto.getId()));
            }
        }
        return eventShortDtos;
    }

    public EventFullDtoWithComments getById(Long userId, Long eventId, HttpServletRequest httpServletRequest) {
        log.info("Получаем событие #{} пользователя #{}", eventId, userId);
        statsWebClient.addHit(new EndpointHitDto(
                null,
                httpServletRequest.getRequestURI(),
                httpServletRequest.getRemoteAddr(),
                LocalDateTime.now()
        ));
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new WrongUserException();
        }
        EventFullDtoWithComments eventFullDtoWithComments = EventsMapper.eventToFullDtoWithComments(event, privateRequestsService.countConfirmedRequests(
                event.getId()));
        ArrayList<String> uris = new ArrayList<>(List.of("/events/" + eventId));
        StatsRequestDto statsRequestDto = StatsRequestDto.builder()
                .withStart(eventFullDtoWithComments.getPublishedOn())
                .withEnd(LocalDateTime.now())
                .withUris(uris)
                .withUnique(false)
                .build();
        eventFullDtoWithComments.setViews((long) statsWebClient.getViews(statsRequestDto).size());
        List<Comment> comments = commentsRepository.findAllByEvent(event);
        eventFullDtoWithComments.setComments(comments.stream()
                .map(CommentsMapper::toCommentDto)
                .collect(Collectors.toList()));
        return eventFullDtoWithComments;
    }

}
