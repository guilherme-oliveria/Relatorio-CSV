package br.jus.tjro.gabinete.service.local;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.jus.tjro.gabinete.builders.service.BuilderProcessoParteEnderecoService;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParteEndereco;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProcessoParteEnderecoServiceTest {

    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("br.jus.tjro.gabinete.fixtures");
    }

    @Test
    public void devePassarPorTodosOsMetodosSemErro() throws Exception {
        BuilderProcessoParteEnderecoService builder = new BuilderProcessoParteEnderecoService();
        ProcessoParteEnderecoService service = builder.get();

        Processo processo = getProcesso();
        ProcessoParte partes = Fixture.from(ProcessoParte.class).gimme("valido");
        partes.setProcesso(processo);
        ProcessoParteEndereco retorno = service.importaOuAtualizaEnderecoDaParteDoProcesso(partes);
        assertNotEquals(null, retorno);
    }

    @Test
    public void enderecoExistenteNaoSalvaNada() throws Exception {
        BuilderProcessoParteEnderecoService builder = new BuilderProcessoParteEnderecoService();
        ProcessoParteEnderecoService service = builder.get();

        Processo processo = getProcesso();
        processo.setSistema(FonteDadosEnum.PJEPG);

        ProcessoParte parte = Fixture.from(ProcessoParte.class).gimme("valido");
        parte.setProcesso(processo);
        parte.setId(1l);
        parte.setIdParteLegado(22l);

        List<ProcessoParteEndereco> enderecos = new ArrayList<>();
        ProcessoParteEndereco endereco = new ProcessoParteEndereco();
        endereco.setId(6l);
        enderecos.add(endereco);
        parte.setProcessoParteEnderecos(enderecos);
        ProcessoParteEndereco retorno = service.importaOuAtualizaEnderecoDaParteDoProcesso(parte);
        assertNull(retorno);
    }

    @Test
    public void salvaEnderecoPoisExisteServidorRemoto() throws Exception {
        BuilderProcessoParteEnderecoService builder = new BuilderProcessoParteEnderecoService();
        ProcessoParteEnderecoService service = builder.get();

        Processo processo = getProcesso();
        processo.setSistema(FonteDadosEnum.PJESG);

        ProcessoParte parte = Fixture.from(ProcessoParte.class).gimme("valido");
        parte.setProcesso(processo);
        parte.setId(2l);
        parte.setIdParteLegado(22l);

        List<ProcessoParteEndereco> enderecos = new ArrayList<>();
        ProcessoParteEndereco endereco = new ProcessoParteEndereco();
        endereco.setId(6l);
        enderecos.add(endereco);
        parte.setProcessoParteEnderecos(enderecos);

        ProcessoParteEndereco retorno = service.importaOuAtualizaEnderecoDaParteDoProcesso(parte);
        assertNotNull(retorno);
        assertNotNull(retorno.getId());
    }

    private Processo getProcesso() {
        Processo processo = Fixture.from(Processo.class).gimme("paraImportacao");
        processo.setSistema(FonteDadosEnum.PJEPG);
        return processo;
    }
}
