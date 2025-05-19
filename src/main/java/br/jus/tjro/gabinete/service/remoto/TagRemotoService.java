package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.model.gab.tag.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import br.jus.tjro.gabinete.dto.TagProcessoDTO;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Service
public class TagRemotoService extends RemotoServiceAbstract {

    private final List<FonteDados> fontes;
    private final RestTemplate restTemplate;

    @Autowired
    public TagRemotoService(List<FonteDados> fontes, RestTemplate restTemplate) {
        this.fontes = fontes;
        this.restTemplate = restTemplate;
    }

    public List<Tag> getTagPorProcessoId(Long idProcessoLegado, FonteDadosEnum fonte) throws Exception {
        String url = getUrlBase(fonte) + "/processo/" + idProcessoLegado;
        RequestEntity<Void> request = get(url, "Erro ao buscar Tags do processo");
        Tag[] tagsRemotoArray = restTemplate.exchange(request, Tag[].class).getBody();
        List<Tag> tagsRemoto = tagsRemotoArray != null ? Arrays.asList(tagsRemotoArray) : Arrays.asList();

        return tagsRemoto;
    }

    public String devolveTagPje(TagProcessoDTO requisicao, FonteDadosEnum fonte) throws Exception {

        String returnTrue = null;
        try {
            ResponseEntity<String> response =  post(getUrlBase(fonte) + "/tag-processo",requisicao,String.class,null);
            if (response.getStatusCode() == HttpStatus.OK)
                returnTrue = response.getBody();

            if(returnTrue == null || returnTrue.isEmpty()) {
                String mess = "Não foi possível obter o id legado para a tag: " + requisicao.getIdProcesso();
                throw new Exception(mess);
            }
            return returnTrue;
        } catch (HttpServerErrorException eHttp) {
            List<String> list = eHttp.getResponseHeaders().get("error");
            if (list != null && list.size() > 0){
                throw new Exception(list.get(0),eHttp);
            }
            String mess = "Erro generico ao enviar a origem. ID_TAG: " + requisicao.getIdProcesso();
            throw new Exception(mess,eHttp);
        }catch (ResourceAccessException ra){
            throw new Exception("Erro na comunicação com o servidor de origem.",ra);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    private String getUrlBase(FonteDadosEnum fonte) throws Exception {
        return getFonte(fonte).getUrlBase() + "/tag";
    }

    private FonteDados getFonte(FonteDadosEnum fonte) throws Exception {
        return fonte.selecionaFonte(fontes);
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
