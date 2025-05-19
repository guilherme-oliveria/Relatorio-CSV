package br.jus.tjro.gabinete.scheduled.devolve.origem.builder;

import br.jus.tjro.gabinete.api.modelo.PublicarDJE;
import br.jus.tjro.gabinete.model.gab.minuta.PublicarProcessoDJE;

public class PublicarDjeBuilderApi {

    public static PublicarDJE convertePublicarProcessoDjeParaPublicarDje(PublicarProcessoDJE publicacao) {
        if (publicacao == null || (publicacao != null && !publicacao.getPublicar()))
            return null;
        String conteudo = publicacao.getConteudo();
        if (conteudo == null)
            conteudo = publicacao.getMinuta().getMinutaHtmlRenderizado();
        return new PublicarDJE(publicacao.getId().toString(),publicacao.getPrazo(), conteudo);
    }

}
