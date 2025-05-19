package br.jus.tjro.gabinete.scheduled.devolve.origem.builder;

import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimento;
import br.jus.tjro.sinapses.api.modelo.ClassificadorRegistroExemplo;

import java.io.UnsupportedEncodingException;
import com.itextpdf.io.codec.Base64;


public class ClassificadorRegistroExemploBuilder {

    ClassificadorRegistroExemplo classificadorRegistroExemplo;

    public ClassificadorRegistroExemploBuilder(Minuta minuta) throws UnsupportedEncodingException {
        MinutaMovimento movimento = minuta.getMinutaMovimentos().stream().findFirst().orElse(null);
        classificadorRegistroExemplo = new ClassificadorRegistroExemplo(
            "CLS_TIPO_MOVIMENTO_MAGISTRADON",
            movimento.getId().toString(),
            Base64.encodeBytes(minuta.getMinutaHtmlRenderizadoBytes()));
    }


}
