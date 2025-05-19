package br.jus.tjro.gabinete.model.gab.minuta;

import br.jus.tjro.gabinete.UnitTest;
import br.jus.tjro.gabinete.config.ColegiadoEnvironment;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MinutaTest {

    @Test
    public void testa_minuta_foi_integrada() {
        Minuta minuta = new Minuta();
        minuta.setIdMinutaSistemaLegado("idLegado");
        minuta.setHashP7s("temAssinatura");
        assertEquals(true,minuta.foiIntegrada());
    }

    @Test
    public void testa_minuta_nao_integrada() {
        Minuta minuta = new Minuta();
        assertEquals(false,minuta.foiIntegrada());
    }

    @Test
    public void testa_minuta_nao_integrada_porem_assinada() {
        Minuta minuta = new Minuta();
        minuta.setHashP7s("assinatura");
        assertEquals(false,minuta.foiIntegrada());
    }

    @Test
    public void testa_minuta_nao_integrada_falta_anexo() {
        Minuta minuta = new Minuta();
        minuta.setIdMinutaSistemaLegado("idLegado");
        minuta.setHashP7s("temAssinatura");
        MinutaAnexo minutaAnexo = new MinutaAnexo();
        ArrayList<MinutaAnexo> anexos = new ArrayList<MinutaAnexo>();
        anexos.add(minutaAnexo);
        minuta.setAnexos(anexos);
        assertEquals(false,minuta.foiIntegrada());
    }

    @Test
    public void testa_minuta_integrada_com_anexo() {
        Minuta minuta = new Minuta();
        minuta.setIdMinutaSistemaLegado("idLegado");
        minuta.setHashP7s("temAssinatura");
        MinutaAnexo minutaAnexo = new MinutaAnexo();
        minutaAnexo.setHashP7s("assinatura");
        minutaAnexo.setIdMinutaSistemaLegado("idLegado");
        ArrayList<MinutaAnexo> anexos = new ArrayList<MinutaAnexo>();
        anexos.add(minutaAnexo);
        minuta.setAnexos(anexos);
        assertEquals(true,minuta.foiIntegrada());
    }

    @Test
    public void testa_minuta_integrada_com_anexo_lista_vazia() {
        Minuta minuta = new Minuta();
        minuta.setIdMinutaSistemaLegado("idLegado");
        minuta.setHashP7s("temAssinatura");
        minuta.setAnexos(new ArrayList());
        assertEquals(true,minuta.foiIntegrada());
    }

    @Test
    public void testaMinutaEquas() {
        Minuta minuta1 = new Minuta();
        Minuta minuta2 = new Minuta();
        minuta1.setId(1l);
        minuta2.setId(1l);
        assertTrue(minuta1.equals(minuta2));
    }

    @Test
    public void testaMinutaEquasNaoIguais() {
        Minuta minuta1 = new Minuta();
        Minuta minuta2 = new Minuta();
        minuta1.setId(1l);
        minuta2.setId(2l);
        assertFalse(minuta1.equals(minuta2));
    }

    @Test
    public void naoRemoveIdLegadoNaMinuta() {
        assertThrows(RuntimeException.class, () -> {
            Minuta minuta = new Minuta();
            minuta.setIdMinutaSistemaLegado("666");
            minuta.setIdMinutaSistemaLegado(null);
        });
    }

    @Test
    public void podeAdicionarNullQandoJaEstaNUll() {
        Minuta minuta = new Minuta();
        minuta.setIdMinutaSistemaLegado(null);
        assertNull(minuta.getIdMinutaSistemaLegado());
    }

    @Test
    public void naoRemoveIdLegadoNoAnexo() {
        assertThrows(RuntimeException.class, () -> {
            MinutaAnexo minutaAnexo = new MinutaAnexo();
            minutaAnexo.setIdMinutaSistemaLegado("666");
            minutaAnexo.setIdMinutaSistemaLegado(null);
        });
    }

    @Test
    public void podeAdicionarNullQandoJaEstaNUllNoAnexo() {
        MinutaAnexo anexo = new MinutaAnexo();
        anexo.setIdMinutaSistemaLegado(null);
        assertNull(anexo.getIdMinutaSistemaLegado());
    }

    @Test
    public void validaIsAssinaPauta() throws Exception {
        new ColegiadoEnvironment(List.of("tipoColegiado"),List.of("anexo"), "");
        Minuta minuta = new Minuta();
        minuta.setTipoDocumento(new TipoDocumento("tipoColegiado","windson",true,true));
        minuta.setPedidoInclusaoPauta(new PedidoInclusaoPauta());
        assertEquals(true,minuta.isAssinaPauta());
    }

    @Test()
    public void erroAoVerificarAssinaturaColegiadoSemPauta() {
        new ColegiadoEnvironment(List.of("tipoColegiado"),List.of("anexo"), "");
        Minuta minuta = new Minuta();
        minuta.setTipoDocumento(new TipoDocumento("tipoColegiado","windson",true,true));
        assertThrows(Throwable.class, minuta::isAssinaPauta);
    }

    @Test
    public void validaPautaNaoColegiado() throws Exception {
        new ColegiadoEnvironment(List.of("tipoColegiado"),List.of("anexo"), "");
        Minuta minuta = new Minuta();
        minuta.setTipoDocumento(new TipoDocumento("NaoColegiado","windson",true,true));
        assertEquals(false,minuta.isAssinaPauta());
    }
}

