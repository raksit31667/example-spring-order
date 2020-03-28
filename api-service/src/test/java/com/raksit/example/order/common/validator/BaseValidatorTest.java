package com.raksit.example.order.common.validator;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseValidatorTest {

  @Mock
  private ConstraintValidatorContext validatorContext;

  @Mock
  private ConstraintViolationBuilder violationBuilder;

  @Spy
  private FakeBaseValidator validator;

  @Test
  void shouldReturnTrueWhenIsValidGivenValidateValueReturnsTrue() {
    // Given
    doReturn(true).when(validator).validate("fakeValue");

    // When
    // Then
    assertTrue(validator.isValid("fakeValue", validatorContext));
  }

  @Test
  void shouldReturnFalseAndSetContextMessageWhenIsValidGivenValidateValueReturnsFalse() {
    // Given
    validator.setMessage("fakeMessage");
    doReturn(false).when(validator).validate("fakeValue");
    when(validatorContext.buildConstraintViolationWithTemplate("fakeMessage"))
        .thenReturn(violationBuilder);
    when(violationBuilder.addConstraintViolation()).thenReturn(validatorContext);

    // When
    // Then
    assertFalse(validator.isValid("fakeValue", validatorContext));
    verify(validatorContext).disableDefaultConstraintViolation();
  }

  private @interface FakeAnnotation {

    String message() default "fakeMessage";
  }

  private static class FakeBaseValidator extends BaseValidator<FakeAnnotation, String> {

    @Override
    public boolean validate(String value) {
      return false;
    }

    @Override
    public void initialize(FakeAnnotation constraintAnnotation) {
      this.setMessage(constraintAnnotation.message());
    }
  }
}