package com.smdb.spatialmeta.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class LettuceRedisConfig {



    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        try {
            // 测试连通性
            factory.getConnection();
        } catch (RedisConnectionFailureException e) {
            log.info("未发现redis服务");
        }
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //采用string的序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

}
