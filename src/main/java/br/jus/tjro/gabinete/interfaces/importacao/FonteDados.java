package br.jus.tjro.gabinete.interfaces.importacao;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;

public interface FonteDados {

    String getUrlBase();

    FonteDadosEnum getFonteDadosEnum();

    String getUriRelativaProcessoConcluso();

}
