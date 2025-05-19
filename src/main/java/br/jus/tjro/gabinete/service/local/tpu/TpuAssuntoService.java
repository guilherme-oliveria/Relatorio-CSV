package br.jus.tjro.gabinete.service.local.tpu;

import br.jus.tjro.gabinete.model.gab.tpu.TpuAssunto;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoAssuntoRepository;
import br.jus.tjro.gabinete.service.remoto.tpu.TpuAssuntoRemotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TpuAssuntoService {

    private final TpuAssuntoRemotoService tpuAssuntoRemotoService;
    private final Map<Long, TpuAssunto> assuntoCache = new HashMap<>();

    private final Map<Long, List<TpuAssunto>> assuntoCacheArvore = new HashMap<>();

    private final ProcessoAssuntoRepository processoAssuntoRepository;

    @Autowired
    public TpuAssuntoService(
        ProcessoAssuntoRepository processoAssuntoRepository,
        TpuAssuntoRemotoService tpuAssuntoRemotoService) {
        this.tpuAssuntoRemotoService = tpuAssuntoRemotoService;
        this.processoAssuntoRepository = processoAssuntoRepository;
    }

    public List<TpuAssunto> getArvoreAssuntos(Long id) throws Exception {
        List<TpuAssunto> arvore;
        if (!assuntoCacheArvore.containsKey(id)) {
            arvore = tpuAssuntoRemotoService.getArvoreAssuntos(id);
            assuntoCacheArvore.put(id, arvore);
        } else {
            arvore = assuntoCacheArvore.get(id);
        }
        return arvore;
    }

    public TpuAssunto getAssunto(Long codigo) throws Exception {
        List<Long> codigos = new ArrayList<Long>();
        codigos.add(codigo);
        return getAssuntoCacheOuBuscaServidorRemotoEhAiciona(codigos).get(0);
    }

    public List<TpuAssunto> getAssuntos(List<Long> codigos) throws Exception {
        return getAssuntoCacheOuBuscaServidorRemotoEhAiciona(codigos);
    }

    public List<TpuAssunto> assuntosDoProcesso(Long idProcesso) throws Exception {
        List<Long> codigos = this.processoAssuntoRepository.findIdsAssuntos(idProcesso);
        return getAssuntos(codigos);
    }

    private List<TpuAssunto> recuperaDoCache(List<Long> assuntoIds) {
        return assuntoIds.parallelStream().map(id -> this.assuntoCache.get(id)).collect(Collectors.toList());
    }

    private List<TpuAssunto> getAssuntoCacheOuBuscaServidorRemotoEhAiciona(List<Long> codigos) throws Exception {
        List<Long> assuntoParabuscarSrvRemoto = assuntosQueFaltam(codigos);
        List<TpuAssunto> assunstosIndisponiveis = new ArrayList<>();
        if (!assuntoParabuscarSrvRemoto.isEmpty()) {
            List<TpuAssunto> assuntoRemoto = tpuAssuntoRemotoService.getAssuntos(assuntoParabuscarSrvRemoto);
            assuntoRemoto.stream().filter(it -> !it.getDescricao().contains("Indisponivel")).forEach(p -> assuntoCache.put(p.getCodigo(), p));
            assunstosIndisponiveis = assuntoRemoto.stream().filter(it -> it.getDescricao().contains("Indisponivel")).collect(Collectors.toList());
        }
        assunstosIndisponiveis.addAll(recuperaDoCache(codigos));
        return assunstosIndisponiveis;
    }

    private List<Long> assuntosQueFaltam(List<Long> codigos) {
        List<Long> assuntosParabuscarSrvRemoto = new ArrayList<Long>();
        codigos.forEach(p -> {
            if (!assuntoCache.containsKey(p))
                assuntosParabuscarSrvRemoto.add(p);
        });
        return assuntosParabuscarSrvRemoto;
    }
}
