package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.UnitTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VisibilidadeTest {
    private final String jsons = "[{\"cpf\":\"018.470.042-61\",\"nome\":\"ANDREW RAMIRES MAY\",\"idPessoaLegado\":423324,\"idLegado\":846506},{\"cpf\":null,\"nome\":\"LaboratÃ³rio Biomed\",\"idPessoaLegado\":442719,\"idLegado\":626993},{\"cpf\":\"681.263.522-72\",\"nome\":\"LEILIANE NEVES DE FREITAS\",\"idPessoaLegado\":791223,\"idLegado\":602625},{\"cpf\":\"035.941.902-00\",\"nome\":\"SEBASTIAO RODRIGUES DE FREITAS\",\"idPessoaLegado\":833074,\"idLegado\":602624},{\"cpf\":\"528.750.832-87\",\"nome\":\"FABIOLA FERNANDES FREITAS\",\"idPessoaLegado\":88735,\"idLegado\":602623}]";
    private final String json = "{\"nome\":\"ANDREW RAMIRES MAY\",\"idPessoaLegado\":423324,\"idLegado\":846506}";

    @Test
    public void numerosCpf() {
        Visibilidade visibilidade = new Visibilidade("windson","018.470.042-61", 666L);
        assertEquals("01847004261",visibilidade.getNumerosCpf());
    }

    @Test
    public void jsonTest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Visibilidade[] visibilidade = mapper.reader().forType(Visibilidade[].class).readValue(jsons);
        assertEquals(5, visibilidade.length);
        assertEquals(846506L, visibilidade[0].getIdLegado().longValue());
    }

    @Test
    public void testCpfNull() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Visibilidade visibilidade = mapper.reader().forType(Visibilidade.class).readValue(json);
        assertEquals("",visibilidade.getCpf());
        assertEquals("",visibilidade.getNumerosCpf());
    }
}

