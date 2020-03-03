package com.raksit.example.order.validator;

import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.Setter;

@Setter
public abstract class BaseValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {

  private String message;

  @Override
  public boolean isValid(T value, ConstraintValidatorContext context) {
    if (validate(value)) {
      return true;
    } else {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message)
          .addConstraintViolation();
      return false;
    }
  }

  abstract boolean validate(T value);
}
