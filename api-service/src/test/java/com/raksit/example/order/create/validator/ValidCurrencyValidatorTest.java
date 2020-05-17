package com.raksit.example.order.create.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
    assertThat(validator.validate("AUD"), is(true));
  }

  @Test
  void shouldReturnTrueWhenValidateGivenCurrencyTHB() {
    // When
    // Then
    assertThat(validator.validate("THB"), is(true));
  }

  @Test
  void shouldReturnFalseWhenValidateGivenCurrency123() {
    // When
    // Then
    assertThat(validator.validate("123"), is(false));
  }

  @Test
  void shouldReturnFalseWhenValidateGivenCurrencyNull() {
    // When
    // Then
    assertThat(validator.validate(null), is(false));
  }
}