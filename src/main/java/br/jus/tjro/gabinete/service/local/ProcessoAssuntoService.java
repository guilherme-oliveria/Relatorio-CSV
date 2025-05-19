package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoAssunto;
import br.jus.tjro.gabinete.model.gab.tpu.TpuAssunto;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoAssuntoRepository;
import br.jus.tjro.gabinete.service.local.tpu.TpuAssuntoService;
import br.jus.tjro.gabinete.service.remoto.AssuntoRemotoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ProcessoAssuntoService {

    private final Logger looger = LoggerFactory.getLogger(ProcessoAssuntoService.class);
    
    @Autowired
    private ProcessoAssuntoRepository repository;

    @Autowired
    private AssuntoRemotoService assuntoRemotoService;

    @Autowired
    private TpuAssuntoService tpuAssuntoService;

    public void save(ProcessoAssunto processoAssunto) {
        repository.save(processoAssunto);
    }

    protected ProcessoAssunto montaObjProcessoAssunto(Processo processo, Long idAssuntoRemoto, Boolean ehPrincipal) {
        return new ProcessoAssunto(processo, idAssuntoRemoto, ehPrincipal);
    }

    public void importaOuAtualizaAssuntosDoProcesso(Processo processo) throws Exception {
        Long idLegado = processo.getIdProcessoSistemaLegado();
        FonteDadosEnum fonte = processo.getSistema();

        List<Long> assuntosRemotosProcesso = assuntoRemotoService.getIdsAssuntosProcesso(idLegado, fonte);
        repository.deleteAllByProcessoIdAndIdAssuntoNotIn(processo, assuntosRemotosProcesso);

        List<ProcessoAssunto> assuntosAhImportar = new ArrayList<>();
        assuntosRemotosProcesso.stream()
            .forEach(assuntoRe -> assuntosAhImportar.add(
                montaObjProcessoAssunto(processo, assuntoRe, false))
            );
        Long assuntoPrincipal = Long.valueOf(0);
        try {
            assuntoPrincipal = Long.valueOf(
                assuntoRemotoService.buscaIdCnjDoAssuntoPrincipalDoProcessoNoWebService(
                    idLegado, processo.getSistema()
                )
            );
        } catch (Exception e) {
            looger.error(e.getMessage());
        }
        for (ProcessoAssunto processoAssunto : assuntosAhImportar) {
            try {
                ProcessoAssunto processoAssuntoLocal = repository.findByProcessoAndIdAssunto(processo, processoAssunto.getIdAssunto());
                if(processoAssuntoLocal != null) {
                    processoAssuntoLocal.setAssuntoPrincipal(processoAssunto.getAssuntoPrincipal());
                    processoAssunto = processoAssuntoLocal;
                }
                if (Objects.equals(processoAssunto.getIdAssunto(), assuntoPrincipal)) {
                    processoAssunto.setAssuntoPrincipal(true);
                }
                save(processoAssunto);
            } catch (Exception e) {
                looger.error(e.getMessage(),e);
            }
        }
            /*}

            // TODO implementar a adição de novos assuntos no PJE ou a exclusão no PJE
            // se a quantidade de assuntos remotos for menor, então foi adicionado pelo Gabinete
            // e é necessário adicionar no PJE
//            if (assuntosRemotosProcessoCount < assuntosLocaisDoProcessoCount) {
//                List<ProcessoAssunto> assuntosAhExportar = new ArrayList<>();
//            }

        }*/
    }

    public List<TpuAssunto> buscaAssuntosProcesso(Long idProcesso) throws Exception {
        List<Long> idsAssuntos = buscaIdsAssuntos(idProcesso);
        return tpuAssuntoService.getAssuntos(idsAssuntos);

    }

    private List<Long> buscaIdsAssuntos(Long idProcesso) {
        return repository.findIdsAssuntos(idProcesso);
    }
}
