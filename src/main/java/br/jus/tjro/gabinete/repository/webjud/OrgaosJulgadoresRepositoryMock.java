package br.jus.tjro.gabinete.repository.webjud;

import br.jus.tjro.gabinete.model.gab.Papel;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "mock.julgador", havingValue = "true", matchIfMissing = false)
public class OrgaosJulgadoresRepositoryMock implements OrgaosJulgadoresRepository {


    private final Logger looger = LoggerFactory.getLogger(OrgaosJulgadoresRepositoryMock.class);

    private final OrgaoJulgador orgao = new OrgaoJulgador("PJEPG-1", "Julgador mock", List.of(), "WIND", List.of());

    @Override
    public Optional<OrgaoJulgador> findById(String id) {
        return Optional.of(orgao);
    }

    @Override
    @Cacheable(value = "usuario-orgao-julgador", unless = "#result == null", key = "#usuario.getId()")
    public List<OrgaoJulgador> findAllByUsuario(Usuario usuario) {
        return List.of(orgao);
    }

    @Override
    @Cacheable(value = "usuario-papel", unless = "#result == null", key = "#usuario.getCpf()")
    public Map<String, List<Papel>> findAllPapelByUsuario(Usuario usuario) {
        Papel papel = new Papel("Administrador", List.of(orgao, new OrgaoJulgador("PJEPG-8", "Julgador PJEPG-8 mock", List.of(), "WIND2", List.of())));
        Papel papel2 = new Papel("Assessor", List.of(orgao));
        Papel papel3 = new Papel("Magistrado", List.of(new OrgaoJulgador("PJESG-2", "Julgador3 mock", List.of(), "WIND2", List.of())));
        Map<String, List<Papel>> mapPapeis = new HashMap<String, List<Papel>>();
        mapPapeis.put("PJEPG", List.of(papel, papel2));
        mapPapeis.put("PJESG", List.of(papel3));
        return mapPapeis;
    }

    @Override
    public List<OrgaoJulgador> findAllByIdColegiado(String id) {
        return Collections.emptyList();
    }

    @Override
    @Cacheable(value = "orgaos-julgadores-revisor", unless = "#result == null", key = "#orgaoJulgador.getId()")
    public List<OrgaoJulgador> findSouRevisadoPor(OrgaoJulgador orgaoJulgador) {
        return List.of(new OrgaoJulgador("PJESG-3", "Julgador 3 Revisor mock", List.of(), "WIND2", List.of()));
    }

    @Override
//    @Cacheable(value = "orgaos-julgadores-revisados", unless = "#result == null", key = "#orgaoJulgador.getId()")
    public List<OrgaoJulgador> findQueSouRevisor(OrgaoJulgador orgaoJulgador) {
        return List.of(new OrgaoJulgador("PJESG-2", "Julgador 2 Revisado mock", List.of(), "WIND2", List.of()));
    }

    @Override
    public void evictOrgaoJulgadoresByUsuario(Usuario usuario) {

    }

    @Override
    public void evictPapeisByUsuario(Usuario usuario) {

    }

    @Override
    public void evictOrgaosJulgadoresRevisor(Usuario usuario, OrgaoJulgador orgaoJulgador) {

    }

    @Override
    public void atualizaOrgaoJulgadorByUsuario(Usuario usuario) {

    }
}
