package br.jus.tjro.gabinete.repository.gab.processo;

import br.jus.tjro.gabinete.dto.MovimentoProcessoDTO;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoMovimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface ProcessoMovimentoRepository extends JpaRepository<ProcessoMovimento, Long> {

    List<ProcessoMovimento> findByProcesso(Processo processo);

    @Transactional(readOnly = true)
    @Query("SELECT new br.jus.tjro.gabinete.dto.MovimentoProcessoDTO("
        + "pm.id,pm.idProcessoMovimentoLegado,pm.descricao,pm.data_atualizacao,pm.cpf_usuario,pm.nome_usuario,pm.movimento,pm.processoDocumento.id ) "
        + "FROM ProcessoMovimento pm "
        + "where pm.processo.id = :idProcesso " +
          "order by pm.data_atualizacao desc")
    Page<MovimentoProcessoDTO> pegaMovimentosDoProcessoPeloIdProcesso(@Param("idProcesso") Long idProcesso, Pageable pageable);
}
