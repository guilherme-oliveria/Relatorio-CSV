package br.jus.tjro.gabinete.service.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class CacheService {

    private final CacheManager cacheManager;

    @Autowired
    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Cache listarCache(String cacheName) {
        Map<Object, Object> cacheContent = new HashMap<>();
        Cache cache = cacheManager.getCache(cacheName);
        return cache;
    }
}
