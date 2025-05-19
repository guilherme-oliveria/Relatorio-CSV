package br.jus.tjro.gabinete.model.docs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ModeloDocumentoEnvTest {
    @Test
    public void criacaoDosEnvsDocs() {
        ModeloDocumentoEnv defaultDocs = new ModeloDocumentoEnv("", "", "http://localhost:4201");
        assertEquals( "http://localhost:9765", defaultDocs.getBackUrl());
        assertEquals( "http://localhost:4201", defaultDocs.getFrontUrl());
        assertTrue(defaultDocs.temFront());
    }

    @Test
    public void criacaoDosEnvsDocsSemOFront() {
        ModeloDocumentoEnv defaultDocs = new ModeloDocumentoEnv("", "", null);
        assertFalse(defaultDocs.temFront());
    }

    @Test
    public void verificaJson() throws JsonProcessingException {
        ModeloDocumentoEnv defaultDocs = new ModeloDocumentoEnv("", "", "http://localhost:4201");
        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(defaultDocs);
        assertThat(json, containsString("temFront"));
    }
}
