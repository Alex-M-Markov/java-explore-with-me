package yandex.praktikum.ewmservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_EARLY)
public class TooEarlyEventException extends RuntimeException {
    public TooEarlyEventException() {
        super("Данное событие начинается слишком рано");
    }
}
