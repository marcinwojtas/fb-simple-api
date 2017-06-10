package pl.wojtas.fb.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.wojtas.fb.exception.InternalException;
import pl.wojtas.fb.exception.NotFoundException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public void handleNotFound() {
    }

    @ExceptionHandler(value = InternalException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public void handleInternal() {
    }
}
