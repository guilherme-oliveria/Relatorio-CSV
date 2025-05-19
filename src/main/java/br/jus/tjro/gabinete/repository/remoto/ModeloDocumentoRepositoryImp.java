package br.jus.tjro.gabinete.repository.remoto;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.infrastructure.converter.TipoDocumentoConverter;
import br.jus.tjro.gabinete.model.RestPageImpl;
import br.jus.tjro.gabinete.model.docs.ModeloDocumentoEnv;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.ModeloMinuta;
import br.jus.tjro.gabinete.model.gab.transiente.VariavelTemplate.VariavelTemplate;
import br.jus.tjro.gabinete.service.remoto.RemotoServiceAbstract;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Deprecated
public class ModeloDocumentoRepositoryImp extends RemotoServiceAbstract implements ModeloDocumentoRepository  {

    private final RestTemplate restTemplate;
    private final String host;
    private final TipoDocumentoConverter tipoDocumentoConverter;

    ModeloDocumentoRepositoryImp(ModeloDocumentoEnv docsEnv,
                                 RestTemplate restTemplate,
                                 TipoDocumentoConverter tipoDocumentoConverter){
        this.restTemplate = restTemplate;
        this.host = docsEnv.getBackUrl();
        this.tipoDocumentoConverter = tipoDocumentoConverter;
    }

    @Override
    public ModeloMinuta save(ModeloMinuta modeloMinuta, Usuario usuario) throws ServicoRemotoException {
        modeloMinuta.setUsuarioSaveUpdate(usuario);
        ModeloMinuta retorno = put(host, modeloMinuta, ModeloMinuta.class, "Erro ao registrar modelo de minuta").getBody();
        return preparaModelo(retorno,usuario);
    }

    @Override
    public Optional<ModeloMinuta> findById(String id,Usuario usuario) throws ServicoRemotoException {
        ModeloMinuta retorno = get(host + "/"+id,ModeloMinuta.class, usuario.getToken()).getBody();
        if(retorno == null)
            return Optional.empty();
        return Optional.of(preparaModelo(retorno,usuario));
    }

    public List<ModeloMinuta> findBy(QueryModeloDocumento query,Usuario usuario) throws ServicoRemotoException {
        List<ModeloMinuta> retorno = post(host + "/totais", query.getMap(), new ParameterizedTypeReference<List<ModeloMinuta>>() {
        }).getBody();
        return preparaModelo(retorno,usuario);
    }

    @Override
    public Map<String, Long> totais(List<QueryModeloDocumento> querys, Usuario usuario) throws ServicoRemotoException {
        return post(host + "/totais", querys.stream().map(QueryModeloDocumento::getMap).collect(toList()), new ParameterizedTypeReference<Map<String, Long>>() {}).getBody();
    }

    @Override
    public Page<ModeloMinuta> findBy(QueryModeloDocumento query, Pageable pageable, Usuario usuario) throws ServicoRemotoException {
        var retorno = post(host,query.getMap(),new ParameterizedTypeReference<RestPageImpl<ModeloMinuta>>() { },pageable).getBody();
        return preparaModelo(retorno,usuario);
    }

    @Override
    public void delete(String id, Usuario usuario) throws ServicoRemotoException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", usuario.getToken());
        HttpEntity<String> request = new HttpEntity<String>(headers);
        restTemplate.exchange(host+"/"+id+"/"+usuario.getCpf(), HttpMethod.DELETE, request,Void.class,"Erro ao tentar deletar");
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }

    private ModeloMinuta preparaModelo(ModeloMinuta modeloDocumento,Usuario usuario){
        modeloDocumento.setTipoDocumento(tipoDocumentoConverter.convertToEntityAttribute(modeloDocumento.getIdTipoDocumento()));
        if(usuario != null)
            modeloDocumento.isCanEditOrDelete(usuario);
        return modeloDocumento;
    }

    private List<ModeloMinuta> preparaModelo(List<ModeloMinuta> modelos,Usuario usuario){
        modelos.forEach(t -> preparaModelo(t,usuario));
        return modelos;
    }

    private Page<ModeloMinuta> preparaModelo(Page<ModeloMinuta> modelos, Usuario usuario){
        preparaModelo(modelos.getContent(),usuario);
        return modelos;
    }
}
