package br.jus.tjro.gabinete.config;

import brave.sampler.Sampler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(type = "org.springframework.cloud.context.scope.refresh.RefreshScope")
public class RefreshScopedSamplerConfiguration {

    @Bean
	public Sampler mySampler() {
		return Sampler.ALWAYS_SAMPLE;
	}

}
