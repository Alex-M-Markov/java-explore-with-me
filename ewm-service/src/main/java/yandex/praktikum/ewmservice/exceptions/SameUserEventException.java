package yandex.praktikum.ewmservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SameUserEventException extends RuntimeException {
    public SameUserEventException() {
        super("Это Ваше событие, вы и так в нём участвуете!");
    }
}
