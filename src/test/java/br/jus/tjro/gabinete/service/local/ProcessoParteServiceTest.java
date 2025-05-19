package br.jus.tjro.gabinete.service.local;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.jus.tjro.gabinete.builders.service.BuilderParteRemotoService;
import br.jus.tjro.gabinete.builders.service.BuilderProcessoParteService;
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.model.gab.enums.TipoPolo;
import br.jus.tjro.gabinete.model.gab.transiente.Partes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ProcessoParteServiceTest {

    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("br.jus.tjro.gabinete.fixtures");
    }

    @Test
    public void deveSalvarTodosItensRemoto() throws Exception {
        List<ProcessoParte> partesLocais = new ArrayList<>();
        List<Partes> partesRemoto = getPartesRemoto();
        Processo processo = Fixture.from(Processo.class).gimme("paraImportacao");

        BuilderProcessoParteService builderService = new BuilderProcessoParteService();
        ProcessoParteService service = builderService.get();
        List<Pessoa> pessoasLocais = null;
        List<ProcessoParte> resul = service.verificaQuaisPartesDevemSerAtualizadas(pessoasLocais, partesLocais, partesRemoto, processo);
        assertEquals(4,resul.size());
    }

    @Test
    public void deveSalvarItensRemotoEhRemoverItemQueJaExisteLocal() throws Exception {
        List<ProcessoParte> partesLocais = new ArrayList<>();
        List<Partes> partesRemoto = getPartesRemoto();

        Partes parteJaExistente = partesRemoto.stream().findFirst().get();
        ProcessoParte parteNoBanco = new ProcessoParte();
        parteNoBanco.setIdParteLegado(parteJaExistente.getId());
        parteNoBanco.setTipoPolo(parteJaExistente.getTipoPolo());
        parteNoBanco.setTipoParte(parteJaExistente.getTipoParte());
        partesLocais.add(parteNoBanco);

        Processo processo = Fixture.from(Processo.class).gimme("paraImportacao");

        BuilderProcessoParteService builderService = new BuilderProcessoParteService();
        ProcessoParteService service = builderService.get();
        List<Pessoa> pessoasLocais = null;
        List<ProcessoParte> resul = service.verificaQuaisPartesDevemSerAtualizadas(pessoasLocais, partesLocais, partesRemoto, processo);
        assertEquals(3,resul.size());
    }


    @Test
    public void deveSalvarItemsRemotoEhAtualizarItemLocal() throws Exception {
        List<ProcessoParte> partesLocais = new ArrayList<>();
        List<Partes> partesRemoto = getPartesRemoto();

        Partes parteJaExistente = partesRemoto.stream().findFirst().get();
        ProcessoParte parteNoBanco = new ProcessoParte();
        Long id = parteJaExistente.getId();
        parteNoBanco.setIdParteLegado(id);
        TipoPolo poloQueMudou = parteJaExistente.getTipoPolo();
        if(!poloQueMudou.equals(TipoPolo.A))
            parteNoBanco.setTipoPolo(TipoPolo.A);
        else
            parteNoBanco.setTipoPolo(TipoPolo.P);
        parteNoBanco.setTipoParte(parteJaExistente.getTipoParte());
        partesLocais.add(parteNoBanco);

        Processo processo = Fixture.from(Processo.class).gimme("paraImportacao");

        BuilderProcessoParteService builderService = new BuilderProcessoParteService();
        ProcessoParteService service = builderService.get();
        List<Pessoa> pessoasLocais = null;
        List<ProcessoParte> resul = service.verificaQuaisPartesDevemSerAtualizadas(pessoasLocais, partesLocais, partesRemoto, processo);

        ProcessoParte parteAtualizada = resul.stream().filter(p -> p.getIdParteLegado() == id).findFirst().orElse(null);

        assertNotEquals(null, parteAtualizada);
        assertEquals(4, resul.size());
        assertEquals(poloQueMudou, parteAtualizada.getTipoPolo());
    }

    public List<Partes> getPartesRemoto(){
        BuilderParteRemotoService builder = new BuilderParteRemotoService();
        return builder.getPartes();
    }

    @Test
    public void removePessoasQueJaExistemEmListaDePartesTest() throws Exception {
        BuilderProcessoParteService builderService = new BuilderProcessoParteService();
        ProcessoParteService service = builderService.get();
        List<ProcessoParte> processoPartes = new ArrayList<>();

        Pessoa pessoa1 = new Pessoa();
        pessoa1.setIdPessoaLegado(1l);
        ProcessoParte parte1 = new ProcessoParte();
        parte1.setPessoa(pessoa1);

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setIdPessoaLegado(2l);

        ProcessoParte parte2 = new ProcessoParte();
        parte2.setPessoa(pessoa2);

        ProcessoParte parte3 = new ProcessoParte();
        parte3.setPessoa(pessoa2);

        processoPartes.add(parte1);
        processoPartes.add(parte2);
        processoPartes.add(parte3);
        assertEquals(3,processoPartes.size());
        List<Pessoa> retorno = service.removePessoasQueJaExistemEmListaDePartes(processoPartes);
        assertEquals(2,retorno.size());
    }

    @Test
    public void existePessoaParaDuasPartesRemoto() throws Exception {
        BuilderProcessoParteService builderService = new BuilderProcessoParteService();
        ProcessoParteService service = builderService.get();

        List<Pessoa> pessoasLocais = new ArrayList<>();
        pessoasLocais.add(getPessoaLocalStatic());

        List<Partes> partesRemoto = new ArrayList<>();

        assertNotEquals(getParte1().getId(),getParte2().getId());
        assertEquals(getParte1().getPessoa().getId(),getParte2().getPessoa().getId());

        partesRemoto.add(getParte1());
        partesRemoto.add(getParte2());

        List<ProcessoParte> partesLocais = new ArrayList<>();

        Processo processo = new Processo();

        List<ProcessoParte> retorno = service.verificaQuaisPartesDevemSerAtualizadas(pessoasLocais, partesLocais, partesRemoto, processo);
        assertEquals(2,retorno.size());

        for ( ProcessoParte p : retorno) {
            assertEquals(6L, p.getPessoa().getId());
            assertEquals(666L, p.getPessoa().getIdPessoaLegado());
        }


    }

    private Pessoa getPessoaRemotoStatic(){
        Pessoa pessoa = new Pessoa();
        pessoa.setEmail("email");
        pessoa.setId(666l);
        return pessoa;
    }

    private Pessoa getPessoaLocalStatic(){
        Pessoa pessoa = new Pessoa();
        pessoa.setEmail("email");
        pessoa.setId(6l);
        pessoa.setIdPessoaLegado(666l);
        return pessoa;
    }

    private Partes getParte1() {
        Partes parte = new Partes();
        parte.setId(1l);
        parte.setNome("windson");
        parte.setNrDocumento("numero");
        parte.setEmail("email");
        parte.setLogin("login");
        parte.setTipoParte("Passivo");
        parte.setTipoPolo(TipoPolo.P);

        parte.setPessoa(getPessoaRemotoStatic());
        parte.setIdPessoaLegado(getPessoaRemotoStatic().getId().intValue());
        return parte;
    }

    private Partes getParte2() {
        Partes parte = new Partes();
        parte.setId(2l);
        parte.setNome("windson");
        parte.setNrDocumento("numero");
        parte.setEmail("email");
        parte.setLogin("login");
        parte.setTipoParte("Ativo");
        parte.setTipoPolo(TipoPolo.A);

        parte.setPessoa(getPessoaRemotoStatic());
        parte.setIdPessoaLegado(getPessoaRemotoStatic().getId().intValue());
        return parte;
    }

}
