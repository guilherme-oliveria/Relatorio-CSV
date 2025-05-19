package br.jus.tjro.gabinete.fonte.dados;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosQualifier;
import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.util.ParametrosUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FonteDadosQualifier(FonteDadosEnum.PJEPG)
public class PjePG implements FonteDados {

    @Autowired
    private ParametrosUtil parametro;

    @Override
    public String getUrlBase() {
        return parametro.getValorString("URL_PJE_PG_REST");
    }

    @Override
    public FonteDadosEnum getFonteDadosEnum() {
        return FonteDadosEnum.PJEPG;
    }

    @Override
    public String getUriRelativaProcessoConcluso() {
        return "/processo/conclusos";
    }

}
