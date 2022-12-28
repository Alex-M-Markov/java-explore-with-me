package yandex.praktikum.ewmservice.services.publicuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yandex.praktikum.ewmservice.entities.Compilation;
import yandex.praktikum.ewmservice.entities.Event;
import yandex.praktikum.ewmservice.entities.ParticipationRequest;
import yandex.praktikum.ewmservice.entities.dto.compilations.CompilationDto;
import yandex.praktikum.ewmservice.entities.dto.event.EventShortDto;
import yandex.praktikum.ewmservice.entities.mappers.CompilationMapper;
import yandex.praktikum.ewmservice.entities.mappers.EventsMapper;
import yandex.praktikum.ewmservice.exceptions.CompilationNotFoundException;
import yandex.praktikum.ewmservice.repositories.CompilationsRepository;
import yandex.praktikum.ewmservice.repositories.RequestsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationsService {

    private final CompilationsRepository compilationsRepository;
    private final RequestsRepository requestsRepository;

    public List<CompilationDto> get(Boolean pinned, Integer from, Integer size) {
        log.info("Получаем все подборки, начиная с #{}", from);
        List<Compilation> compilations = compilationsRepository.findAllByPinned(pinned, PageRequest.of(from, size));
        List<Event> allEvents = new ArrayList<>();
        for (Compilation c : compilations) {
            allEvents.addAll(c.getEvents());
        }
        List<ParticipationRequest> allRequestsOfEvents = requestsRepository.findParticipationRequestsByEventIn(
                allEvents);
        Map<Event, Long> collect = allRequestsOfEvents.stream()
                .collect(Collectors.groupingBy(ParticipationRequest::getEvent, Collectors.counting()));
        return compilations.stream()
                .map(e -> CompilationMapper.compilationToDto(e, e.getEvents().stream()
                        .map(f -> EventsMapper.eventToShortDto(f, collect.get(f)))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public CompilationDto getById(Long compId) {
        log.info("Получаем подборку {}", compId);
        Compilation compilation = compilationsRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        List<ParticipationRequest> allRequestsOfEvents = requestsRepository.findParticipationRequestsByEventIn(
                compilation.getEvents());
        Map<Event, Long> collect = allRequestsOfEvents.stream()
                .collect(Collectors.groupingBy(ParticipationRequest::getEvent, Collectors.counting()));
        List<EventShortDto> eventShortDtos = compilation.getEvents().stream()
                .map(e -> EventsMapper.eventToShortDto(e, collect.get(e)))
                .collect(Collectors.toList());
        return CompilationMapper.compilationToDto(compilation, eventShortDtos);
    }
}
