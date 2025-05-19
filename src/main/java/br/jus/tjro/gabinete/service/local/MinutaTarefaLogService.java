package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaTarefaLog;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.minuta.MinutaTarefaLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MinutaTarefaLogService {

    @Autowired
    private MinutaTarefaLogRepository minutaTarefaLogRepository;

    public MinutaTarefaLog save(MinutaTarefaLog minutaTarefaLog) throws Exception {
        return minutaTarefaLogRepository.save(minutaTarefaLog);
    }

    public void registrarLog(Processo processo, Usuario usuario) throws Exception {
        Optional<Minuta> minuta = processo.minutaEmElaboracao();
        if(minuta.isPresent()){
            MinutaTarefaLog log = new MinutaTarefaLog(minuta.get(),usuario, processo.getTarefa());
            minutaTarefaLogRepository.save(log);
        }
    }

    public List<MinutaTarefaLog> getPorIdMinutaAndTarefaEnum(Long id, TarefaEnum paraIntegracao) {
        return minutaTarefaLogRepository.findByMinutaIdAndTarefaEnumOrderByDataLogDesc(id, paraIntegracao);
    }
}
