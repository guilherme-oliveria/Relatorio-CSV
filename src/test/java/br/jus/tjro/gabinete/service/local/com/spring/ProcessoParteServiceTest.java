package br.jus.tjro.gabinete.service.local.com.spring;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.repository.gab.pessoa.PessoaRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoParteRepository;
import br.jus.tjro.gabinete.service.local.ProcessoParteService;
import br.jus.tjro.gabinete.service.remoto.ParteRemotoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ProcessoParteServiceTest {

    ProcessoParteService processoParteService;

    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("br.jus.tjro.gabinete.fixtures");
    }

    private ProcessoParteService getProcessoParteService() {
        ParteRemotoService parteRemotoService = mock(ParteRemotoService.class);
        ProcessoParteRepository processoParteRepository = mock(ProcessoParteRepository.class);
        PessoaRepository pessoaRepository = mock(PessoaRepository.class);
        return new ProcessoParteService(parteRemotoService, processoParteRepository, pessoaRepository);
    }

    @Test
    public void deveMontarObjPessoa() {
        processoParteService = getProcessoParteService();
        Pessoa pessoa = Fixture.from(Pessoa.class).gimme("valido");

        Pessoa pessoaMontada = new Pessoa(
            pessoa.getIdPessoaLegado(), pessoa.getNome(), pessoa.getEmail(),
            pessoa.getDataNascimento(), pessoa.getDataObito(), pessoa.getTipoPessoa(),
            FonteDadosEnum.PJEPG
        );

        assertThat(pessoa).isEqualToComparingFieldByField(pessoaMontada);
    }

    @Test
    public void deveMontarObjProcessoParte() {
        processoParteService = getProcessoParteService();
        Processo processo = Fixture.from(Processo.class).gimme("valid");

        ProcessoParte processoParte = Fixture.from(ProcessoParte.class).gimme("validoSemPessoa");
        processoParte.setProcesso(processo);

        ProcessoParte processoParteMontado = processoParteService.montaObjProcessoParte(
            processoParte.getIdParteLegado(), processoParte.getTipoParte(), processoParte.getProcuradoria(),
            processoParte.getTipoPolo(), processo, null);

        assertThat(processoParte).isEqualToComparingFieldByField(processoParteMontado);
    }
}
