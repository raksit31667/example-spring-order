package com.raksit.example.order.find.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class ValidOrderIdValidatorTest {

  @Mock
  private ValidOrderId validOrderId;

  private ValidOrderIdValidator validator;

  @BeforeEach
  void setUp() {
    validator = new ValidOrderIdValidator();
    validator.initialize(validOrderId);
  }

  @Test
  void shouldReturnTrueWhenValidateGivenUUIDFromValidString() {
    // When
    // Then
    assertThat(validator.validate("bbad724a-5645-4af3-b17d-0f5ee006ee50"), is(true));
  }

  @Test
  void shouldReturnFalseWhenValidateGivenUUIDFromInvalidString() {
    // When
    // Then
    assertThat(validator.validate("abcd"), is(false));
  }
}