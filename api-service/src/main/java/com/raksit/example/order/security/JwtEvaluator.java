package com.raksit.example.order.security;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class JwtEvaluator extends SecurityExpressionRoot implements
    MethodSecurityExpressionOperations {

  private final String issuer;

  public JwtEvaluator(Authentication authentication, String issuer) {
    super(authentication);
    this.issuer = issuer;
  }

  public boolean hasApplicationRole(ApplicationRole role) {
    return ((JwtUser) getPrincipal()).getRoles().contains(role.getValue());
  }

  public boolean isFromExpectedIssuer() {
    return ((JwtUser) getPrincipal()).getIssuer().equals(issuer);
  }

  @Override
  public void setFilterObject(Object filterObject) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object getFilterObject() {
    return null;
  }

  @Override
  public void setReturnObject(Object returnObject) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object getReturnObject() {
    return null;
  }

  @Override
  public Object getThis() {
    return null;
  }
}
