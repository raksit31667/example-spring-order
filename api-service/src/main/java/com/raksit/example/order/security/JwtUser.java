package com.raksit.example.order.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtUser {

  private String appId;
  private String issuer;
  private List<String> roles;
}
