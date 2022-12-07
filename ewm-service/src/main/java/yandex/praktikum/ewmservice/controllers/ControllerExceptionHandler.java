package yandex.praktikum.ewmservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yandex.praktikum.ewmservice.exceptions.ErrorResponse;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse stateIsNotValid(ConstraintViolationException ex) {
        log.info("Введённые данные нарушают установленные ограничения: {}", ex.getMessage());
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse stateIsNotValid(DataIntegrityViolationException ex) {
        log.info("Введённые данные нарушают ограничения базы данных: {}", ex.getMessage());
        return new ErrorResponse(ex.getMessage());
    }
}
