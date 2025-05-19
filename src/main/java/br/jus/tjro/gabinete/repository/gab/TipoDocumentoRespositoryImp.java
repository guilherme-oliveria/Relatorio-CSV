package br.jus.tjro.gabinete.repository.gab;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.service.remoto.RemotoServiceAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TipoDocumentoRespositoryImp extends RemotoServiceAbstract implements TipoDocumentoRepository   {

    private final RestTemplate restTemplate;
    private final String host;
    private final Logger looger = LoggerFactory.getLogger(TipoDocumentoRespositoryImp.class);

    @Autowired
    public TipoDocumentoRespositoryImp(RestTemplate restTemplate,@Value("${URL_PJE_PG_REST:''}") String host){
        this.host = host+"/tipo-documento-tarefa/";
        this.restTemplate = restTemplate;
    }

    @Override
    @Cacheable(value = "tiposDocumentos", unless = "#result == null or #result.isEmpty()")
    public List<TipoDocumento> findAll() {
        RequestEntity<Void> request =null;
        try{
            request = get(host,"Erro ao buscar tipo documento");
        }catch (ServicoRemotoException e){
            e.printStackTrace();
        }
        TipoDocumento[] tipoDocumentos = getRestTemplate().exchange(request, TipoDocumento[].class).getBody();
        return tipoDocumentos == null ? new ArrayList() : Arrays.asList(tipoDocumentos);
    }

    @Override
    @Cacheable(value = "tiposDocumentosIds", key = "#ids", unless = "#result == null or #result.isEmpty()")
    public List<TipoDocumento> findAll(String ids) throws ServicoRemotoException {
        String[] idsArray = ids.split(",");
        String encodedIds = Arrays.stream(idsArray)
            .map(id -> URLEncoder.encode(id.trim(), StandardCharsets.UTF_8))
            .collect(Collectors.joining(","));

        String finalUrl = host + encodedIds;
        return Arrays.asList(Objects.requireNonNull(get(finalUrl, TipoDocumento[].class).getBody()));
    }


    @Override
    @Cacheable(value = "tipoDocumento",unless="#result == null")
    public Optional<TipoDocumento> findById(String id)  {
        return findAll().stream().filter(tpDoc -> tpDoc.getId().equals(id)).findFirst();
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
