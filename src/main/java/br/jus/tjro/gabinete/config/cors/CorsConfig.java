package br.jus.tjro.gabinete.config.cors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

//@Configuration
@Component
public class CorsConfig {

    @Autowired
    private CorsCustomizerConfigurer corsCustomizerConfigurer;
    @Bean
    public CorsWebFilter corsConfigurer() {
        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/*", corsCustomizerConfigurer.getCorsConfiguration());
        return new CorsWebFilter(source);
    }
}
