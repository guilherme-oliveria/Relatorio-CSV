package br.jus.tjro.gabinete.config.cors;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NO_CONTENT;


public class CorsEnableFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(response instanceof HttpServletResponse){
            var it = ((HttpServletResponse) response);
            if(it.getHeader("Access-Control-Allow-Origin") == null)
                it.addHeader("Access-Control-Allow-Origin", "*");
            if(it.getHeader("Access-Control-Allow-Methods") == null)
                it.addHeader("Access-Control-Allow-Methods", String.join(",", AllowMethods.values));
            if(it.getHeader("Access-Control-Allow-Headers") == null)
                it.addHeader("Access-Control-Allow-Headers", String.join(",", AllowHeaders.values));
            if (request instanceof HttpServletRequest && ((HttpServletRequest) request).getMethod().equals("OPTIONS")) {
                it.addHeader("Access-Control-Max-Age", "1728000");
                it.setStatus(NO_CONTENT.value());
            }
        }
        chain.doFilter(request, response);
    }
}
