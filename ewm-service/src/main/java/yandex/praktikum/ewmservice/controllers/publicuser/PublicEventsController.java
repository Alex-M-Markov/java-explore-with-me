package yandex.praktikum.ewmservice.controllers.publicuser;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yandex.praktikum.ewmservice.entities.SortOptions;
import yandex.praktikum.ewmservice.entities.dto.event.EventFullDtoWithComments;
import yandex.praktikum.ewmservice.entities.dto.event.EventShortDto;
import yandex.praktikum.ewmservice.services.publicuser.PublicEventsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class PublicEventsController {
    private final PublicEventsService publicEventsService;

    @GetMapping
    public List<EventShortDto> get(
            @RequestParam(name = "text", required = false) String annotation,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime rangeEnd,
            @RequestParam(name = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(name = "sort", required = false) SortOptions sort,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size,
            HttpServletRequest httpServletRequest) {
        return publicEventsService.get(annotation, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size, httpServletRequest);
    }

    @GetMapping("/{id}")
    public EventFullDtoWithComments getByIdAndState(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        return publicEventsService.getByIdAndState(id, httpServletRequest);
    }

}
