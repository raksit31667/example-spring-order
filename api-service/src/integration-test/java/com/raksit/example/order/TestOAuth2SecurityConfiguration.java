package com.raksit.example.order;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@Import(OrderApplication.class)
public class TestOAuth2SecurityConfiguration {

  @Value("${security.oauth2.resource.id}")
  private String resourceId;

  @Bean
  @Primary
  public TokenStore tokenStore() {
    return new InMemoryTokenStore();
  }

  @Bean
  public ResourceServerConfiguration testResourceServerConfiguration(TokenStore tokenStore) {
    ResourceServerConfiguration resourceServerConfiguration = new ResourceServerConfiguration();
    resourceServerConfiguration.setConfigurers(Collections.singletonList(new ResourceServerConfigurerAdapter() {

      @Override
      public void configure(ResourceServerSecurityConfigurer resources) {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);
        resources.resourceId(resourceId)
            .tokenStore(tokenStore)
            .tokenServices(tokenServices);
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
