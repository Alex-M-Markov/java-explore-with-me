package yandex.praktikum.ewmservice.entities.dto.compilations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yandex.praktikum.ewmservice.entities.dto.event.EventShortDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;

}
