package br.jus.tjro.gabinete.infrastructure.filters;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.stream.Stream.of;

@Component
public class IPAddressFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var res = ((HttpServletResponse) response);
        var req = ((HttpServletRequest) request);
        of("X-Forward-For", "X-Forwarded-For")
            .map(s -> ofNullable(req.getHeader(s)))
            .filter(Optional::isPresent)
            .findFirst().ifPresentOrElse(s -> {
                if(s.isPresent()) {
                    res.addHeader("ipAddress", s.get());
                    MDC.put("ipAddress", s.get());
                }
            }, () -> {});
        chain.doFilter(request, response);
    }
}
