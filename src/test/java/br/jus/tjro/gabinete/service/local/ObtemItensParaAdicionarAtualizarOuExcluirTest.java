package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.interfaces.importacao.EntidadeDoisBancos;
import br.jus.tjro.gabinete.model.gab.enums.TipoPolo;
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.model.gab.transiente.Partes;
import br.jus.tjro.gabinete.service.importacao.ObtemItensParaAdicionarAtualizarOuExcluir;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObtemItensParaAdicionarAtualizarOuExcluirTest {

    @Test
    public void existeEntidadeLocal(){
        ProcessoParte local = new ProcessoParte();
        Partes remoto = new Partes();
        local.setIdParteLegado(666l);
        remoto.setId(666l);

        List<EntidadeDoisBancos> listaLocal = new ArrayList<>();
        List<EntidadeDoisBancos> listaRemota = new ArrayList<>();

        listaLocal.add(local);
        listaRemota.add(remoto);

        var ob = new ObtemItensParaAdicionarAtualizarOuExcluir(listaLocal,listaRemota);
        assertEquals(0,ob.getListaAdicao().size());
        assertEquals(0,ob.getListaExclusao().size());
        assertEquals(0,ob.getListaAtualizacao().size());
    }


    @Test
    public void umRegistroRemotoDeveSerAdicionado(){
        Partes remoto = new Partes();
        remoto.setId(1l);
        remoto.setTipoPolo(TipoPolo.A);

        List<EntidadeDoisBancos> listaLocal = new ArrayList<>();
        List<EntidadeDoisBancos> listaRemota = new ArrayList<>();

        listaRemota.add(remoto);

        var ob = new ObtemItensParaAdicionarAtualizarOuExcluir(listaLocal,listaRemota);
        assertEquals(1,ob.getListaAdicao().size());
        assertEquals(0,ob.getListaExclusao().size());
        assertEquals(0,ob.getListaAtualizacao().size());
    }

    @Test
    public void adicionaUmEhRemoveUm(){
        ProcessoParte local = new ProcessoParte();
        Partes remoto = new Partes();
        local.setIdParteLegado(666l);
        remoto.setId(66l);

        List<EntidadeDoisBancos> listaLocal = new ArrayList<>();
        List<EntidadeDoisBancos> listaRemota = new ArrayList<>();

        listaLocal.add(local);
        listaRemota.add(remoto);

        var ob = new ObtemItensParaAdicionarAtualizarOuExcluir(listaLocal,listaRemota);
        assertEquals(1,ob.getListaAdicao().size());
        assertEquals(1,ob.getListaExclusao().size());
        assertEquals(0,ob.getListaAtualizacao().size());
    }

    @Test
    public void adicionaUmEhMantemOutro(){
        ProcessoParte local = new ProcessoParte();
        Partes remoto1 = new Partes();
        Partes remoto2 = new Partes();
        local.setIdParteLegado(666l);
        remoto1.setId(666l);
        remoto2.setId(66l);

        List<EntidadeDoisBancos> listaLocal = new ArrayList<>();
        List<EntidadeDoisBancos> listaRemota = new ArrayList<>();

        listaLocal.add(local);
        listaRemota.add(remoto1);
        listaRemota.add(remoto2);

        var ob = new ObtemItensParaAdicionarAtualizarOuExcluir(listaLocal,listaRemota);
        assertEquals(1,ob.getListaAdicao().size());
        assertEquals(0,ob.getListaExclusao().size());
        assertEquals(0,ob.getListaAtualizacao().size());
    }

    @Test
    public void verificaParteParaAtualizar(){
        ProcessoParte local = new ProcessoParte();
        Partes remoto1 = new Partes();
        local.setIdParteLegado(666l);
        local.setTipoParte("SUPER SAYAJIN I");
        remoto1.setId(666l);
        remoto1.setTipoParte("SUPER SAYAJIN II");

        List<EntidadeDoisBancos> listaLocal = new ArrayList<>();
        List<EntidadeDoisBancos> listaRemota = new ArrayList<>();

        listaLocal.add(local);
        listaRemota.add(remoto1);

        var ob = new ObtemItensParaAdicionarAtualizarOuExcluir(listaLocal,listaRemota);
        assertEquals(0, ob.getListaAdicao().size(), "Lista adicao");
        assertEquals(0, ob.getListaExclusao().size(), "Lista Exclusao");
        assertEquals(1, ob.getListaAtualizacao().size(), "Lista Atualizacao");
    }

    @Test
    public void processoComDuasVezesMesmaPessoa(){
        Pessoa p1Local = new Pessoa();
        Pessoa p2Local = new Pessoa();

        p1Local.setId(1l);
        p1Local.setIdPessoaLegado(11l);
        p1Local.setNome("Andrew");

        p2Local.setId(2l);
        p2Local.setIdPessoaLegado(22l);
        p2Local.setNome("Windson");

        Pessoa p1Remoto = new Pessoa();
        p1Remoto.setId(11l);
        p1Remoto.setIdPessoaLegado(11l);
        p1Remoto.setNome("Andrew May");

        Pessoa p2Remoto = new Pessoa();
        p2Remoto.setId(22l);
        p2Remoto.setIdPessoaLegado(22l);
        p2Remoto.setNome("Windson");

        Pessoa p3Remoto = new Pessoa();
        p3Remoto.setId(22l);
        p3Remoto.setIdPessoaLegado(22l);
        p3Remoto.setNome("Windson");

        Pessoa p4Remoto = new Pessoa();
        p4Remoto.setId(44l);
        p4Remoto.setIdPessoaLegado(44l);
        p4Remoto.setNome("Stz");

        List<EntidadeDoisBancos> listaLocal = new ArrayList<>();
        List<EntidadeDoisBancos> listaRemota = new ArrayList<>();

        listaLocal.add(p1Local);
        listaLocal.add(p2Local);

        listaRemota.add(p1Remoto);
        listaRemota.add(p2Remoto);
        listaRemota.add(p3Remoto);
        listaRemota.add(p4Remoto);

        var ob = new ObtemItensParaAdicionarAtualizarOuExcluir(listaLocal,listaRemota);
        assertEquals(1, ob.getListaAdicao().size(), "Lista adicao");
        assertEquals(0, ob.getListaExclusao().size(), "Lista Exclusao");
        assertEquals(1, ob.getListaAtualizacao().size(), "Lista Atualizacao");
    }

    @Test
    public void pessoaExclusao(){
        Pessoa p1Local = new Pessoa();

        p1Local.setId(1l);
        p1Local.setIdPessoaLegado(11l);
        p1Local.setNome("Andrew");

        Pessoa p1Remoto = new Pessoa();
        p1Remoto.setId(2l);
        p1Remoto.setIdPessoaLegado(2l);
        p1Remoto.setNome("Andrew May");


        List<EntidadeDoisBancos> listaLocal = new ArrayList<>();
        List<EntidadeDoisBancos> listaRemota = new ArrayList<>();

        listaLocal.add(p1Local);

        listaRemota.add(p1Remoto);

        var ob = new ObtemItensParaAdicionarAtualizarOuExcluir(listaLocal,listaRemota);
        assertEquals(1, ob.getListaAdicao().size(), "Lista adicao");
        assertEquals(1, ob.getListaExclusao().size(), "Lista Exclusao");
        assertEquals(0, ob.getListaAtualizacao().size(), "Lista Atualizacao");
    }

    @Test
    public void verificaParteParaAtualizarPoremEhDeleteEhInsert(){
        ProcessoParte local = new ProcessoParte();
        Partes remoto1 = new Partes();
        local.setIdParteLegado(666l);
        local.setTipoParte("SUPER SAYAJIN I");
        remoto1.setId(661l);
        remoto1.setTipoParte("SUPER SAYAJIN II");

        List<EntidadeDoisBancos> listaLocal = new ArrayList<>();
        List<EntidadeDoisBancos> listaRemota = new ArrayList<>();

        listaLocal.add(local);
        listaRemota.add(remoto1);

        var ob = new ObtemItensParaAdicionarAtualizarOuExcluir(listaLocal,listaRemota);
        assertEquals(1, ob.getListaAdicao().size(), "Lista adicao");
        assertEquals(1, ob.getListaExclusao().size(), "Lista de exclusao");
        assertEquals(0, ob.getListaAtualizacao().size(), "Lista de update");
    }
}
