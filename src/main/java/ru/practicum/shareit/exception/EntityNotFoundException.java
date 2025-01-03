package ru.practicum.shareit.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> entityClass, String message) {
        super(entityClass.getSimpleName() + message);
    }

}
