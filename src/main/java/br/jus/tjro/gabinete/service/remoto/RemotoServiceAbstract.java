package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.infrastructure.converter.PageableConverter;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.ModeloMinuta;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Component
public abstract class RemotoServiceAbstract {

    @Autowired
    private AuthenticationUsuarioService auth;

    protected abstract RestTemplate getRestTemplate();

    private final Logger looger = LoggerFactory.getLogger(RemotoServiceAbstract.class);


    protected RequestEntity<Void> get(String URL) throws ServicoRemotoException {
        return this.get(URL, "Erro generico ao buscar recurso remoto");
    }

    protected RequestEntity<Void> get(String URL, String msgError) throws ServicoRemotoException {
        try {
            return RequestEntity.get(URI.create(URL)).headers(addHeaders()).build();
        } catch (HttpServerErrorException | HttpClientErrorException eHttp) {
            throw tratamentoErro(eHttp,msgError);
        } catch (Exception e) {
            looger.error(e.getMessage(), e);
            throw new ServicoRemotoException(msgError, e);
        }
    }

    protected <T> ResponseEntity<T> get(String URL, Class<T> responseType) throws ServicoRemotoException {
        return this.get(URL,responseType, "Erro ao buscar recurso remoto");
    }

    protected <T> ResponseEntity<T> get(String URL, Class<T> responseType,String msgError) throws ServicoRemotoException {
        RequestEntity<Void> httpEntity = this.get(URL, msgError);
        try{
            return getRestTemplate().exchange(httpEntity, responseType);
        } catch (HttpServerErrorException | HttpClientErrorException eHttp) {
            throw tratamentoErro(eHttp,msgError);
        } catch (Exception e) {
            throw new ServicoRemotoException(msgError, e);
        }
    }

    protected <T> ResponseEntity<T> post(String url, Object request, Class<T> responseType) throws ServicoRemotoException {
        return post(url, request, responseType, "Erro ao buscar recurso remoto");
    }

    protected <T> ResponseEntity<T> post(String url, Object request, ParameterizedTypeReference<T> responseType) throws ServicoRemotoException {
        return post(url,request,responseType,null);
    }

    protected <T> ResponseEntity<T> post(String url, Object request, ParameterizedTypeReference<T> responseType, Pageable pageable) throws ServicoRemotoException {
        try{
            RequestEntity<Object> entity = RequestEntity.post(URI.create(url+"/?"+new PageableConverter().toQueryString(pageable)))
                .contentType(MediaType.APPLICATION_JSON_UTF8).headers(addHeaders()).body(request);
            return getRestTemplate().exchange(entity, responseType);
        } catch (HttpServerErrorException | HttpClientErrorException eHttp) {
            throw tratamentoErro(eHttp,"Erro ao buscar recurso remoto");
        } catch (Exception e) {
            throw new ServicoRemotoException("Erro ao buscar recurso remoto", e);
        }
    }


    protected <T> ResponseEntity<T> post(String url, Object request, Class<T> responseType, String msg, MediaType midiaType) throws ServicoRemotoException {
        try {
            RequestEntity<Object> entity = RequestEntity.post(URI.create(url))
                .contentType(midiaType).headers(addHeaders()).body(request);
            return getRestTemplate().exchange(entity, responseType);
        } catch (HttpServerErrorException | HttpClientErrorException eHttp) {
            throw tratamentoErro(eHttp,"Erro ao buscar recurso remoto");
        } catch (Exception e) {
            throw new ServicoRemotoException(msg, e);
        }
    }

    protected <T> ResponseEntity<T> post(String url, Object request, Class<T> responseType, String msg) throws ServicoRemotoException {
        return post(url,request,responseType,msg,MediaType.APPLICATION_JSON);
    }

    protected <T> ResponseEntity<T> exchange(String url, HttpMethod httpMethod, HttpEntity httpEntity, Class<T> responseType, String msg) throws ServicoRemotoException {
        return this.exchange(url, httpMethod, httpEntity, responseType, msg, false);
    }

    protected <T> ResponseEntity<T> exchange(String url, HttpMethod httpMethod, HttpEntity httpEntity, Class<T> responseType, String msg, boolean addHeader) throws ServicoRemotoException {
        try {
            HttpEntity updatedHttpEntity = null;
            if(addHeader){
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", returnToken());
                updatedHttpEntity = new HttpEntity<>(httpEntity.getBody(), headers);
            }else{
                updatedHttpEntity = httpEntity;
            }

            return getRestTemplate().exchange(url, httpMethod, updatedHttpEntity, responseType);
        } catch (HttpServerErrorException | HttpClientErrorException eHttp) {
            throw tratamentoErro(eHttp, "Erro ao buscar recurso remoto");
        } catch (Exception e) {
            throw new ServicoRemotoException(msg, e);
        }

    }

    private ServicoRemotoException tratamentoErro(HttpStatusCodeException eHttp, String msgError){
        List<String> list = eHttp.getResponseHeaders().get("error");
        looger.error(eHttp.getMessage(), eHttp);
        if (list != null && list.size() > 0)
            return new ServicoRemotoException("Erro ao buscar recurso remoto, o servidor diz: " + list.get(0), eHttp);
        else if(!eHttp.getResponseBodyAsString().equals(""))
                return new ServicoRemotoException("Erro ao buscar recurso remoto, o servidor diz: " + eHttp.getResponseBodyAsString(), eHttp);
        return new ServicoRemotoException(msgError, eHttp);
    }

    protected <T> ResponseEntity<T> put(String url, Object request, Class<T> responseType, String msg) throws ServicoRemotoException {
        try {
            RequestEntity<Object> entity = RequestEntity.put(URI.create(url))
                .contentType(MediaType.APPLICATION_JSON_UTF8).headers(addHeaders()).body(request);
            return getRestTemplate().exchange(entity, responseType);
        } catch (HttpServerErrorException | HttpClientErrorException eHttp) {
            throw tratamentoErro(eHttp,"Erro ao buscar recurso remoto");
        } catch (Exception e) {
            throw new ServicoRemotoException(msg, e);
        }
    }

    private HttpHeaders addHeaders(){
        HttpHeaders headers = new HttpHeaders();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            headers.add("Authorization", auth.getUsuario(authentication,false).getToken());
        }
        return headers;
    }

    private String returnToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return auth.getUsuario(authentication,false).getToken();
        }
        return "";
    }
}
