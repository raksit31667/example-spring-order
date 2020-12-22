package com.raksit.example.order.common.cache;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class ProductClient {

  private final HttpComponentsClientHttpRequestFactory requestFactory;

  public ProductClient(HttpComponentsClientHttpRequestFactory requestFactory) {
    this.requestFactory = requestFactory;
  }

  @Cacheable("beer")
  public Optional<Product> findById(long id) {
    ResponseEntity<Product[]> productResponse = new RestTemplate(requestFactory)
        .getForEntity(String.format("https://api.punkapi.com/v2/beers/%d", id), Product[].class);
    return Optional.ofNullable(productResponse.getBody()).map(products -> products[0]);
  }
}
