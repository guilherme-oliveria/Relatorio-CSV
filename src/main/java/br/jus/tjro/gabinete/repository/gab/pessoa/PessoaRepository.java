package br.jus.tjro.gabinete.repository.gab.pessoa;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    List<Pessoa> findByNomeContains(String nome);

    Optional<Pessoa> findByIdPessoaLegadoAndSistema(Long idPessoaLegado, FonteDadosEnum sistema);

    List<Pessoa> findByIdPessoaLegadoInAndSistema(@Param("idPessoaLegado") List<Long> idsPessoasLegados, @Param("sistema") FonteDadosEnum sistema);

}
