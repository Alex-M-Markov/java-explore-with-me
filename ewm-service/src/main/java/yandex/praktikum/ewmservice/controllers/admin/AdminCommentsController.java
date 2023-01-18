package yandex.praktikum.ewmservice.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yandex.praktikum.ewmservice.entities.dto.comment.CommentDto;
import yandex.praktikum.ewmservice.services.admin.AdminCommentsService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@Validated
public class AdminCommentsController {
    private final AdminCommentsService adminCommentsService;

    @GetMapping("/{userId}")
    public List<CommentDto> getAllOfUser(@PathVariable Long userId,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return adminCommentsService.getAllOfUser(userId, from, size);
    }

    @GetMapping
    public List<CommentDto> findByText(@RequestParam(name = "text") String text,
                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                       @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return adminCommentsService.findByText(text, from, size);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long commentId) {
        adminCommentsService.delete(commentId);
    }


    @DeleteMapping("/{userId}")
    public void deleteAllOfUser(@PathVariable Long userId) {
        adminCommentsService.deleteAllOfUser(userId);
    }

}