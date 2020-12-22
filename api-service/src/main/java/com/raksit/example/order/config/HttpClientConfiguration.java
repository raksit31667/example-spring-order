package com.raksit.example.order.config;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
public class HttpClientConfiguration {

  @Bean
  public HttpComponentsClientHttpRequestFactory requestFactory() {
    CloseableHttpClient httpClient = HttpClients.custom()
        .setSSLHostnameVerifier(new NoopHostnameVerifier())
        .build();
    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setHttpClient(httpClient);
    return requestFactory;
  }
}
