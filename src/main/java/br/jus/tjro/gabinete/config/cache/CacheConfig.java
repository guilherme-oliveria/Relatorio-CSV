//package br.jus.tjro.gabinete.config.cache;
//
//import com.github.benmanes.caffeine.cache.Caffeine;
//import com.github.benmanes.caffeine.cache.Ticker;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//
//import org.springframework.cache.caffeine.CaffeineCache;
//import org.springframework.cache.support.SimpleCacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
//import static br.jus.tjro.gabinete.config.cache.RedisConfig.createCacheConfiguration;
//
////@Configuration
//public class CacheConfig {
//
//
//    private final CacheConfigurationProperties properties;
//
//    private final Logger looger = LoggerFactory.getLogger(CacheConfig.class);
//
//    @Autowired
//    public CacheConfig(CacheConfigurationProperties properties) {
//        this.properties = properties;
//    }
//
//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, RedisCacheConfiguration redisCacheConfiguration) {
//        try{
//            Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
//
//            for (Map.Entry<String, Long> cacheNameAndTimeout : properties.getCacheExpirations().entrySet()) {
//                cacheConfigurations.put(cacheNameAndTimeout.getKey(), createCacheConfiguration(cacheNameAndTimeout.getValue()));
//            }
//            redisConnectionFactory.getConnection();
//            return RedisCacheManager
//                .builder(redisConnectionFactory)
//                .cacheDefaults(redisCacheConfiguration)
//                .withInitialCacheConfigurations(cacheConfigurations).build();
//        }catch (Exception e){
//            looger.error("Erro ao inicializar cache do redis",e);
//            SimpleCacheManager manager = new SimpleCacheManager();
//            manager.setCaches(getCaches(ticker()));
//            return manager;
//        }
//    }
//
//
//    private Collection<? extends Cache> getCaches(Ticker ticker) {
//        List<CaffeineCache> lista = properties.getCacheExpirations()
//            .entrySet().stream()
//            .map(k -> buildCache(k.getKey(), ticker, k.getValue().intValue()))
//            .collect(Collectors.toList());
//        return lista;
//    }
//
//    private CaffeineCache buildCache(String name, Ticker ticker, int minutesToExpire) {
//        return new CaffeineCache(name, Caffeine.newBuilder()
//            .expireAfterWrite(minutesToExpire, TimeUnit.MINUTES)
//            .ticker(ticker)
//            .build());
//    }
//
//    @Bean
//    public Ticker ticker() {
//        return Ticker.systemTicker();
//    }
//}
