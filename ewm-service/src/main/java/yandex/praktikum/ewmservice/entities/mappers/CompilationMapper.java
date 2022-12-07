package yandex.praktikum.ewmservice.entities.mappers;


import lombok.experimental.UtilityClass;
import yandex.praktikum.ewmservice.entities.Compilation;
import yandex.praktikum.ewmservice.entities.Event;
import yandex.praktikum.ewmservice.entities.dto.compilations.CompilationDto;
import yandex.praktikum.ewmservice.entities.dto.compilations.NewCompilationDto;
import yandex.praktikum.ewmservice.entities.dto.event.EventShortDto;

import java.util.List;

@UtilityClass
public class CompilationMapper {

    public static Compilation fromNewCompilationDto(NewCompilationDto newCompilationDto, List<Event> events) {
        return Compilation.builder()
                .withEvents(events)
                .withPinned(newCompilationDto.isPinned())
                .withTitle(newCompilationDto.getTitle())
                .build();
    }

    public static CompilationDto compilationToDto(Compilation compilation, List<EventShortDto> eventShortDtos) {
        return new CompilationDto(
                compilation.getId(),
                eventShortDtos,
                compilation.getPinned(),
                compilation.getTitle()
        );
    }

}
