package br.jus.tjro.gabinete.repository.webjud;

import br.jus.tjro.gabinete.model.gab.Papel;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.security.AuthenticationParseUsuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;

@Repository
@ConditionalOnProperty(name = "mock.julgador", havingValue = "false", matchIfMissing = true)
public class OrgaosJulgadoresRepositoryImp implements OrgaosJulgadoresRepository {

    private final RestTemplate restTemplate;
    private final String host;

    private final Logger looger = LoggerFactory.getLogger(OrgaosJulgadoresRepositoryImp.class);

    @Autowired
    public OrgaosJulgadoresRepositoryImp(RestTemplate restTemplate, @Value("${lotacoes.url:http://localhost:8080}") String host) {
        this.restTemplate = restTemplate;
        this.host = host;
    }

    @Override
    @Cacheable(value = "orgao-julgador", unless = "#result == null && #result.empty()")
    public Optional<OrgaoJulgador> findById(String id) {
        looger.info("Buscando Orgao julgador por id no servidor remoto! " + id);
        RequestEntity<Void> request = RequestEntity.get(URI.create(host + "/" + id)).header("Authorization", getTokenUserLogado()).build();
        return Optional.ofNullable(restTemplate.exchange(request, OrgaoJulgador.class).getBody());
    }

    @Override
    @Cacheable(value = "usuario-orgao-julgador", unless = "#result == null", key = "#usuario.getId()")
    public List<OrgaoJulgador> findAllByUsuario(Usuario usuario) {
        looger.info("Buscando Orgao julgador por usuario no servidor remoto em: " + host + "/cpf/" + usuario.getCpf());
        RequestEntity<Void> request = RequestEntity.get(URI.create(host + "/cpf/" + usuario.getCpf()))
            .header("Authorization", usuario.getToken())
            .build();
        OrgaoJulgador[] oj = restTemplate.exchange(request, OrgaoJulgador[].class).getBody();
        return oj == null ? new ArrayList() : Arrays.asList(oj);
    }

    @Override
    @Cacheable(value = "usuario-papel", unless = "#result == null", key = "#usuario.getCpf()")
    public Map<String, List<Papel>> findAllPapelByUsuario(Usuario usuario) {
        looger.info("Buscando Orgao julgador por usuario no servidor remoto em: " + host + "/cpf-papel/" + usuario.getCpf());
        RequestEntity<Void> request = RequestEntity.get(URI.create(host + "/cpf-papel/" + usuario.getCpf())).header("Authorization", usuario.getToken()).build();
        ParameterizedTypeReference<Map<String, List<Papel>>> type = new ParameterizedTypeReference<>() {
        };
        Map<String, List<Papel>> papeis = restTemplate.exchange(request, type).getBody();
        return papeis == null ? new HashMap<String, List<Papel>>() : papeis;
    }

    @Override
    public List<OrgaoJulgador> findAllByIdColegiado(String id) {
        looger.info("Buscando Orgao julgador colegiado por id no servidor remoto! " + id);
        RequestEntity<Void> request = RequestEntity.get(URI.create(host + "/composicao/colegiado/" + id + "/orgao")).build();
        OrgaoJulgador[] oj = restTemplate.exchange(request, OrgaoJulgador[].class).getBody();
        return oj == null ? new ArrayList() : Arrays.asList(oj);
    }

    @Override
    @Cacheable(value = "orgaos-julgadores-revisor", unless = "#result == null", key = "#orgaoJulgador.getId()")
    public List<OrgaoJulgador> findSouRevisadoPor(OrgaoJulgador orgaoJulgador) {
        looger.info("Buscando Orgaos julgadores revisores do oj informado no servidor remoto em: " + host + "/orgao-julgador/" + orgaoJulgador.getId() + "/me-revisam");
        RequestEntity<Void> request = RequestEntity.get(URI.create(host + "/orgao-julgador/" + orgaoJulgador.getId() + "/me-revisam")).header("Authorization", getTokenUserLogado()).build();
        ParameterizedTypeReference<List<OrgaoJulgador>> type = new ParameterizedTypeReference<>() {
        };
        List<OrgaoJulgador> ojRevisor = restTemplate.exchange(request, type).getBody();
        return ojRevisor == null ? new ArrayList<>() : ojRevisor;
    }

    @Override
    @Cacheable(value = "orgaos-julgadores-revisados", unless = "#result == null", key = "#orgaoJulgador.getId()")
    public List<OrgaoJulgador> findQueSouRevisor(OrgaoJulgador orgaoJulgador) {
        looger.info("Buscando orgãos julgadores que são revisados pelo oj informado no servidor remoto em: " + host + "/orgao-julgador/" + orgaoJulgador.getId() + "/eu-reviso");
        RequestEntity<Void> request = RequestEntity.get(URI.create(host + "/orgao-julgador/" + orgaoJulgador.getId() + "/eu-reviso")).header("Authorization", getTokenUserLogado()).build();
        ParameterizedTypeReference<List<OrgaoJulgador>> type = new ParameterizedTypeReference<>() {
        };
        List<OrgaoJulgador> oJsRevisados = restTemplate.exchange(request, type).getBody();
        return oJsRevisados == null ? new ArrayList<>() : oJsRevisados;
    }


    @Override
    @CacheEvict(value = "usuario-orgao-julgador", key = "#usuario.getId()")
    public void evictOrgaoJulgadoresByUsuario(Usuario usuario) {
        RequestEntity<Void> request = RequestEntity.get(URI.create(host + "/cache/" + usuario.getCpf()))
            .header("Authorization", usuario.getToken()).build();
        restTemplate.exchange(request, String.class);
    }

    @Override
    @CacheEvict(value = "usuario-papel", key = "#usuario.getCpf()")
    public void evictPapeisByUsuario(Usuario usuario) {
        RequestEntity<Void> request = RequestEntity.get(URI.create(host + "/cache/" + usuario.getCpf()))
            .header("Authorization", usuario.getToken()).build();
        restTemplate.exchange(request, String.class);
    }

    @Override
    @CacheEvict(value = "orgaos-julgadores-revisor", key = "#orgaoJulgador.getId()")
    public void evictOrgaosJulgadoresRevisor(Usuario usuario, OrgaoJulgador orgaoJulgador) {
        RequestEntity<Void> request = RequestEntity.get(URI.create(host + "/cache/" + orgaoJulgador.getId()))
            .header("Authorization", usuario.getToken()).build();
        restTemplate.exchange(request, String.class);
    }

    @Override
    @Cacheable(value = "usuario-orgao-julgador", unless = "#result == null", key = "#usuario.getId()")
    public void atualizaOrgaoJulgadorByUsuario(Usuario usuario) {
        RequestEntity<Void> request = RequestEntity.get(URI.create(host + "/update/" + usuario.getCpf()))
            .header("Authorization", usuario.getToken()).build();
        restTemplate.exchange(request, String.class);
    }

    private String getTokenUserLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Usuario usuario = AuthenticationParseUsuario.parse(authentication);
            return usuario.getToken();
        }
        return "";
    }
}
