package yandex.praktikum.ewmservice.entities.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yandex.praktikum.ewmservice.entities.Location;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateEventRequest {

    @Size(max = 1000)
    private String annotation;
    private Long category;
    @Size(max = 10000)
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    @PositiveOrZero
    private int participantLimit;
    private Boolean requestModeration;
    @Size(max = 255)
    private String title;

}
