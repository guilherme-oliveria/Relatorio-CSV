package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.webjud.Parametro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ParametrosService {

    @Autowired
    private Environment env;

    public Parametro recuperarParametroPorConfiguracaoNome(String configuracaoNome) {
        String confApp = env.getProperty(configuracaoNome);
        if (confApp != null)
            return new Parametro(confApp);
        else
            throw new RuntimeException(
                "Não foi encontrado nenhum registro cadastrado para o parâmetro de configuração de nome: "
                    + configuracaoNome);
    }

}
