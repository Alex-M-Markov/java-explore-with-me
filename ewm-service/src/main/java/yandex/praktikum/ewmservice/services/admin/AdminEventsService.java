package yandex.praktikum.ewmservice.services.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yandex.praktikum.ewmservice.client.StatsWebClient;
import yandex.praktikum.ewmservice.entities.Event;
import yandex.praktikum.ewmservice.entities.ParticipationRequest;
import yandex.praktikum.ewmservice.entities.State;
import yandex.praktikum.ewmservice.entities.dto.event.AdminUpdateEventRequest;
import yandex.praktikum.ewmservice.entities.dto.event.EventFullDto;
import yandex.praktikum.ewmservice.entities.dto.statistics.EndpointHitDto;
import yandex.praktikum.ewmservice.entities.dto.statistics.StatsRequestDto;
import yandex.praktikum.ewmservice.entities.dto.statistics.ViewStatsDto;
import yandex.praktikum.ewmservice.entities.mappers.EventsMapper;
import yandex.praktikum.ewmservice.exceptions.*;
import yandex.praktikum.ewmservice.repositories.CategoriesRepository;
import yandex.praktikum.ewmservice.repositories.EventsRepository;
import yandex.praktikum.ewmservice.repositories.RequestsRepository;
import yandex.praktikum.ewmservice.services.privateuser.PrivateRequestsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AdminEventsService {

    private final EventsRepository eventsRepository;
    private final CategoriesRepository categoriesRepository;
    private final RequestsRepository requestsRepository;
    private final StatsWebClient statsWebClient;
    private final PrivateRequestsService privateRequestsService;

    @Transactional
    public EventFullDto update(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("Обновляется событие {}", eventId);
        Event eventFound = eventsRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        if (adminUpdateEventRequest.getAnnotation() != null && !adminUpdateEventRequest.getAnnotation().isBlank()) {
            eventFound.setAnnotation(adminUpdateEventRequest.getAnnotation());
        }
        if (adminUpdateEventRequest.getCategory() != null) {
            eventFound.setCategory(categoriesRepository.findById(adminUpdateEventRequest.getCategory())
                    .orElseThrow(() -> new CategoryNotFoundException(adminUpdateEventRequest.getCategory())));
        }
        if (adminUpdateEventRequest.getDescription() != null && !adminUpdateEventRequest.getDescription().isBlank()) {
            eventFound.setDescription(adminUpdateEventRequest.getDescription());
        }
        if (adminUpdateEventRequest.getEventDate() != null) {
            eventFound.setEventDate(adminUpdateEventRequest.getEventDate());
        }
        if (adminUpdateEventRequest.getLocation() != null) {
            eventFound.setLocation(adminUpdateEventRequest.getLocation());
        }

        if (adminUpdateEventRequest.getPaid() != null) {
            eventFound.setPaid(adminUpdateEventRequest.getPaid());
        }
        if (adminUpdateEventRequest.getParticipantLimit() != eventFound.getParticipantLimit()) {
            eventFound.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        }
        if (adminUpdateEventRequest.getRequestModeration() != null) {
            eventFound.setRequestModeration(adminUpdateEventRequest.getRequestModeration());
        }
        if (adminUpdateEventRequest.getTitle() != null && !adminUpdateEventRequest.getTitle().isBlank()) {
            eventFound.setTitle(adminUpdateEventRequest.getTitle());
        }
        log.info("Событие {} успешно обновлено", eventId);
        return EventsMapper.eventToFullDto(eventFound, privateRequestsService.countConfirmedRequests(
                eventFound.getId()));
    }

    public List<EventFullDto> find(List<Long> users, List<State> states, List<Long> categories,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                   Integer from, Integer size, HttpServletRequest httpServletRequest) {
        log.info("Выгружаем события по запросу");
        statsWebClient.addHit(new EndpointHitDto(
                null,
                httpServletRequest.getRequestURI(),
                httpServletRequest.getRemoteAddr(),
                LocalDateTime.now()
        ));
        List<Event> events = eventsRepository.getEventsAdmin(users, states, categories, rangeStart, rangeEnd, from,
                size);
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
        List<EventFullDto> eventFullDtos = events.stream()
                .map((e) -> EventsMapper.eventToFullDto(e, collect.get(e)))
                .collect(Collectors.toList());
        if (eventsViewsStats.size() > 0) {
            for (EventFullDto eventFullDto : eventFullDtos) {
                eventFullDto.setViews(eventsViewsStats.get(eventFullDto.getId()));
            }
        }
        return eventFullDtos;
    }

    @Transactional
    public EventFullDto publish(Long eventId) {
        log.info("Публикуем событие {}", eventId);
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (!event.getState().equals(State.PENDING)) {
            throw new EventStateException("Событие не ожидает публикации");
        }
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1L))) {
            throw new TooEarlyEventException();
        }
        event.setState(State.PUBLISHED);
        eventsRepository.save(event);
        log.info("Событие {} успешно опубликовано", eventId);
        return EventsMapper.eventToFullDto(event, privateRequestsService.countConfirmedRequests(event.getId()));
    }

    @Transactional
    public EventFullDto reject(Long eventId) {
        log.info("Отклоняем событие {}", eventId);
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getState().equals(State.PUBLISHED)) {
            throw new TooLateToChangeException();
        }
        event.setState(State.CANCELED);
        eventsRepository.save(event);
        log.info("Событие {} успешно отклонено", eventId);
        return EventsMapper.eventToFullDto(event, privateRequestsService.countConfirmedRequests(event.getId()));
    }
}
