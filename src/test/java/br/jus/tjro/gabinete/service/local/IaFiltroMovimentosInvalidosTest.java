package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.tpu.TpuMovimento;
import br.jus.tjro.gabinete.service.local.tpu.TpuMovimentoService;
import br.jus.tjro.gabinete.service.remoto.ia.IaFiltroMovimentosInvalidos;
import br.jus.tjro.sinapses.api.modelo.ClassificacaoClasse;
import br.jus.tjro.sinapses.api.modelo.ClassificacaoClasseConviccao;
import br.jus.tjro.sinapses.api.modelo.ClassificacaoResultado;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IaFiltroMovimentosInvalidosTest {
    @Test
    public void deveFiltrarOMeroExpediente() {
        ClassificacaoResultado classificacaoResultado = new ClassificacaoResultado();
        List<ClassificacaoClasseConviccao> classificacaoClasseConviccaos = List.of(
            new ClassificacaoClasseConviccao(new ClassificacaoClasse("11010", "Mero expediente"), new BigDecimal("10.0")),
            new ClassificacaoClasseConviccao(new ClassificacaoClasse("11018", "Recebimento de Embargos à Execução"), new BigDecimal("10.0"))
        );
        classificacaoResultado.setResultados(classificacaoClasseConviccaos);
        IaFiltroMovimentosInvalidos iaFiltroMovimentosInvalidos = new IaFiltroMovimentosInvalidos(getTpuMovimentoService());
        iaFiltroMovimentosInvalidos.filtrar(classificacaoResultado);
        assert(classificacaoResultado.getResultados().size() == 1);
    }

    private TpuMovimentoService getTpuMovimentoService() {
        TpuMovimentoService tpuMovimentoService = mock(TpuMovimentoService.class);
        var meroExpediente = new TpuMovimento(11010l);
        meroExpediente.setTemFilhos(true);
        meroExpediente.setFilhos(List.of(new TpuMovimento(666l), new TpuMovimento(6661l)));
        meroExpediente.setSituacao("A");
        var recebimentoEmbargo = new TpuMovimento(11018l);
        recebimentoEmbargo.setSituacao("A");
        recebimentoEmbargo.setTemFilhos(false);
        try {
            when(tpuMovimentoService.getMovimentos(any())).thenReturn(List.of(
                meroExpediente,
                recebimentoEmbargo
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tpuMovimentoService;
    }
}
