package yandex.praktikum.ewmservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CompilationNotFoundException extends RuntimeException {
    public CompilationNotFoundException(Long userId) {
        super("Подборка " + userId + " не найдена");
    }
}
