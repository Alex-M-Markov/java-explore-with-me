package yandex.praktikum.ewmservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventIsFullException extends RuntimeException {
    public EventIsFullException() {
        super("Места на данном мероприятии закончились");
    }
}
