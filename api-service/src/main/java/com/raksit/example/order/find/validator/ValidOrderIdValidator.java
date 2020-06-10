package com.raksit.example.order.find.validator;

import com.raksit.example.order.common.validator.BaseValidator;

import java.util.UUID;

public class ValidOrderIdValidator extends BaseValidator<ValidOrderId, String> {

  @Override
  public boolean validate(String value) {
    try {
      UUID.fromString(value);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  @Override
  public void initialize(ValidOrderId constraintAnnotation) {
    this.setMessage(constraintAnnotation.message());
  }
}
