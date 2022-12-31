package yandex.praktikum.ewmservice.controllers.privateuser;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import yandex.praktikum.ewmservice.entities.dto.request.ParticipationRequestDto;
import yandex.praktikum.ewmservice.services.privateuser.PrivateRequestsService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class PrivateRequestsController {
    private final PrivateRequestsService privateRequestsService;

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto post(
            @PathVariable Long userId,
            @RequestParam(value = "eventId") Long eventId) {
        return privateRequestsService.post(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getAll(@PathVariable long userId) {
        return privateRequestsService.getAll(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancel(
            @PathVariable long userId,
            @PathVariable long requestId) {
        return privateRequestsService.cancel(userId, requestId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForEvent(
            @PathVariable long userId,
            @PathVariable long eventId) {
        return privateRequestsService.getForUserEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequestForEvent(
            @PathVariable long userId,
            @PathVariable long eventId,
            @PathVariable long reqId) {
        return privateRequestsService.confirmForUserEvent(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequestForEvent(
            @PathVariable long userId,
            @PathVariable long eventId,
            @PathVariable long reqId) {
        return privateRequestsService.rejectForUserEvent(userId, eventId, reqId);
    }

}
