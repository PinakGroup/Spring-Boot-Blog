package com.li.blog.config.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import java.time.Duration;

/**
 * @author li
 * @version 1.0
 * @since 18-9-21 下午2:30
 **/
@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(connectionFactory);
        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration());
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        //有效时间
        cacheConfiguration = cacheConfiguration.entryTtl(Duration.ofDays(1));
        //修复热部署时的缓存异常
        JdkSerializationRedisSerializer redisSerializer = new JdkSerializationRedisSerializer(getClass().getClassLoader());
        SerializationPair serializationPair = SerializationPair.fromSerializer(redisSerializer);
        cacheConfiguration = cacheConfiguration.serializeValuesWith(serializationPair);
        //公共前缀
        cacheConfiguration = cacheConfiguration.prefixKeysWith("Blog===>");
        return cacheConfiguration;
    }
}
