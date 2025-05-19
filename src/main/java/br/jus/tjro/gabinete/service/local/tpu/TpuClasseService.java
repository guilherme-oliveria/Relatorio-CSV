package br.jus.tjro.gabinete.service.local.tpu;

import br.jus.tjro.gabinete.model.gab.tpu.TpuClasse;
import br.jus.tjro.gabinete.service.remoto.tpu.TpuClasseRemotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TpuClasseService {

    private final TpuClasseRemotoService tpuClasseRemotoService;
    private final Map<Long, TpuClasse> classesCache = new HashMap<>();

    private final Map<Long, List<TpuClasse>> classesCacheArvore = new HashMap<>();

    @Autowired
    public TpuClasseService(TpuClasseRemotoService tpuClasseRemotoService) {
        this.tpuClasseRemotoService = tpuClasseRemotoService;
    }

    public List<TpuClasse> getArvoreClasses(Long id) throws Exception {
        List<TpuClasse> arvore;
        if (!classesCacheArvore.containsKey(id)) {
            arvore = tpuClasseRemotoService.getArvoreClasses(id);
            classesCacheArvore.put(id, arvore);
        } else {
            arvore = classesCacheArvore.get(id);
        }
        return arvore;
    }

    public TpuClasse getClasse(Long codigo) throws Exception {
        return tpuClasseRemotoService.getClasse(codigo);
    }
}
