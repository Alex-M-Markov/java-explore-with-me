package yandex.praktikum.ewmservice.controllers.privateuser;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import yandex.praktikum.ewmservice.entities.dto.comment.CommentDto;
import yandex.praktikum.ewmservice.entities.dto.comment.NewCommentDto;
import yandex.praktikum.ewmservice.services.privateuser.PrivateCommentsService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class PrivateCommentsController {
    private final PrivateCommentsService privateCommentsService;

    @PostMapping("/{userId}/{eventId}/comments")
    public CommentDto create(@PathVariable long userId, @PathVariable long eventId,
                             @RequestBody @Valid NewCommentDto newCommentDto) {
        return privateCommentsService.create(userId, eventId, newCommentDto);
    }

    @PatchMapping("/{userId}/{eventId}/comments")
    public CommentDto update(@PathVariable long userId, @PathVariable long eventId,
                             @RequestBody @Valid CommentDto commentDto) {
        return privateCommentsService.update(userId, eventId, commentDto);
    }

    @DeleteMapping("/{userId}/{eventId}/{commentId}")
    public void delete(@PathVariable long userId, @PathVariable long eventId, @PathVariable long commentId) {
        privateCommentsService.delete(userId, eventId, commentId);
    }

}