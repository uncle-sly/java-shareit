package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandlingControllerAdvice {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        final List<ValidationViolation> validationViolations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> {
                            log.error("MethodArgumentNotValidException: {} : {}", error.getField(), error.getDefaultMessage());
                            return new ValidationViolation(error.getField(), error.getDefaultMessage());
                        }
                )
                .toList();

        return new ValidationErrorResponse(validationViolations);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onMissingRequestHeaderException(final MissingRequestHeaderException e) {
        log.error("MissingRequestHeaderException: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAnyException(final Throwable e) {
        log.error("Error: ", e);
        return new ErrorResponse(e.getMessage());
    }
}

