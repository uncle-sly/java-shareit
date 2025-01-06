package ru.practicum.shareit.user.exception;

public class UserEmailExistedException extends RuntimeException {
    public UserEmailExistedException(String message) {
        super(message);
    }
}
