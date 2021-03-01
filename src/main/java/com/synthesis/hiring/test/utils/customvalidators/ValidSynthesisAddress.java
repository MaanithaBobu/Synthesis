package com.synthesis.hiring.test.utils.customvalidators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
@Constraint(validatedBy = AddressValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSynthesisAddress {

  String message() default "Invalid Address Please refer manual.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
