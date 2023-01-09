package yandex.praktikum.ewmservice.entities.mappers;


import lombok.experimental.UtilityClass;
import yandex.praktikum.ewmservice.entities.Comment;
import yandex.praktikum.ewmservice.entities.Event;
import yandex.praktikum.ewmservice.entities.User;
import yandex.praktikum.ewmservice.entities.dto.comment.CommentDto;
import yandex.praktikum.ewmservice.entities.dto.comment.NewCommentDto;

@UtilityClass
public class CommentsMapper {

    public static Comment fromNewCommentDto(NewCommentDto newCommentDto, User user, Event event) {
        return Comment.builder()
                .withText(newCommentDto.getText())
                .withUser(user)
                .withEvent(event)
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .withId(comment.getId())
                .withText(comment.getText())
                .withUser(comment.getUser().getId())
                .withEvent(comment.getEvent().getId())
                .withEdited(comment.isEdited())
                .build();
    }

}
