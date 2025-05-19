package br.jus.tjro.gabinete.service.local;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.jus.tjro.gabinete.builders.service.BuilderProcessoMovimentoService;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoDocumento;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoMovimento;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessoMovimentoServiceTest {
    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("br.jus.tjro.gabinete.fixtures");
    }

    @Test
    public void deveMontarObjProcessoMovimento() {
        Processo processo = Fixture.from(Processo.class).gimme("valid");
        ProcessoDocumento processoDocumento = Fixture.from(ProcessoDocumento.class).gimme("valido");

        ProcessoMovimento processoMovimento = Fixture.from(ProcessoMovimento.class).gimme("valido");
        processoMovimento.setProcesso(processo);
        processoMovimento.setProcessoDocumento(processoDocumento);

        ProcessoMovimento processoMovimentoMontado = new ProcessoMovimento(
            processo, processoMovimento.getMovimento(), processoDocumento,
            processoMovimento.getIdProcessoMovimentoLegado(), processoMovimento.getData_atualizacao(),
            processoMovimento.getDescricao(), processoMovimento.getIn_visibilidade_externa(),
            processoMovimento.getIn_ativo(), processoMovimento.getId_orgao_julgador(),
            processoMovimento.getId_orgao_julgador_colegiado(), processoMovimento.getNome_usuario(),
            processoMovimento.getCpf_usuario()
        );

        assertThat(processoMovimento).isEqualToComparingFieldByField(processoMovimentoMontado);
    }

    @Test
    public void devePassarSemImportarNada() throws Exception {
        BuilderProcessoMovimentoService builder = new BuilderProcessoMovimentoService();
        ProcessoMovimentoService service = builder.get();
        Processo processo = new Processo();
        processo.setId(3l);
        processo.setIdProcessoSistemaLegado(66l);
        processo.setSistema(FonteDadosEnum.PJEPG);

        List<ProcessoMovimento> retorno = service.importaOuAtualizaMovimentosDoProcesso(processo);
        assertEquals(retorno.size(), 0);
    }

    @Test
    public void deveImportarUmMovimentoPg() throws Exception {
        BuilderProcessoMovimentoService builder = new BuilderProcessoMovimentoService();
        ProcessoMovimentoService service = builder.get();
        Processo processo = new Processo();
        processo.setId(2l);
        processo.setIdProcessoSistemaLegado(6l);
        processo.setSistema(FonteDadosEnum.PJEPG);
        List<ProcessoMovimento> retorno = service.importaOuAtualizaMovimentosDoProcesso(processo);
        assertEquals(retorno.size(), 1);

    }

    @Test
    public void deveImportarDoisMovimentoSg() throws Exception {
        BuilderProcessoMovimentoService builder = new BuilderProcessoMovimentoService();
        ProcessoMovimentoService service = builder.get();
        Processo processo = new Processo();
        processo.setId(1l);
        processo.setIdProcessoSistemaLegado(6l);
        processo.setSistema(FonteDadosEnum.PJESG);
        List<ProcessoMovimento> retorno = service.importaOuAtualizaMovimentosDoProcesso(processo);
        assertEquals(retorno.size(), 2);

    }
}
