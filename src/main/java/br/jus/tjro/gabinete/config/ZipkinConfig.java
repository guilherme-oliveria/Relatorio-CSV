package br.jus.tjro.gabinete.config;

import brave.context.slf4j.MDCScopeDecorator;
import brave.propagation.CurrentTraceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static brave.baggage.BaggageFields.*;
import static brave.baggage.CorrelationScopeConfig.SingleCorrelationField.newBuilder;

@Configuration
public class ZipkinConfig {
    @Bean
//    @LoadBalanced
    @Primary
    RestTemplate customRestTemplate() {
        return new RestTemplate() {{
//            this.setInterceptors(List.of(tracingClientHttpRequestInterceptor));
        }};
    }

    @Bean
    CurrentTraceContext.ScopeDecorator legacyIds() {
        return MDCScopeDecorator.newBuilder()
            .clear()
            .add(newBuilder(TRACE_ID)
                .name("X-B3-TraceId").build())
            .add(newBuilder(PARENT_ID)
                .name("X-B3-ParentSpanId").build())
            .add(newBuilder(SPAN_ID)
                .name("X-B3-SpanId").build())
            .add(newBuilder(SAMPLED)
                .name("X-Span-Export").build())
            .build();
    }
}
