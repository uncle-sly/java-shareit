package ru.practicum.shareit.booking.dto;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartBeforeEndValidator.class)
@Documented
public @interface StartBeforeEnd {

    String message() default "некорректные даты Start и End";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
