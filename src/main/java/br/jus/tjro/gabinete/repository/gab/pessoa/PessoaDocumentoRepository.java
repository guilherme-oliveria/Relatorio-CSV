package br.jus.tjro.gabinete.repository.gab.pessoa;

import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.pessoa.PessoaDocumento;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PessoaDocumentoRepository extends JpaRepository<PessoaDocumento, Long> {

    List<PessoaDocumento> findByPessoa(Pessoa pessoa);

    List<PessoaDocumento> findByPessoaAndTipoDocumento(Pessoa pessoa, String documento);

    List<PessoaDocumento> findByIdPessoaLegadoAndDocumentoAndTipoDocumento(
        Long idPessoaLegado, String documento, String tipoDocumento);

    @Query("SELECT DISTINCT pd FROM PessoaDocumento pd " +
        "WHERE pd.pessoa IN (" +
        "    SELECT pp.pessoa FROM ProcessoParte pp " +
        "    WHERE pp.processo = :processo " +
        "    AND pp.tipoParte = 'ADVOGADO'" +
        ") " +
        "AND pd.tipoDocumento = 'OAB'")
    List<PessoaDocumento> findAdvogadoDocumentosPorProcesso(Processo processo);

}
