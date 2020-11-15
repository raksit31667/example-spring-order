package com.raksit.example.order.config;

import com.auth0.jwt.JWT;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import org.springframework.boot.actuate.metrics.web.servlet.DefaultWebMvcTagsProvider;
import org.springframework.boot.actuate.metrics.web.servlet.WebMvcTagsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class PrometheusMetricsConfiguration {

  @Bean
  public WebMvcTagsProvider webMvcTagsProvider() {
    return new ConsumerBasedWebMvcTagsProvider();
  }

  static class ConsumerBasedWebMvcTagsProvider extends DefaultWebMvcTagsProvider {

    @Override
    public Iterable<Tag> getTags(HttpServletRequest request, HttpServletResponse response, Object handler,
        Throwable exception) {
      return Tags.of(super.getTags(request, response, handler, exception)).and(getConsumerAppIdTags(request));
    }

    private Tag getConsumerAppIdTags(HttpServletRequest request) {
      try {
        Optional<String> jwtToken = Optional.ofNullable(request.getHeader("Authorization"))
            .map(authorization -> authorization.replaceFirst("Bearer ", ""));
        String consumerAppId = jwtToken
            .map(JWT::decode)
            .map(decodedJWT -> decodedJWT.getClaim("appid").as(String.class))
            .orElse("");
        return Tag.of("consumer", consumerAppId);
      } catch (Exception e) {
        return Tag.of("consumer", "");
      }
    }
  }
}
