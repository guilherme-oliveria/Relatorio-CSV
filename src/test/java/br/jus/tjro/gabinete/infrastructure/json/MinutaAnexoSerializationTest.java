package br.jus.tjro.gabinete.infrastructure.json;

import br.jus.tjro.gabinete.config.ColegiadoEnvironment;
import br.jus.tjro.gabinete.config.JuizLeigoEnvironment;
import br.jus.tjro.gabinete.infrastructure.converter.TipoDocumentoConverter;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.repository.gab.TipoDocumentoRepository;
import br.jus.tjro.gabinete.repository.gab.TipoDocumentoRespositoryImp;
import br.jus.tjro.gabinete.service.local.TipoDocumentoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class MinutaAnexoSerializationTest {
    ObjectMapper mapper = new ObjectMapper();

    final String jsonReal = "{\"idMinutaPai\":2996681,\"statusUpload\":4,\"id\":382405,\"hash\":\"DE7EF9B283C42D382495C63FEC2A02E87BE7995A06C864999C816D83C0CB6031\",\"html\":null,\"descricao\":\"7002175-12.2021.8.22.0004 - Inteiro teor.pdf\",\"tipoDocumento\":\"118\",\"contentType\":\"application/pdf\",\"sigilo\":null,\"idMinutaSistemaLegado\":null,\"posicao\":null,\"valid\":true,\"minutaPaiTransient\":2996681,\"extensao\":\".pdf\"}```\n";

    @Test
    public void testa_minuta_com_json_do_front() throws JsonProcessingException {
        TipoDocumentoConverter.service = new TipoDocumentoService(mock(TipoDocumentoRespositoryImp.class), new ColegiadoEnvironment(List.of(),List.of(), ""), new JuizLeigoEnvironment(List.of()));
        final var anexo = mapper.readValue(jsonReal, MinutaAnexo.class);
        assertEquals( 2996681,anexo.getMinutaPai().getId());
        assertNotNull(anexo.getTipoDocumento());
    }

}

