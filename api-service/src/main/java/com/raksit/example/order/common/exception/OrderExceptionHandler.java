package com.raksit.example.order.common.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.KafkaException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class OrderExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<OrderExceptionResponse> handleOrderNotFound() {
    return new ResponseEntity<>(OrderExceptionResponse.builder()
        .timestamp(LocalDateTime.now())
        .message("Order Not Found")
        .build(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(KafkaException.class)
  public ResponseEntity<OrderExceptionResponse> handleKafkaException() {
    return new ResponseEntity<>(OrderExceptionResponse.builder()
        .timestamp(LocalDateTime.now())
        .message("Cannot send order notification")
        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    return new ResponseEntity<>(OrderExceptionResponse.builder()
        .timestamp(LocalDateTime.now())
        .message(ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage())
        .build(), HttpStatus.BAD_REQUEST);
  }
}
