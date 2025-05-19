package br.jus.tjro.gabinete.repository.webjud;

import br.jus.tjro.gabinete.model.gab.Papel;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface OrgaosJulgadoresRepository {

    Optional<OrgaoJulgador> findById(String id);

    List<OrgaoJulgador> findAllByUsuario(Usuario usuario);

    Map<String, List<Papel>> findAllPapelByUsuario(Usuario usuario);

    List<OrgaoJulgador> findAllByIdColegiado(String id);

    void evictOrgaoJulgadoresByUsuario(Usuario usuario);

    void evictPapeisByUsuario(Usuario usuario);

    void evictOrgaosJulgadoresRevisor(Usuario usuario, OrgaoJulgador orgaoJulgador);

    void atualizaOrgaoJulgadorByUsuario(Usuario usuario);

    List<OrgaoJulgador> findSouRevisadoPor(OrgaoJulgador orgaoJulgador);

    List<OrgaoJulgador> findQueSouRevisor(OrgaoJulgador orgaoJulgador);
}
