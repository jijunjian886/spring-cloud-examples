package com.neo.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Redis配置
 * Created by JJJ on 2019/5/30.
 */
@Configuration
public class RedisConfig {
    /**
     * Redis 单机配置
     * @return
     */
    @RefreshScope
    @Bean
    @ConfigurationProperties(prefix = "redis")
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        return new RedisStandaloneConfiguration();
    }

    /**
     * 连接池配置
     * @return
     */
    @RefreshScope
    @Bean
    @ConfigurationProperties(prefix = "redis.lettuce.pool")
    public GenericObjectPoolConfig genericObjectPoolConfig() {
        return new GenericObjectPoolConfig();
    }

    /**
     * Lettuce 连接工厂
     * @param redisStandaloneConfiguration
     * @param genericObjectPoolConfig
     * @return
     */
    @RefreshScope
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration, GenericObjectPoolConfig genericObjectPoolConfig) {
        return new LettuceConnectionFactory(redisStandaloneConfiguration, LettucePoolingClientConfiguration.builder().poolConfig(genericObjectPoolConfig).build());
    }

    /**
     * 实例化 RedisTemplate（key， 采用字符串方式序列化；value， 采用 Json 方式序列化）
     * 注意：
     * Springboot2.0 开始 Redis 配置引入了 Lettuce，自动装配默认使用的也是它，这里注入的就是 LettuceConnectionFactory 实例
     * 如要使用 Jedis 需要手动配置
     * @return
     */
    @RefreshScope
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory ) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(om);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        //redisTemplate.setEnableTransactionSupport(true); // 开启事务支持（慎用，非必须不要开启）
        return redisTemplate;
    }
}
