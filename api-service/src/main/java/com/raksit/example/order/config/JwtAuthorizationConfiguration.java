package com.raksit.example.order.config;

import com.raksit.example.order.security.JwtEvaluator;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JwtAuthorizationConfiguration extends GlobalMethodSecurityConfiguration {

  @Value("${security.oauth2.resource.issuer}")
  private String issuer;

  private final ApplicationContext context;

  public JwtAuthorizationConfiguration(ApplicationContext context) {
    this.context = context;
  }

  @Override
  protected MethodSecurityExpressionHandler createExpressionHandler() {
    JwtSecurityExpressionHandler handler = new JwtSecurityExpressionHandler(issuer);
    handler.setApplicationContext(context);
    return handler;
  }

  private static class JwtSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private final String issuer;

    public JwtSecurityExpressionHandler(String issuer) {
      this.issuer = issuer;
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
        Authentication authentication, MethodInvocation invocation) {
      return new JwtEvaluator(authentication, issuer);
    }
  }
}
