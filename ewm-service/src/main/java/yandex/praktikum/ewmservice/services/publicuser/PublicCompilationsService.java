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
import yandex.praktikum.ewmservice.services.privateuser.PrivateRequestsService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationsService {

    private final CompilationsRepository compilationsRepository;
    private final RequestsRepository requestsRepository;
    private final PrivateRequestsService privateRequestsService;

    public List<CompilationDto> get(Boolean pinned, Integer from, Integer size) {
        log.info("Получаем все подборки, начиная с #{}", from);
        return compilationsRepository.findAllByPinned(pinned, PageRequest.of(from, size))
                .stream()
                .map((e) -> {
                    List<Event> events = e.getEvents();
                    List<ParticipationRequest> allRequestsOfEvents = requestsRepository
                            .findParticipationRequestsByEventIn(events);
                    return CompilationMapper.compilationToDto(e, events.stream()
                            .map((f) -> EventsMapper.eventToShortDto(f, Math.toIntExact(allRequestsOfEvents
                                    .stream()
                                    .filter(p -> p.getEvent().equals(f))
                                    .count())))
                            .collect(Collectors.toList()));
                })
                .collect(Collectors.toList());
    }

    public CompilationDto getById(Long compId) {
        log.info("Получаем подборку {}", compId);
        Compilation compilation = compilationsRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        List<EventShortDto> eventShortDtos = compilation.getEvents().stream()
                .map((e) -> EventsMapper.eventToShortDto(e, privateRequestsService.countConfirmedRequests(e.getId())))
                .collect(Collectors.toList());
        return CompilationMapper.compilationToDto(compilation, eventShortDtos);
    }
}
