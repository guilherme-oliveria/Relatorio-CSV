package br.jus.tjro.gabinete.service.remoto.tpu;

import br.jus.tjro.gabinete.model.gab.tpu.Tpu;
import br.jus.tjro.gabinete.model.gab.tpu.TpuComplemento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TpuRemotoServiceAbstract {

    private final RestTemplate restTemplate;


    @Autowired
    public TpuRemotoServiceAbstract(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    protected <T extends Tpu> List<T> findByCodigos(List<Long> codigos, String URL) throws Exception {
        RequestEntity<Void> request;
        codigos = removeNullos(codigos);
        String ids = String.join(",", codigos.stream().map(Object::toString).collect(Collectors.toList()));

        ids = URLEncoder.encode("[" + ids + "]");

        try {
            request = RequestEntity.get(URI.create(URL + "/" + ids)).build();
        } catch (ResourceAccessException e) {
            throw new ResourceAccessException("Não foi possível acessar o serviço da TPU");
        }

        ParameterizedTypeReference<List<T>> typeRef = new ParameterizedTypeReference<List<T>>() {
        };

        return restTemplate.exchange(request, typeRef).getBody();
    }

    protected <T extends Tpu> T findByCodigo(Long codigo, String URL) {
        try {
            RequestEntity<Void> request = RequestEntity.get(URI.create(URL + "/" + codigo)).build();
            return restTemplate.exchange(request, new ParameterizedTypeReference<T>() {
            }).getBody();
        } catch (ResourceAccessException e) {
            throw new ResourceAccessException("Não foi possível acessar o serviço da TPU");
        }
    }

    protected <T extends Tpu> List<T> findArvore(Long id, String URL) {
        String urlString = URL + "/arvore";
        RequestEntity<Void> request;
        if (id != null)
            urlString += "/" + id;
        try {
            request = RequestEntity
                .get(URI.create(urlString))
                .build();
        } catch (ResourceAccessException e) {
            throw new ResourceAccessException("Não foi possível acessar o serviço da TPU");
        }

        ParameterizedTypeReference<List<T>> typeRef = new ParameterizedTypeReference<List<T>>() {
        };
        List<T> items = restTemplate.exchange(
            request,
            typeRef)
            .getBody();

        return items;
    }

    protected List<TpuComplemento> getComplementos(Long codigoMovimento, String url) {
        try {
            RequestEntity<Void> request = RequestEntity
                .get(URI.create(url + "/movimentos/" + codigoMovimento.toString() + "/complementos"))
                .build();
            ParameterizedTypeReference<List<TpuComplemento>> typeRef = new ParameterizedTypeReference<List<TpuComplemento>>() {
            };

            return restTemplate.exchange(request, typeRef).getBody();
        } catch (ResourceAccessException e) {
            throw new ResourceAccessException("Não foi possível acessar o serviço da TPU");
        }
    }

    protected List<Long> removeNullos(List<Long> lista) {
        lista.removeAll(Collections.singleton(null));
        return lista;
    }

}
