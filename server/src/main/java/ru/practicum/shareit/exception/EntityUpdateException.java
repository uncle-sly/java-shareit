package ru.practicum.shareit.exception;

public class EntityUpdateException extends RuntimeException {

    public EntityUpdateException(Class<?> entityClass, String message) {
        super(entityClass.getSimpleName() + message);
    }

}
