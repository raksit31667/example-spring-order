package com.raksit.example.order;

import com.raksit.example.order.security.SecurityExceptionHandler;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
public class TestOAuth2SecurityConfiguration {

  @Value("${security.oauth2.resource.id}")
  private String resourceId;

  @Autowired
  private SecurityExceptionHandler securityExceptionHandler;

  @Bean
  public TokenStore tokenStore() {
    return new InMemoryTokenStore();
  }

  @Bean
  public ResourceServerConfiguration testResourceServerConfiguration(TokenStore tokenStore) {
    ResourceServerConfiguration resourceServerConfiguration = new ResourceServerConfiguration() {
      @Override
      public void setConfigurers(List<ResourceServerConfigurer> configurers) {
        super.setConfigurers(configurers);
      }
    };
    resourceServerConfiguration.setConfigurers(Collections.singletonList(new ResourceServerConfigurerAdapter() {

      @Override
      public void configure(ResourceServerSecurityConfigurer resources) {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);
        resources.resourceId(resourceId)
            .tokenStore(tokenStore)
            .tokenServices(tokenServices)
            .authenticationEntryPoint(securityExceptionHandler)
            .accessDeniedHandler(securityExceptionHandler);
      }

      @Override
      public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/").permitAll()
            .anyRequest().fullyAuthenticated();
      }
    }));

    resourceServerConfiguration.setOrder(1);
    return resourceServerConfiguration;
  }
}
