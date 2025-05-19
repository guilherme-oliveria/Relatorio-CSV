package br.jus.tjro.gabinete.builders.service;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.transiente.MovimentoProcesso;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoMovimento;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoMovimentoRepository;
import br.jus.tjro.gabinete.service.local.ProcessoMovimentoService;
import br.jus.tjro.gabinete.service.remoto.MovimentoRemotoService;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import org.mockito.ArgumentMatchers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class BuilderProcessoMovimentoService {

    public ProcessoMovimentoService get() throws Exception {
        ProcessoMovimentoService service = new ProcessoMovimentoService(getProcessoMovimentoRepository(), getMovimentoRemotoService());
        return service;
    }

    private MovimentoRemotoService getMovimentoRemotoService() throws Exception {
        MovimentoRemotoService service = mock(MovimentoRemotoService.class);
        when(service.pegaMovimentoProcesso(any(),any())).thenAnswer(
            (Answer<List<MovimentoProcesso>>) invocation -> {
                Long id = invocation.getArgument(0);
                FonteDadosEnum fonte = invocation.getArgument(1);
                List<MovimentoProcesso> lista = new ArrayList<>();
                if(id == 6l && fonte != null){
                    MovimentoProcesso mov1 = new MovimentoProcesso();
                    mov1.setId(3l);
                    MovimentoProcesso mov2 = new MovimentoProcesso();
                    mov2.setId(4l);
                    lista.add(mov1);
                    lista.add(mov2);
                }

                return lista;
            });
        return service;
    }

    private ProcessoMovimentoRepository getProcessoMovimentoRepository() {
        ProcessoMovimentoRepository repository = mock(ProcessoMovimentoRepository.class);
        when(repository.saveAll(anyList())).thenAnswer(
            (Answer<List<ProcessoMovimento>>) invocation -> {
                List<ProcessoMovimento> lista;
                lista = invocation.getArgument(0);
                return lista;
            });
        when(repository.findByProcesso(any())).thenAnswer(
            (Answer<List<ProcessoMovimento>>) invocation -> {
                Processo processo = invocation.getArgument(0);
                List<ProcessoMovimento> lista = new ArrayList<>();
                if(processo.getId() == 2l && processo.getSistema() == FonteDadosEnum.PJEPG){
                    ProcessoMovimento mov1 = new ProcessoMovimento();
                    mov1.setIdProcessoMovimentoLegado(3l);
                    lista.add(mov1);
                }
                return lista;
            });
        return repository;
    }
}
