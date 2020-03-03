package com.raksit.example.order.validator;

import java.util.Currency;

public class ValidCurrencyValidator extends BaseValidator<ValidCurrency, String> {

  @Override
  boolean validate(String value) {
    try {
      Currency.getInstance(value);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public void initialize(ValidCurrency constraintAnnotation) {
    this.setMessage(constraintAnnotation.message());
  }
}
