package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.model.gab.transiente.MovimentoProcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

//import br.jus.tjro.gabinete.model.gab.Movimento;

@Service
public class MovimentoRemotoService {

    private final List<FonteDados> fontes;
    private final RestTemplate restTemplate;

    @Autowired
    public MovimentoRemotoService(List<FonteDados> fontes, RestTemplate restTemplate) {
        this.fontes = fontes;
        this.restTemplate = restTemplate;
    }

    private String getUrlBase(FonteDadosEnum fonte) throws Exception {
        return getFonte(fonte).getUrlBase() + "/movimento";
    }

    private FonteDados getFonte(FonteDadosEnum fonte) throws Exception {
        return fonte.selecionaFonte(fontes);
    }

    public List<MovimentoProcesso> pegaMovimentoProcesso(Long idProcessoLegado, FonteDadosEnum fonte) throws Exception {
        RequestEntity<Void> request;
        try {
            request = RequestEntity.get(URI.create(getUrlBase(fonte) + "/processo/" + idProcessoLegado + "/")).build();
        } catch (ResourceAccessException e) {
            throw new ResourceAccessException("Não foi possível estabelecer conexão com o web service do PJE");
        }
        MovimentoProcesso[] movimentos = restTemplate.exchange(request, MovimentoProcesso[].class).getBody();

        return movimentos == null  ? Arrays.asList() : Arrays.asList(movimentos);
    }

//	public List<Movimento> getAllMovimentos() throws Exception {
//		RequestEntity<Void> request;
//		try {
//			request = RequestEntity.getMap(URI.create(getUrlBase(FonteDadosEnum.PJEPG) + "/")).build();
//		} catch (ResourceAccessException e) {
//			throw new ResourceAccessException("Não foi possível estabelecer conexão com o web service do PJE");
//		}
//		return Arrays.asList(restTemplate.exchange(request, Movimento[].class).getBody());
//	}

//	public Long buscaIdPaiDeUmIdCnj(Long idCnj, FonteDadosEnum fonte) throws Exception {
//		Movimento movimento;
//		try {
//			RequestEntity<Void> request = RequestEntity.getMap(URI.create(getUrlBase(fonte) + "/id-pai/" + idCnj)).build();
//			movimento = restTemplate.exchange(request, Movimento.class).getBody();
//		} catch (ResourceAccessException e) {
//			throw new ResourceAccessException("Não foi possível estabelecer conexão com o web service do PJE");
//		}
//		return movimento != null ? movimento.getIdPai() : null;
//	}
}
