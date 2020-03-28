package com.raksit.example.order.create.validator;

import com.raksit.example.order.common.validator.BaseValidator;
import java.util.Currency;

public class ValidCurrencyValidator extends BaseValidator<ValidCurrency, String> {

  @Override
  public boolean validate(String value) {
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
