package br.jus.tjro.gabinete.util;

import br.jus.tjro.gabinete.model.webjud.Parametro;
import br.jus.tjro.gabinete.service.local.ParametrosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;


@Component
@Deprecated
public class ParametrosUtil {

    private final HashMap<String, Parametro> parametros = new HashMap<String, Parametro>();

    @Autowired
    private ParametrosService parametroService;

    public <T> T getValor(Enum<?> configuracao) {
        return getValor(configuracao.name());
    }

    public <T> T getValor(String configuracaoNome) {

        Parametro parametro = getParametro(configuracaoNome);
        return (T) parametro.getValor();
    }

    public String getValorString(String configuracaoNome) {
        String retorno = getValor(configuracaoNome);
        return retorno;
    }

    public String recuperaValorParametro(String configuracao) {
        return getParametro(configuracao).getValor();
    }

    private Parametro getParametro(String configuracaoNome) {

        Parametro retorno;

        if (parametros.containsKey(configuracaoNome)) {
            retorno = parametros.get(configuracaoNome);
        } else {
            retorno = this.parametroService.recuperarParametroPorConfiguracaoNome(configuracaoNome);
            parametros.put(configuracaoNome, retorno);
        }

        return retorno;
    }
}
