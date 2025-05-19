package br.jus.tjro.gabinete.repository.gab.processo.helper;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.repository.gab.filter.ProcessoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ProcessosRepositoryQueries {

    Page<Processo> filtrarProcessosComPaginacaoEOrgaoJulgador(ProcessoFilter processoFilter, Pageable pageable, int idCaixa,
                                                              List<TarefaEnum> tarefas, List<String> idOJ);

    Page<Processo> filtrarProcessosComPaginacaoEOrgaoJulgadorDoLocalizadorV3(ProcessoFilter processoFilter, Pageable pageable, List<Long> idProcessos, List<TarefaEnum> tarefas, List<String> idOJ);

    Processo buscaOsProcessosQueDevemSincronizar(Date oldDate);

    Page<Processo> findByNumeroProcesso(@Param("numeroProcesso") String numeroProcesso, Usuario usuario, Pageable page);

    Page<Processo> findComMinutasRecebidas(ProcessoFilter processoFilter, List<String> idOJ, Pageable page);

    Page<Processo> getProcessosPorOJETarefaRevisarPaginada(ProcessoFilter processoFilter, Pageable pageable, List<OrgaoJulgador> revisadosPor, TarefaEnum revisar);
}
