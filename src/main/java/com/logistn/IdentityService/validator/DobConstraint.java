package com.logistn.IdentityService.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraint {
    int min();

    String message() default "Age must be at lease >= {min}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
