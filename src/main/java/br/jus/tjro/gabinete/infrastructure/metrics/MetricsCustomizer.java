package br.jus.tjro.gabinete.infrastructure.metrics;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsCustomizer {

    @Value("${TJRO_STACK:gabinete}")
    private String stack;

    @Value("${TJRO_ENV:testing}")
    private String environment;

    @Value("${TJRO_APPTAG:gabinete}")
    private String tag;
//
//    @Bean
//    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
//        return registry -> registry.config().commonTags(
//            "tjro_stack", stack,
//            "tjro_instance_name", tag,
//            "tjro_environment", environment
//        );
//    }

}
