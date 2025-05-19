package br.jus.tjro.gabinete.repository.gab.minuta;

import br.jus.tjro.gabinete.model.gab.minuta.Anotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnotacaoRepository extends JpaRepository<Anotacao, Long> {

    @Query("SELECT p "
        + "FROM Anotacao p "
        + "WHERE p.idProcesso = :idProcesso "
        + "AND p.cpfUsuario = :cpfUsuario "
        + "AND p.publicidade = 'Privado' "
        + "ORDER BY p.dataCadastro DESC ")
    List<Anotacao> obtemListaAnotacoesPrivadasDoUsuario(@Param("idProcesso") Long idProcesso, @Param("cpfUsuario") String cpfUsuario);

    @Query("SELECT p "
        + "FROM Anotacao p "
        + "WHERE p.idProcesso = :idProcesso "
        + "AND p.publicidade = 'Público' "
        + "ORDER BY p.dataCadastro DESC ")
    List<Anotacao> obtemListaAnotacoesPublicasDoProcesso(@Param("idProcesso") Long idProcesso);
}
