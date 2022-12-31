package yandex.praktikum.ewmservice.entities.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequest {

    @Size(max = 1000)
    private String annotation;
    private Long category;
    @Size(max = 10000)
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private LocalDateTime eventDate;
    @PositiveOrZero
    private Integer participantLimit;
    @NotNull
    private Long eventId;
    private Boolean paid;
    @Size(max = 255)
    private String title;


}
