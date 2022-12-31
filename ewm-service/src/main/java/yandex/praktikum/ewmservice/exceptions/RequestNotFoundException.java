package yandex.praktikum.ewmservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(Long requestId) {
        super("Запрос " + requestId + " не найден");
    }
}
