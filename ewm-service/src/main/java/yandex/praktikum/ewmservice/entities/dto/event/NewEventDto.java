package yandex.praktikum.ewmservice.entities.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yandex.praktikum.ewmservice.entities.Location;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @NotBlank
    @Size(max = 255)
    private String title;
    @NotBlank
    @Size(max = 1000)
    private String annotation;
    @NotBlank
    @Size(max = 10000)
    private String description;
    @NotNull
    private Long category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    @Future
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    @NotNull
    private Boolean paid;
    @PositiveOrZero
    private int participantLimit;
    @NotNull
    private Boolean requestModeration;
}
