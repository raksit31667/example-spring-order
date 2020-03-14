package com.raksit.example.order.security;

import com.auth0.jwt.JWT;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import static com.google.common.collect.Lists.newArrayList;

public class JwtEvaluator extends SecurityExpressionRoot implements
    MethodSecurityExpressionOperations {

  public static final String APPLICATION_ROLE_CLAIM = "roles";

  public JwtEvaluator(Authentication authentication) {
    super(authentication);
  }

  public boolean hasApplicationRole(ApplicationRole role) {
    Optional<String> accessToken = Optional.ofNullable(authentication)
        .map(Authentication::getDetails)
        .filter(details -> details instanceof OAuth2AuthenticationDetails)
        .map(details -> ((OAuth2AuthenticationDetails) details).getTokenValue());

    List<String> applicationRoles = accessToken.map(JWT::decode)
        .filter(decodedJWT -> decodedJWT.getClaims().containsKey(APPLICATION_ROLE_CLAIM))
        .map(decodedJWT -> decodedJWT.getClaim(APPLICATION_ROLE_CLAIM).asList(String.class))
        .orElse(newArrayList());

    return applicationRoles.contains(role.getValue());
  }

  @Override
  public void setFilterObject(Object filterObject) {

  }

  @Override
  public Object getFilterObject() {
    return null;
  }

  @Override
  public void setReturnObject(Object returnObject) {

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
