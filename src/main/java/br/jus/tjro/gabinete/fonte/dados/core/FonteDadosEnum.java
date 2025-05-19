package br.jus.tjro.gabinete.fonte.dados.core;

import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;

import java.util.List;

import static java.lang.String.format;

public enum FonteDadosEnum {

    PJEPG("PJEPG"), PJESG("PJESG"), PROJUDI("PROJUDI"), SDSG("SDSG"), SIAC("SIAC");

    public String fonte;

    FonteDadosEnum(String fonte) {
        this.fonte = fonte;
    }

    public FonteDados selecionaFonte(List<FonteDados> fontes) {
        return fontes
            .stream()
            .filter(f -> f.getFonteDadosEnum().equals(this))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException(format("Fonte %s não encontrada!", this.fonte)));
    }
}
