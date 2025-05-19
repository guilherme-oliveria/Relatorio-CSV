package br.jus.tjro.gabinete.infrastructure.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


public class BytesJsonTest {
    @Test
    public void serialization() throws JsonProcessingException {
        byte[] bytes = "Jederson Gadelha Windson".getBytes();
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("arquivo", bytes);
        ObjectMapper mapper = new ObjectMapper();
        String retorno = mapper.writeValueAsString(map);
    }
}
