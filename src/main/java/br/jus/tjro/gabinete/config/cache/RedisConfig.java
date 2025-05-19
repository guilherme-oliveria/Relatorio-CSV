package br.jus.tjro.gabinete.config.cache;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Duration;
//
//@Configuration
//public class RedisConfig {
//
//
//    public static ObjectMapper objectMapper() {
//        return Jackson2ObjectMapperBuilder.json()
//            .serializationInclusion(JsonInclude.Include.NON_NULL) // Don’t include null values
//            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) //ISODate
//            .build();
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory cf) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
//        //redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer(objectMapper()));
//        redisTemplate.setConnectionFactory(cf);
//        return redisTemplate;
//    }
//
//    @Bean
//    public RedisCacheConfiguration cacheConfiguration(CacheConfigurationProperties properties) {
//        return createCacheConfiguration(properties.getTimeoutSeconds());
//    }
//
//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory(CacheConfigurationProperties properties) {
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//        redisStandaloneConfiguration.setHostName(properties.getRedisHost());
//        if(properties.getPassword() != null)
//            redisStandaloneConfiguration.setPassword(properties.getPassword());
//        redisStandaloneConfiguration.setPort(properties.getRedisPort());
//        return new LettuceConnectionFactory(redisStandaloneConfiguration);
//    }
//
//    public static RedisCacheConfiguration createCacheConfiguration(long timeoutInSeconds) {
//        return RedisCacheConfiguration.defaultCacheConfig()
//            .disableCachingNullValues()
//            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
//            .entryTtl(Duration.ofSeconds(timeoutInSeconds));
//    }
//}
