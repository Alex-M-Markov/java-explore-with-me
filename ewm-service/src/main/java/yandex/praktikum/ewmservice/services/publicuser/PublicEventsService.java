package yandex.praktikum.ewmservice.services.publicuser;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yandex.praktikum.ewmservice.client.StatsWebClient;
import yandex.praktikum.ewmservice.entities.Event;
import yandex.praktikum.ewmservice.entities.ParticipationRequest;
import yandex.praktikum.ewmservice.entities.SortOptions;
import yandex.praktikum.ewmservice.entities.State;
import yandex.praktikum.ewmservice.entities.dto.event.EventFullDto;
import yandex.praktikum.ewmservice.entities.dto.event.EventShortDto;
import yandex.praktikum.ewmservice.entities.dto.statistics.EndpointHitDto;
import yandex.praktikum.ewmservice.entities.dto.statistics.StatsRequestDto;
import yandex.praktikum.ewmservice.entities.dto.statistics.ViewStatsDto;
import yandex.praktikum.ewmservice.entities.mappers.EventsMapper;
import yandex.praktikum.ewmservice.exceptions.EventNotFoundException;
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
public class PublicEventsService {

    private final EventsRepository eventsRepository;
    private final RequestsRepository requestsRepository;
    private final StatsWebClient statsWebClient;
    private final PrivateRequestsService privateRequestsService;

    public List<EventShortDto> get(String annotation, List<Long> categories, Boolean paid,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                   SortOptions sort, Integer from, Integer size,
                                   HttpServletRequest httpServletRequest) {
        statsWebClient.addHit(new EndpointHitDto(
                null,
                httpServletRequest.getRequestURI(),
                httpServletRequest.getRemoteAddr(),
                LocalDateTime.now()
        ));
        List<Event> events = eventsRepository.getEventsPublic(annotation, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
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

    public EventFullDto getByIdAndState(Long eventId, HttpServletRequest httpServletRequest) {
        log.info("Получаем событие #{}", eventId);
        statsWebClient.addHit(new EndpointHitDto(
                null,
                httpServletRequest.getRequestURI(),
                httpServletRequest.getRemoteAddr(),
                LocalDateTime.now()
        ));
        Event event = eventsRepository.findByIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        EventFullDto eventFullDto = EventsMapper.eventToFullDto(event, privateRequestsService.countConfirmedRequests(
                eventId));
        ArrayList<String> uris = new ArrayList<>(List.of("/events/" + eventId));
        StatsRequestDto statsRequestDto = StatsRequestDto.builder()
                .withStart(eventFullDto.getPublishedOn())
                .withEnd(LocalDateTime.now())
                .withUris(uris)
                .withUnique(false)
                .build();
        eventFullDto.setViews((long) statsWebClient.getViews(statsRequestDto).size());
        return eventFullDto;
    }

}
