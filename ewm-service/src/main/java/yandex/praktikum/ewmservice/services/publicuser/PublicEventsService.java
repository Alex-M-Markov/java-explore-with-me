package yandex.praktikum.ewmservice.services.publicuser;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import yandex.praktikum.ewmservice.entities.mappers.EventsMapper;
import yandex.praktikum.ewmservice.exceptions.EventNotFoundException;
import yandex.praktikum.ewmservice.repositories.EventsRepository;
import yandex.praktikum.ewmservice.repositories.RequestsRepository;
import yandex.praktikum.ewmservice.services.privateuser.PrivateRequestsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
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
    @Value("${spring.application.name}")
    private String appName;

    public List<EventShortDto> get(String annotation, List<Long> categories, Boolean paid,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                   SortOptions sort, Integer from, Integer size,
                                   HttpServletRequest httpServletRequest) {
        statsWebClient.addHit(new EndpointHitDto(
                appName,
                httpServletRequest.getRequestURI(),
                httpServletRequest.getRemoteAddr(),
                LocalDateTime.now()
        ));
        List<Event> events = eventsRepository.getEventsPublic(annotation, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
        List<ParticipationRequest> allRequestsOfEvents = requestsRepository.findParticipationRequestsByEventIn(events);
        return events.stream()
                .map((e) -> EventsMapper.eventToShortDto(e, allRequestsOfEvents
                        .stream()
                        .filter(p -> p.getEvent().equals(e))
                        .count()))
                .collect(Collectors.toList());
    }

    public EventFullDto getByIdAndState(Long eventId, HttpServletRequest httpServletRequest) {
        log.info("Получаем событие #{}", eventId);
        statsWebClient.addHit(new EndpointHitDto(
                appName,
                httpServletRequest.getRequestURI(),
                httpServletRequest.getRemoteAddr(),
                LocalDateTime.now()
        ));
        Event event = eventsRepository.findByIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        return EventsMapper.eventToFullDto(event, privateRequestsService.countConfirmedRequests(eventId));
    }

}
