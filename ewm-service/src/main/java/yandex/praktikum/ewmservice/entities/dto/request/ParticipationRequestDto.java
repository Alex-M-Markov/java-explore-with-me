package yandex.praktikum.ewmservice.entities.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yandex.praktikum.ewmservice.entities.ParticipationStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private ParticipationStatus status = ParticipationStatus.PENDING;

}
