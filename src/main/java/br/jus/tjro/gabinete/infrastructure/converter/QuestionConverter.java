package br.jus.tjro.gabinete.infrastructure.converter;

import br.jus.tjro.gabinete.model.gab.question.QuestionBase;
import br.jus.tjro.gabinete.repository.QuestionBaseRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;

import java.util.Map;
import java.util.Set;


@Component
public class QuestionConverter implements AttributeConverter<Set<QuestionBase>, String> {

    public static QuestionBaseRepository repository;

    public QuestionConverter(){
    }

    QuestionConverter(QuestionBaseRepository repository){
        QuestionConverter.repository = repository;
    }

    @Override
    public String convertToDatabaseColumn(Set<QuestionBase> question) {
        if(question == null)
            return null;
        Map<String, String> resultadoMap = question.stream().filter(it -> it.getValue() != null).map(QuestionBase::getKeyAndValue)
            .reduce((firstMap, secondMap) -> {firstMap.putAll(secondMap); return firstMap;}).orElse(null);
        ObjectMapper mapper = new ObjectMapper();
        try {
            if(resultadoMap == null)
                return null;
            return mapper.writeValueAsString(resultadoMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao realizar converter questionBase para Json",e);
        }
    }

    @Override
    public Set<QuestionBase> convertToEntityAttribute(String json) {
        Set<QuestionBase> resultado = null;
        try {
            resultado = repository.findAll();
            if(json != null) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> values = mapper.reader().forType(getType()).readValue(json);
                resultado.forEach(it -> it.setValue(values.get(it.getKey())));
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao realizar converter de questionBase",e);
        }
        return resultado;
    }

    private TypeReference<Map<String, String>> getType(){
        TypeReference<Map<String,String>> typeRef = new TypeReference<Map<String,String>>() {};
        return typeRef;
    }

    @Autowired
    public void init(QuestionBaseRepository repository) {
        QuestionConverter.repository = repository;
    }
}
