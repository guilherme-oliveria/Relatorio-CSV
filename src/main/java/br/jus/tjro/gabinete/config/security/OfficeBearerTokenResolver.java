package br.jus.tjro.gabinete.config.security;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

public final class OfficeBearerTokenResolver implements BearerTokenResolver {

    @Override
    public String resolve(HttpServletRequest request) {
        return request.getHeader("Authorization") == null ?
            request.getHeader("Cookie") : new DefaultBearerTokenResolver().resolve(request);
    }
}
