package yandex.praktikum.ewmservice.controllers.privateuser;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yandex.praktikum.ewmservice.entities.dto.event.*;
import yandex.praktikum.ewmservice.services.privateuser.PrivateEventsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class PrivateEventsController {
    private final PrivateEventsService privateEventsService;

    @PostMapping("/{userId}/events")
    public EventFullDto create(@PathVariable long userId, @RequestBody @Valid NewEventDto newEventDto) {
        return privateEventsService.create(userId, newEventDto);
    }

    @PatchMapping("/{userId}/events")
    public EventFullDto update(@PathVariable long userId, @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        return privateEventsService.update(userId, updateEventRequest);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancel(@PathVariable Long userId, @PathVariable long eventId) {
        return privateEventsService.cancel(userId, eventId);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getAll(
            @PathVariable long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size,
            HttpServletRequest httpServletRequest) {
        return privateEventsService.getAllOfUser(userId, from, size, httpServletRequest);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDtoWithComments getById(
            @PathVariable long userId,
            @PathVariable long eventId,
            HttpServletRequest httpServletRequest) {
        return privateEventsService.getById(userId, eventId, httpServletRequest);
    }

}