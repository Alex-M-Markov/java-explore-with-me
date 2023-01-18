package yandex.praktikum.ewmservice.controllers.privateuser;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public CommentDto create(@PathVariable Long userId, @PathVariable Long eventId,
                             @RequestBody @Valid NewCommentDto newCommentDto) {
        return privateCommentsService.create(userId, eventId, newCommentDto);
    }

    @PatchMapping("/{userId}/{eventId}/comments")
    public CommentDto update(@PathVariable Long userId, @PathVariable Long eventId,
                             @RequestBody @Valid CommentDto commentDto) {
        return privateCommentsService.update(userId, eventId, commentDto);
    }

    @DeleteMapping("/{userId}/{eventId}/{commentId}")
    public void delete(@PathVariable Long userId, @PathVariable Long eventId, @PathVariable Long commentId) {
        privateCommentsService.delete(userId, eventId, commentId);
    }

}