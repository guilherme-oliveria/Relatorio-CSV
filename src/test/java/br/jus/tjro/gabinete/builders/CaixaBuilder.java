package br.jus.tjro.gabinete.builders;

import br.jus.tjro.gabinete.model.gab.localizador.Caixa;

public class CaixaBuilder {

    private Caixa caixa;

    private CaixaBuilder() {
    }

    public static CaixaBuilder umaCaixa() {
        CaixaBuilder builder = new CaixaBuilder();
        builder.caixa = new Caixa();
        builder.caixa.setNome("Decisão");
        return builder;
    }

    public CaixaBuilder nome(String nome) {
        caixa.setNome(nome);
        return this;
    }

    public Caixa agora() {
        return caixa;
    }
}
