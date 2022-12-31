package yandex.praktikum.ewmservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WrongUserException extends RuntimeException {
    public WrongUserException() {
        super("Данное событие было опубликовано другим пользователем");
    }
}
