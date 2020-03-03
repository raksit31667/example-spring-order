package com.raksit.example.order.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ValidCurrencyValidatorTest {

  @Mock
  private ValidCurrency validCurrency;

  private ValidCurrencyValidator validator;

  @BeforeEach
  void setUp() {
    validator = new ValidCurrencyValidator();
    validator.initialize(validCurrency);
  }

  @Test
  void shouldReturnTrueWhenValidateGivenCurrencyAUD() {
    // When
    // Then
    assertTrue(validator.validate("AUD"));
  }

  @Test
  void shouldReturnTrueWhenValidateGivenCurrencyTHB() {
    // When
    // Then
    assertTrue(validator.validate("THB"));
  }

  @Test
  void shouldReturnFalseWhenValidateGivenCurrency123() {
    // When
    // Then
    assertFalse(validator.validate("123"));
  }

  @Test
  void shouldReturnFalseWhenValidateGivenCurrencyNull() {
    // When
    // Then
    assertFalse(validator.validate(null));
  }
}