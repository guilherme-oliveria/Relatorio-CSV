package br.jus.tjro.gabinete.repository.gab.localizador;

import br.jus.tjro.gabinete.model.gab.localizador.Filtro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface FiltroRepository extends JpaRepository<Filtro, Long> {


    @Query(value = "select ft from Filtro ft join ft.usuarios us join ft.orgaosJulgadores oj where us.id = :usuario AND oj.id= :orgao and ft.parametro <> null order by ft.nome")
    List<Filtro> findByUsuario(@Param("usuario") String usuario, @Param("orgao") String orgao);

    @Query(value = "select ft from Filtro ft join ft.usuarios us join ft.orgaosJulgadores oj where us.id = :usuario AND oj.id = :orgao AND ft.nome = :nome AND ft.id <> :id and ft.parametro <> null")
    List<Filtro> findByNomeAndUsuarioAndOrgaosJulgadores(@Param("nome") String nome, @Param("usuario") String usuario, @Param("orgao") String orgao, @Param("id") Long id);

    @Query(value = "select ft.* from FILTRO ft" +
        "  join FILTRO_LOC_USUARIO usl ON ft.ID = usl.FILTRO_ID" +
        "  JOIN LOC_USUARIO us on us.ID = usl.USUARIOS_ID" +
        "  join FILTRO_LOC_ORGAO_JULGADOR ojl on ft.ID = ojl.FILTRO_ID" +
        "  JOIN LOC_ORGAO_JULGADOR oj on ojl.ORGAOSJULGADORES_ID_STR = oj.ID where us.ID = :usuario AND oj.ID= :orgao AND dbms_lob.compare(ft.parametro, :parametro) = 0 AND ft.id <> :id", nativeQuery = true)
    List<Filtro> findByParametroUsuario(@Param("parametro") String parametro, @Param("usuario") String usuario, @Param("id") Long id, @Param("orgao") String orgao);

}
