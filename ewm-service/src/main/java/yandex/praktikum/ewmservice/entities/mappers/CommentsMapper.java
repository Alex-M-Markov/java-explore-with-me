package yandex.praktikum.ewmservice.entities.mappers;


import lombok.experimental.UtilityClass;
import yandex.praktikum.ewmservice.entities.Comment;
import yandex.praktikum.ewmservice.entities.Event;
import yandex.praktikum.ewmservice.entities.User;
import yandex.praktikum.ewmservice.entities.dto.comment.CommentDto;

@UtilityClass
public class CommentsMapper {

    public static Comment fromCommentDto(CommentDto commentDto, User user, Event event) {
        return Comment.builder()
                .withText(commentDto.getText())
                .withUser(user)
                .withEvent(event)
                .withEdited(commentDto.isEdited())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .withText(comment.getText())
                .withUser(comment.getUser().getId())
                .withEvent(comment.getEvent().getId())
                .withEdited(comment.isEdited())
                .build();
    }

}
