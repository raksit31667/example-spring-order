package com.raksit.example.order.common.cache;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(value = "spring.cache.type", havingValue = "redis")
public class ProductCacheTask implements ApplicationRunner {

  private final RedisTemplate<String, Object> redisTemplate;

  private final KeyGenerator keyGenerator;

  private final HttpComponentsClientHttpRequestFactory requestFactory;

  public ProductCacheTask(
      RedisTemplate<String, Object> redisTemplate, KeyGenerator keyGenerator,
      HttpComponentsClientHttpRequestFactory requestFactory) {
    this.redisTemplate = redisTemplate;
    this.keyGenerator = keyGenerator;
    this.requestFactory = requestFactory;
  }

  @Override
  public void run(ApplicationArguments args) {
    ResponseEntity<Product[]> productResponse = new RestTemplate(requestFactory)
        .getForEntity("https://api.punkapi.com/v2/beers", Product[].class);
    List<Product> products = Optional.ofNullable(productResponse.getBody())
        .map(Arrays::asList).orElse(Collections.emptyList());
    products.forEach(product ->
        redisTemplate.opsForValue().set(String.format("beer||B%s",
            keyGenerator.generate(null, null, product.getId())), product));
  }
}
