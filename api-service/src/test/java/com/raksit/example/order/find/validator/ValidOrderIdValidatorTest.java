package com.raksit.example.order.find.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    assertTrue(validator.validate("bbad724a-5645-4af3-b17d-0f5ee006ee50"));
  }

  @Test
  void shouldReturnFalseWhenValidateGivenUUIDFromInvalidString() {
    // When
    // Then
    assertFalse(validator.validate("abcd"));
  }
}