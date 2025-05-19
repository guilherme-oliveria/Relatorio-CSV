package br.jus.tjro.gabinete.config.cors;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CorsCustomizerConfigurer implements Customizer<CorsConfigurer<HttpSecurity>> {

    private String urlCors;
    public CorsCustomizerConfigurer(@Value("${URL_CORS:''}") String urlCors) {
        this.urlCors = urlCors;
    }

    @Override
    public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
        httpSecurityCorsConfigurer.configurationSource(getCorsConfigurationSource());
    }

    public CorsConfigurationSource getCorsConfigurationSource(){
        var configuration = getCorsConfiguration();
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/*", configuration);
        source.registerCorsConfiguration("/**", configuration);
        source.registerCorsConfiguration("/*/*", configuration);
        source.registerCorsConfiguration("*", configuration);
        source.setAllowInitLookupPath(true);
        return source;
    }

    public CorsConfiguration getCorsConfiguration(){
        List<String> cors = new ArrayList<String>();
        cors.add("https://*.tjro.jus.br:[*]");
        cors.add("*chrome-extension*");
        cors.addAll(Arrays.stream(this.urlCors.split(",")).toList());
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(cors);
        configuration.setAllowedMethods(AllowMethods.values);
        configuration.setMaxAge(3600L);
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(AllowHeaders.values);
        configuration.setExposedHeaders(AllowHeaders.values);
        return configuration;
    }


}
