package br.jus.tjro.gabinete.fonte.dados;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosQualifier;
import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.util.ParametrosUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@FonteDadosQualifier(FonteDadosEnum.PJESG)
public class PjeSG implements FonteDados {

    private final String urlBase;

    public PjeSG(@Value("${URL_PJE_SG_REST:http://localhost:8282}") String urlBase){
        this.urlBase = urlBase;
    }

    @Override
    public String getUrlBase() {
        return urlBase;
    }

    @Override
    public FonteDadosEnum getFonteDadosEnum() {
        return FonteDadosEnum.PJESG;
    }

    @Override
    public String getUriRelativaProcessoConcluso() {
        return "/processo/conclusos";
    }

}
