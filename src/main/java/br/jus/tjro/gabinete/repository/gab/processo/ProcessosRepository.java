package br.jus.tjro.gabinete.repository.gab.processo;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.repository.gab.processo.helper.ProcessosRepositoryQueries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessosRepository extends CrudRepository<Processo, Long>, ProcessosRepositoryQueries {

    @Query("SELECT p FROM Processo p LEFT JOIN FETCH p.tags" +
        " LEFT JOIN FETCH p.minutas minu" +
        " LEFT JOIN FETCH minu.assinatura assina " +
        " LEFT JOIN FETCH assina.cadeiaCertificado cad WHERE p.id = :id")
    Optional<Processo> findOptionalById(@Param("id") Long id);

    Processo getById(Long id);

    List<Processo> findByTarefaEnum(TarefaEnum tarefa);

    List<Processo> findByTarefaEnumIn(Collection<TarefaEnum> tarefa);

    Page<Processo> findByTarefaEnumIn(Collection<TarefaEnum> tarefa, Pageable pageable);

    @Query("SELECT proc FROM Processo proc where proc.id in (:processos)")
    List<Processo> findByIds(@Param("processos") List<Long> idProcessos);

    @PostAuthorize("validaProcessosAcessados(returnObject)")
    List<Processo> findByOrgaoJulgadorObjAndTarefaEnum(OrgaoJulgador id, TarefaEnum tarefaEnum);

    @Transactional
    @Modifying
    @Query("update Processo p set p.motivoSegredoJustica = :motivo, p.segredoJustica = true where p.id = :id")
    int atualizaSegredoJustica(@Param("motivo") String motivoSegredoJustica, @Param("id") Long idProcesso);

    @Transactional
    @Modifying
    @Query("update Processo p set p.motivoSegredoJustica = NULL, p.segredoJustica = false where p.id = :id")
    int removeSegredoJustica(@Param("id") Long idProcesso);

    Optional<Processo> findByIdProcessoSistemaLegadoAndSistema(long idProcessoLegado, FonteDadosEnum fonteDeDados);

    Optional<Processo> findByNumeroProcessoAndSistema(String numeroProcesso, FonteDadosEnum fonteDadosEnum);

    @Query("SELECT proc FROM Processo proc where proc.numeroProcesso = :processo")
    Processo findByNumeroProcesso(@Param("processo") String numeroProcesso);

    @Query(value = "select orgao_julgador_str from processo group by orgao_julgador_str",nativeQuery = true)
    List<String> allOrgaoJulgador();

    Processo findTop1ByTarefaEnumAndOrgaoJulgadorObj(TarefaEnum tarefa, OrgaoJulgador orgaoJulgador);

    @Query("SELECT distinct p FROM Processo p " +
        "JOIN p.processoPartes pp " +
        "JOIN pp.pessoa pess " +
        "JOIN pess.pessoaDocumentos pd " +
        "JOIN pd.estado est " +
        "WHERE  UPPER(est.uf) = UPPER(:siglaEstado)  " +
        "AND pd.tipoDocumento = 'OAB' AND pp.tipoParte = 'ADVOGADO' "  +
        "AND CAST(REGEXP_REPLACE(pd.documento, '[^0-9]', '', 'g') AS integer) = :numeroOab")
    List<Processo> findProcessoByOabAndEstadoAndNumeroDocumento(@Param("numeroOab") int numeroOab,
                                                          @Param("siglaEstado") String siglaEstado);

}
