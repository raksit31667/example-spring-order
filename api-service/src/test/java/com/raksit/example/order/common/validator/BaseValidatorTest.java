package com.raksit.example.order.common.validator;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
    assertThat(validator.isValid("fakeValue", validatorContext), is(true));
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
    assertThat(validator.isValid("fakeValue", validatorContext), is(false));
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