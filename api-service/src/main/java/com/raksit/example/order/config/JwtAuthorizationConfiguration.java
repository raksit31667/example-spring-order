package com.raksit.example.order.config;

import com.raksit.example.order.security.JwtEvaluator;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private ApplicationContext context;

  @Override
  protected MethodSecurityExpressionHandler createExpressionHandler() {
    JwtSecurityExpressionHandler handler = new JwtSecurityExpressionHandler();
    handler.setApplicationContext(context);
    return handler;
  }

  private static class JwtSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
        Authentication authentication, MethodInvocation invocation) {
      return new JwtEvaluator(authentication);
    }
  }
}
