package com.eshop.cart.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.eshop.cart.domain.Cart;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Cart> redisTemplate(RedisConnectionFactory connectionFactory,
                                                     ObjectMapper objectMapper) {
        RedisTemplate<String, Cart> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        var keySerializer = new StringRedisSerializer();
        var valueSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        template.setKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashKeySerializer(keySerializer);
        template.setHashValueSerializer(valueSerializer);
        template.afterPropertiesSet();

        return template;
    }
}
