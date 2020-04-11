package com.raksit.example.order.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raksit.example.order.common.exception.OrderExceptionResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {

    ResponseEntity<OrderExceptionResponse> responseEntity = ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(OrderExceptionResponse.builder()
            .timestamp(LocalDateTime.now())
            .message("The request has not been applied because it lacks valid authentication " +
                "credentials for the target resource")
            .build());

    setHttpServletResponse(response, responseEntity);
  }

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {

    ResponseEntity<OrderExceptionResponse> responseEntity = ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(OrderExceptionResponse.builder()
            .timestamp(LocalDateTime.now())
            .message("The server understood the request but refuses to authorize it")
            .build());

    setHttpServletResponse(response, responseEntity);
  }

  private void setHttpServletResponse(HttpServletResponse response,
      ResponseEntity<OrderExceptionResponse> responseEntity) throws IOException {
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(responseEntity.getStatusCodeValue());
    response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
  }
}
