package com.raksit.example.order.config;

import com.google.common.base.Joiner;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableCaching
public class RedisConfiguration extends CachingConfigurerSupport {

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
    redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    return redisTemplate;
  }

  @Bean
  public RedisCacheManager redisCacheManager(RedisTemplate<String, Object> redisTemplate) {
    RedisCacheWriter redisCacheWriter = RedisCacheWriter
        .nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory());
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(SerializationPair.fromSerializer(redisTemplate.getValueSerializer()));
    return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
  }

  @Override
  @Bean
  public KeyGenerator keyGenerator() {
    return (target, method, params) -> Joiner.on('|')
        .join(Stream.of(Optional.of(params).orElse(new Object[]{}))
        .map(String::valueOf)
        .collect(Collectors.toList()));
  }
}

