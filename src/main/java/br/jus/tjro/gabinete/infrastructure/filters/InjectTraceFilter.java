package br.jus.tjro.gabinete.infrastructure.filters;

import brave.Tracer;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
class InjectTraceFilter extends GenericFilterBean {

    private final Tracer tracer = null;

// Resolver problema do traceId
//    InjectTraceFilter(Tracer tracer) {
//        this.tracer = tracer;
//    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        brave.Span currentSpan = null;
        if(this.tracer != null) {
            currentSpan = this.tracer.currentSpan();
        }
        if (currentSpan == null) {
            chain.doFilter(request, response);
            return;
        }
        var res = ((HttpServletResponse) response);
        res.addHeader("X-B3-TraceId", currentSpan.context().traceIdString());
        res.addHeader("X-B3-SpanId", currentSpan.context().spanId() + "");
        chain.doFilter(request, response);
    }

}
