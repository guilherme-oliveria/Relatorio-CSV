package br.jus.tjro.gabinete.repository.gab.processo;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.model.gab.enums.TipoPolo;
import br.jus.tjro.gabinete.model.gab.transiente.Partes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface ProcessoParteRepository extends PagingAndSortingRepository<ProcessoParte, Long>, JpaRepository<ProcessoParte,Long> {

    List<ProcessoParte> findByProcesso(Processo processo);

    List<ProcessoParte> findByPessoaAndProcessoTarefaEnumIn(Pessoa pessoa, List<TarefaEnum> tarefaEnum);

    @Transactional(readOnly = true)
    @Query(value = ""
        + "SELECT "
        + "new br.jus.tjro.gabinete.model.gab.transiente.Partes("
        + "pp.id, pp.pessoa.nome, pp.tipoPolo, pp.tipoParte,pp.procuradoria, COALESCE(cpf.tipoDocumento, oab.tipoDocumento,''), COALESCE(cpf.documento, oab.documento,'')) "
        + "FROM ProcessoParte pp "
        + "JOIN pp.pessoa p "
        + "LEFT JOIN p.pessoaDocumentos cpf ON cpf.tipoDocumento = 'CPF' "
        + "LEFT JOIN p.pessoaDocumentos oab ON oab.tipoDocumento = 'OAB' "
        + "JOIN pp.processo pro "
        + "WHERE "
        + "pp.processo.id = :idProcesso "
        + "AND "
        + "pp.tipoPolo = :tipoPolo" )
    List<Partes> buscaParteProcessoPorIdProcessoEPolo(@Param("tipoPolo") TipoPolo tipoPolo, @Param("idProcesso") Long idProcesso);
}
