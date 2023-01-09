package yandex.praktikum.ewmservice.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import yandex.praktikum.ewmservice.entities.dto.comment.CommentDto;
import yandex.praktikum.ewmservice.services.admin.AdminCommentsService;

import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class AdminCommentsController {
    private final AdminCommentsService adminCommentsService;

    @GetMapping("/{userId}")
    public List<CommentDto> getAllOfUser(@PathVariable long userId) {
        return adminCommentsService.getAllOfUser(userId);
    }

    @GetMapping
    public List<CommentDto> findByText(@RequestParam(name = "text") String text) {
        return adminCommentsService.findByText(text);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable long commentId) {
        adminCommentsService.delete(commentId);
    }


    @DeleteMapping("/{userId}")
    public void deleteAllOfUser(@PathVariable long userId) {
        adminCommentsService.deleteAllOfUser(userId);
    }

}