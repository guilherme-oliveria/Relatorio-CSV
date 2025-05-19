package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.model.gab.transiente.ParteEnderecoTransiente;
import br.jus.tjro.gabinete.model.gab.transiente.Partes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Service
public class ParteRemotoService extends RemotoServiceAbstract {

    private final List<FonteDados> fontes;
    private final RestTemplate restTemplate;

    @Autowired
    public ParteRemotoService(List<FonteDados> fontes, RestTemplate restTemplate) {
        this.fontes = fontes;
        this.restTemplate = restTemplate;
    }

    private String getUrlBase(FonteDadosEnum fonte) throws Exception {
        return getFonte(fonte).getUrlBase() + "/partes";
    }

    private FonteDados getFonte(FonteDadosEnum fonte) throws Exception {
        return fonte.selecionaFonte(fontes);
    }

    public List<Partes> buscaParteProcessoPorIdProcessoETipoPolo(String polo, Long idSistemaLegado, FonteDadosEnum fonte) throws Exception {
        Partes[] partes = pegarParteProcessoPorIdProcessoETipoPolo(polo, idSistemaLegado, fonte);
        if (partes == null)
            return null;
        return Arrays.asList(partes);
    }

    private Partes[] pegarParteProcessoPorIdProcessoETipoPolo(String polo, Long idProcessoLegado, FonteDadosEnum fonte) throws Exception {
        RequestEntity<Void> request;
        try {
            request = get(String.valueOf(URI.create(getUrlBase(fonte) + "/" + polo + "/processo/" + idProcessoLegado)));
        } catch (ResourceAccessException e) {
            throw new ResourceAccessException("Não foi possível estabelecer conexão com o web service do PJE");
        }
        return restTemplate.exchange(request, Partes[].class).getBody();
    }

    public List<Partes> pegarPartesEInteressadosProcessoPorIdProcesso(Long idProcessoLegado, FonteDadosEnum fonte) throws Exception {
        String url = getUrlBase(fonte) + "/e-interessados/processo/" + idProcessoLegado;
        RequestEntity<Void> request = get(url, "Erro ao buscar parte interessada do processo");
        Partes[] partesRemotoArray = restTemplate.exchange(request, Partes[].class).getBody();
        List<Partes> partesRemoto = partesRemotoArray != null ? Arrays.asList(partesRemotoArray) : Arrays.asList();
        partesRemoto.forEach(p-> {
            if(p.getPessoa().getId() != null)
                p.getPessoa().setIdPessoaLegado(p.getPessoa().getId());
        });
        return partesRemoto;
    }

    public ParteEnderecoTransiente pegarEnderecoDaParte(Long idProcessoParte, FonteDadosEnum fonte) throws Exception {
        RequestEntity<Void> request;
        try {
            request = get(String.valueOf(URI.create(getUrlBase(fonte) + "/endereco/" + idProcessoParte)));
        } catch (ResourceAccessException e) {
            throw new ResourceAccessException("Não foi possível estabelecer conexão com o web service do PJE");
        }
        return restTemplate.exchange(request, ParteEnderecoTransiente.class).getBody();
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
