package br.jus.tjro.gabinete.config.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CacheConfigurationProperties {

    private final long minutos = 60l;
    private final long timeoutSeconds = 5*minutos;
    private final int redisPort;
    private final String redisHost;
    private final Map<String, Long> cacheExpirations = new HashMap<>();
    private final String password;

    public CacheConfigurationProperties(@Value("${cache.redisHost:localhost}") String redisHost, @Value("${cache.redisPort:6379}") int redisPort,
                                        @Value("${cache.redisPassword:windson}") String password){
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.password = password;

        cacheExpirations.put("tiposDocumentos", 360l*minutos);
        cacheExpirations.put("tipoDocumento", 360l*minutos);
        cacheExpirations.put("variaveisModeloDocumento", 360l*minutos);
        cacheExpirations.put("usuario-orgao-julgador", 15*minutos);
        cacheExpirations.put("usuario-papel", 15*minutos);
        cacheExpirations.put("orgao-julgador", 60*minutos);
        cacheExpirations.put("tpuClasseArvore",360l*minutos);
        cacheExpirations.put("tpuClasse",360l*minutos);
        cacheExpirations.put("orgaos-julgadores-revisor",14400l*minutos);
        cacheExpirations.put("orgaos-julgadores-revisados",14400l*minutos);

    }

    public long getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public Map<String, Long> getCacheExpirations() {
       return cacheExpirations;
    }

    public String getPassword() {
        return password.equals("windson") ? null : password;
    }
}


