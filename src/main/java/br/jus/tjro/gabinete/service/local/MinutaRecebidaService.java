package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaRecebida;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.minuta.MinutasRecebidasRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class MinutaRecebidaService {
    private final Logger looger = LoggerFactory.getLogger(MinutaRecebidaService.class);

    private final MinutaService minutaService;
    private final MinutasRecebidasRepository minutasRecebidasRepository;
    private final ProcessosRepository processosRepository;
    private final MinutaTarefaLogService minutaTarefaLogService;

    MinutaRecebidaService(MinutaService minutaService, MinutasRecebidasRepository minutasRecebidasRepository, ProcessosRepository processosRepository, MinutaTarefaLogService minutaTarefaLogService) {
        this.minutaService = minutaService;
        this.minutasRecebidasRepository = minutasRecebidasRepository;
        this.processosRepository = processosRepository;
        this.minutaTarefaLogService = minutaTarefaLogService;
    }

    public MinutaRecebida aprovar(MinutaRecebida minutaRecebida, Usuario usuario) throws Exception {
        try {
            looger.info("aprovando minutaRecebida " + minutaRecebida.getId());
            Processo processo = minutaRecebida.getProcesso();
            processo.minutaEmElaboracao().ifPresent(it -> {
                it.descartar();
                this.minutaService.save(it);
            });

            processo.setTarefa(TarefaEnum.Minutar);
            minutaTarefaLogService.registrarLog(processo, usuario);

            this.processosRepository.save(processo);

            Minuta novaMinuta = new Minuta(processo);
            novaMinuta.setMinutaHtml(minutaRecebida.getHtml());
            novaMinuta.defineAutoria(usuario);
            this.minutaService.save(novaMinuta);
            this.minutaService.adicionaMovimento(novaMinuta.getId(), minutaRecebida.getIdMovimentoProcessual());
            this.minutaService.salvarComVersao(novaMinuta, usuario);

            minutaRecebida.setProcesso(processo);
            minutaRecebida.finalizada();
            return this.minutasRecebidasRepository.save(minutaRecebida);
        } catch (Exception e) {
            looger.error("erro ao salvar aprovar a minutaRecebida: " + minutaRecebida.getId());
            throw e;
        }

    }
}
