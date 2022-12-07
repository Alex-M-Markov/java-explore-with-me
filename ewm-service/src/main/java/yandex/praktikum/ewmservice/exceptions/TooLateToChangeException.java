package yandex.praktikum.ewmservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TooLateToChangeException extends RuntimeException {
    public TooLateToChangeException() {
        super("Данное событие уже нельзя изменить");
    }
}
