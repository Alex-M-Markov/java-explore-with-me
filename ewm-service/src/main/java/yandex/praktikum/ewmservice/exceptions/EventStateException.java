package yandex.praktikum.ewmservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventStateException extends RuntimeException {
    public EventStateException() {
        super("Данное событие еще не опубликовано");
    }

    public EventStateException(String message) {
        super(message);
    }
}
