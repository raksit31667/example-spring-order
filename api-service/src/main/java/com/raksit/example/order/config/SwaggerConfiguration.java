package com.raksit.example.order.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.ImplicitGrant;
import springfox.documentation.service.LoginEndpoint;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.collect.Sets.newHashSet;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

  @Value("${security.oauth2.resource.id}")
  private String resourceId;

  @Value("${security.oauth2.tenant.id}")
  private String tenantId;

  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .tags(new Tag("Order", "Everything about order"))
        .consumes(newHashSet(MediaType.APPLICATION_JSON_VALUE))
        .produces(newHashSet(MediaType.APPLICATION_JSON_VALUE))
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.raksit.example.order"))
        .paths(PathSelectors.any())
        .build()
        .useDefaultResponseMessages(false)
        .securityContexts(securityContexts())
        .securitySchemes(securitySchemes());
  }

  @Bean
  public SecurityConfiguration swaggerSecurityConfiguration() {
    return SecurityConfigurationBuilder.builder()
        .clientId(resourceId)
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Order Service")
        .description("The Order Service serves as an example Spring project")
        .version("1.0")
        .build();
  }

  private List<SecurityContext> securityContexts() {
    return Collections.singletonList(SecurityContext.builder()
        .securityReferences(Collections.singletonList(
            new SecurityReference("spring_oauth", scopes())))
        .forPaths(PathSelectors.regex(".*"))
        .build());
  }

  private List<SecurityScheme> securitySchemes() {
    return Collections.singletonList(new OAuthBuilder()
        .name("spring_oauth")
        .grantTypes(implicitGrantTypes())
        .scopes(Arrays.asList(scopes()))
        .build());
  }

  private List<GrantType> implicitGrantTypes() {
    return Collections.singletonList(new ImplicitGrant(new LoginEndpoint(
  String.format("https://login.microsoftonline.com/%s/oauth2/v2.0/authorize", tenantId)),
        "id_token"));
  }

  private AuthorizationScope[] scopes() {
    return new AuthorizationScope[] {
        new AuthorizationScope(String.format("%s/.default", resourceId),
            "authenticate to make a request via Swagger")};
  }
}
