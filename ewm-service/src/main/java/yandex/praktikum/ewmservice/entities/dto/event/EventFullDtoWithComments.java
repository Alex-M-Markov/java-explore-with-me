package yandex.praktikum.ewmservice.entities.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import yandex.praktikum.ewmservice.entities.State;
import yandex.praktikum.ewmservice.entities.dto.category.CategoryDto;
import yandex.praktikum.ewmservice.entities.dto.comment.CommentDto;
import yandex.praktikum.ewmservice.entities.dto.location.LocationDto;
import yandex.praktikum.ewmservice.entities.dto.user.UserShortDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder (setterPrefix = "with")
public class EventFullDtoWithComments {

    private Long id;
    private String title;
    private String annotation;
    private String description;
    private CategoryDto category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    private int participantLimit;
    private Long confirmedRequests;
    private Boolean requestModeration;
    private UserShortDto initiator;
    private State state;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private Long views;
    private List<CommentDto> comments;
}
