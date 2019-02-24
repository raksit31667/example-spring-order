package com.raksit.example.order.common.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class OrderExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<OrderExceptionResponse> handleOrderNotFound(OrderNotFoundException ex) {
    OrderExceptionResponse exceptionResponse = OrderExceptionResponse.builder()
        .timestamp(LocalDateTime.now())
        .message("Order Not Found")
        .build();

    return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
  }
}
