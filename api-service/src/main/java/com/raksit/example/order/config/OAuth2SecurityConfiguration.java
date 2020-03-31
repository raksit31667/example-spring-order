package com.raksit.example.order.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.IssuerClaimVerifier;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class OAuth2SecurityConfiguration {

  @Autowired
  private RequestMappingHandlerMapping requestMappingHandlerMapping;

  @Value("${security.oauth2.resource.issuer}")
  private String issuer;

  @Value("${security.oauth2.resource.id}")
  private String resourceId;

  @Value("${security.oauth2.resource.jwk.keySetUri}")
  private String keySetUri;

  @Bean
  protected ResourceServerConfiguration resourceServerConfiguration() {
    ResourceServerConfiguration resourceServerConfiguration = new ResourceServerConfiguration() {
      @Override
      public void setConfigurers(List<ResourceServerConfigurer> configurers) {
        super.setConfigurers(configurers);
      }
    };
    resourceServerConfiguration
        .setConfigurers(Collections.singletonList(new ResourceServerConfigurerAdapter() {

          @Override
          public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            TokenStore tokenStore = jwkTokenStore();
            DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
            defaultTokenServices.setTokenStore(tokenStore);
            resources.resourceId(resourceId)
                .tokenStore(tokenStore)
                .tokenServices(defaultTokenServices);
          }

          @Override
          public void configure(HttpSecurity http) throws Exception {
            http.requestMatchers()
                .mvcMatchers(getRequestMappingPatterns())
                .and()
                .authorizeRequests()
                .anyRequest().fullyAuthenticated();
          }
        }));

    resourceServerConfiguration.setOrder(98);
    return resourceServerConfiguration;
  }

  private TokenStore jwkTokenStore() throws MalformedURLException {
    IssuerClaimVerifier issuerClaimVerifier = new IssuerClaimVerifier(new URL(issuer));
    return new JwkTokenStore(keySetUri, issuerClaimVerifier);
  }

  private String[] getRequestMappingPatterns() {
    return requestMappingHandlerMapping.getHandlerMethods()
        .entrySet().stream()
        .filter(entry -> entry.getValue().getMethodAnnotation(PreAuthorize.class) != null)
        .flatMap(entry -> entry.getKey().getPatternsCondition().getPatterns().stream())
        .toArray(String[]::new);
  }
}
