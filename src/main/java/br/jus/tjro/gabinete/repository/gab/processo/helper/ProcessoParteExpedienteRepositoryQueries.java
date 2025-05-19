package br.jus.tjro.gabinete.repository.gab.processo.helper;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParteExpediente;
import br.jus.tjro.gabinete.repository.gab.filter.ProcessoParteExpedienteFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProcessoParteExpedienteRepositoryQueries {

    Page<ProcessoParteExpediente> filtraProcessoParteExpediente(Processo processo, ProcessoParteExpedienteFilter processoParteExpedienteFilter, Pageable pageable, String query);
}
