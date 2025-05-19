package br.jus.tjro.gabinete.infrastructure.json;

import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PessoaJsonTests {


    @Test
    public void descerializaPessoa() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Pessoa pessoa = mapper.reader().forType(Pessoa.class).readValue(json);
        assertEquals("DANIEL SOUZA DE OLIVEIRA",pessoa.getNome());
    }



    String json = "{\n" +
        "        \"id\": null,\n" +
        "        \"idPessoaLegado\": null,\n" +
        "        \"nome\": \"DANIEL SOUZA DE OLIVEIRA\",\n" +
        "        \"email\": null,\n" +
        "        \"dataNascimento\": null,\n" +
        "        \"dataObito\": null,\n" +
        "        \"pessoaDocumentos\": [],\n" +
        "        \"objetoUpdateString\": \"DANIEL SOUZA DE OLIVEIRA\",\n" +
        "        \"objetoKeyString\": null,\n" +
        "        \"in_tipo_pessoa\": null\n" +
        "      }";
}
