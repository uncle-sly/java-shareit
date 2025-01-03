package ru.practicum.shareit.error;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.EntityUpdateException;
import ru.practicum.shareit.item.exception.ValidationException;
import ru.practicum.shareit.user.exception.UserEmailExistedException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(
            ConstraintViolationException e
    ) {
        final List<ValidationViolation> validationViolations = e.getConstraintViolations().stream()
                .map(
                        violation -> {
                            log.error("ConstraintViolationException: {} : {}", violation.getPropertyPath().toString(), violation.getMessage());
                            return new ValidationViolation(
                                    violation.getPropertyPath().toString(),
                                    violation.getMessage()
                            );
                        }
                )
                .toList();

        return new ValidationErrorResponse(validationViolations);
    }

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

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse onEntityNotFoundException(final EntityNotFoundException e) {
        log.error("EntityNotFoundException: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({UserEmailExistedException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse onUserEmailExistedException(final UserEmailExistedException e) {
        log.error("UserEmailExistedException: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({EntityUpdateException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse onEntityUpdateException(final EntityUpdateException e) {
        log.error("EntityUpdateException: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse onValidationException(final ValidationException e) {
        log.error("ValidationException: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAnyException(final Throwable e) {
        log.error("Error: ", e);
        return new ErrorResponse(e.getMessage());
    }
}

