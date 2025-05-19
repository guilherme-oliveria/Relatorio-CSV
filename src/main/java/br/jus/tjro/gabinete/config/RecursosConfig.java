package br.jus.tjro.gabinete.config;

import br.jus.tjro.gabinete.model.RecursosDoPapel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class RecursosConfig {
    @Bean
    public RecursosDoPapel recursoBean(@Value("${recursos.json:{\"administrador\":[\"Administrador\"],\"magistrado\":[\"magistrado\"],\"assessor\":[\"Diretor de Secretaria\",\"Assessor\",\"Assessor TJRO\"]}}") String recursos) throws JsonProcessingException {
        TypeReference<Map<String, List<String>>> typeRef = new TypeReference<Map<String, List<String>>>() {
        };
        return recursoBean(new ObjectMapper().readValue(recursos, typeRef));
    }

    public RecursosDoPapel recursoBean(Map<String, List<String>> recursos) {
        return new RecursosDoPapel(recursos);
    }
}
