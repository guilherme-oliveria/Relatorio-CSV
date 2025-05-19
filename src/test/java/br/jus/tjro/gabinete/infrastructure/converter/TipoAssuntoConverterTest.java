package br.jus.tjro.gabinete.infrastructure.converter;

import br.jus.tjro.gabinete.model.gab.tpu.TpuAssunto;
import br.jus.tjro.gabinete.service.local.tpu.TpuAssuntoService;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TipoAssuntoConverterTest {


    @Test
    public void testaQuandoColunaEhNull(){
        TpuAssuntoConverter converter = new TpuAssuntoConverter();
        converter.init(mock(TpuAssuntoService.class));
        TpuAssunto retorno = converter.convertToEntityAttribute(null);
        assertNull(retorno.getCodigo());
    }

    @Test
    public void testaQuandoMudaId() throws Exception {
        TpuAssuntoService service = mock(TpuAssuntoService.class);
        when(service.getAssunto(1l)).
            thenReturn(new TpuAssunto(1l,"windson","glosarrion",null,"situacao",false));
        when(service.getAssunto(2l)).
            thenReturn(new TpuAssunto(1l,"jederson gadelha","glosarrion",null,"situacao",false));

        TpuAssuntoConverter converter = new TpuAssuntoConverter();
        converter.init(service);
        TpuAssunto retorno = converter.convertToEntityAttribute(null);
        assertNull(retorno.getCodigo());

        retorno = converter.convertToEntityAttribute(1l);
        assertEquals("1",retorno.getCodigo().toString());
        assertEquals("windson",retorno.getDescricao());


        retorno = converter.convertToEntityAttribute(2l);
        assertEquals("2",retorno.getCodigo().toString());
        assertEquals("jederson gadelha",retorno.getDescricao());
    }
}
