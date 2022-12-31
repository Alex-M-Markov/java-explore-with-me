package yandex.praktikum.ewmservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long eventId) {
        super("Событие " + eventId + " не найдено");
    }
}
