package yandex.praktikum.ewmservice.services.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yandex.praktikum.ewmservice.entities.Compilation;
import yandex.praktikum.ewmservice.entities.Event;
import yandex.praktikum.ewmservice.entities.ParticipationRequest;
import yandex.praktikum.ewmservice.entities.dto.compilations.CompilationDto;
import yandex.praktikum.ewmservice.entities.dto.compilations.NewCompilationDto;
import yandex.praktikum.ewmservice.entities.dto.event.EventShortDto;
import yandex.praktikum.ewmservice.entities.mappers.CompilationMapper;
import yandex.praktikum.ewmservice.entities.mappers.EventsMapper;
import yandex.praktikum.ewmservice.exceptions.CompilationNotFoundException;
import yandex.praktikum.ewmservice.exceptions.EventNotFoundException;
import yandex.praktikum.ewmservice.repositories.CompilationsRepository;
import yandex.praktikum.ewmservice.repositories.EventsRepository;
import yandex.praktikum.ewmservice.repositories.RequestsRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationsService {
    private final CompilationsRepository compilationsRepository;
    private final EventsRepository eventsRepository;
    private final RequestsRepository requestsRepository;

    @Transactional
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        log.info("Добавляем новую подборку");
        List<Event> eventsList = eventsRepository.findEventsByIdIn(newCompilationDto.getEvents());
        Compilation compilation = compilationsRepository.save(CompilationMapper.fromNewCompilationDto(newCompilationDto,
                eventsList));
        log.info("Новая подборка {} добавлена", compilation.getId());
        List<ParticipationRequest> allRequestsOfEvents = requestsRepository.findParticipationRequestsByEventIn(
                eventsList);
        Map<Event, Long> collect = allRequestsOfEvents.stream()
                .collect(Collectors.groupingBy(ParticipationRequest::getEvent, Collectors.counting()));
        List<EventShortDto> eventShortDtos = compilation.getEvents().stream()
                .map(e -> EventsMapper.eventToShortDto(e, collect.get(e)))
                .collect(Collectors.toList());
        return CompilationMapper.compilationToDto(compilation, eventShortDtos);
    }

    @Transactional
    public void delete(Long compId) {
        log.info("Удаляем подборку {}", compId);
        Compilation compilation = compilationsRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        compilationsRepository.delete(compilation);
        log.info("Подборка {} успешно удалена", compilation.getId());
    }

    @Transactional
    public void deleteEvent(Long compId, Long eventId) {
        log.info("Удаляем событие {} из подборки {}", eventId, compId);
        Compilation compilation = compilationsRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        compilation.getEvents().remove(event);
        compilationsRepository.save(compilation);
        log.info("Событие {} успешно удалено из подборки {}", eventId, compId);
    }

    @Transactional
    public void addEvent(Long compId, Long eventId) {
        log.info("Добавляем событие {} в подборку {}", eventId, compId);
        Compilation compilation = compilationsRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        compilation.getEvents().add(event);
        compilationsRepository.save(compilation);
        log.info("Событие {} успешно добавлено в подборку {}", eventId, compId);
    }

    @Transactional
    public void deletePin(Long compId) {
        log.info("Открепляем подборку {} на главной странице", compId);
        Compilation compilation = compilationsRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        compilation.setPinned(false);
        compilationsRepository.save(compilation);
        log.info("Подборка {} успешно откреплена", compId);
    }

    @Transactional
    public void addPin(Long compId) {
        log.info("Закрепляем подборку {} на главной странице", compId);
        Compilation compilation = compilationsRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        compilation.setPinned(true);
        compilationsRepository.save(compilation);
        log.info("Подборка {} успешно закреплена", compId);
    }
}
