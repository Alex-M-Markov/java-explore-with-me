package yandex.praktikum.ewmservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(long commentId) {
        super("Комментарий " + commentId + " не найден");
    }

}
