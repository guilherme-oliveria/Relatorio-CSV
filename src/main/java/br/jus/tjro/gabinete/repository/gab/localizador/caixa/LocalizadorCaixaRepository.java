package br.jus.tjro.gabinete.repository.gab.localizador.caixa;

import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorCaixa;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorOrgaoJulgador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LocalizadorCaixaRepository extends JpaRepository<LocalizadorCaixa, Long> {

    @Query(value = "select distinct lc from LocalizadorCaixa lc join lc.usuarios us join lc.orgaosJulgadores oj where (us.id = :usuario or (lc.tipo='Sistema' or lc.tipo='OrgaoJulgador')) AND (oj.id= :orgao or lc.tipo='Sistema') order by lc.nome")
    List<LocalizadorCaixa> findByUsuario(@Param("usuario") String usuario, @Param("orgao") String orgao);

    @Query(value = "select distinct lc "
        + "from LocalizadorCaixa lc "
        + "join lc.orgaosJulgadores oj "
        + "where oj.id = :orgao")
    List<LocalizadorCaixa> findByOrgaosJulgadores(@Param("orgao") Long orgaoJulgador);

    LocalizadorCaixa findTop1ByNomeContainingIgnoreCaseAndOrgaosJulgadoresContaining(String nome,
                                                                             LocalizadorOrgaoJulgador orgaoJulgador);
}
