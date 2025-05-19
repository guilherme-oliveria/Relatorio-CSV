package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.repository.gab.filter.ProcessoFilter;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.repository.webjud.OrgaosJulgadoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RevisorService {

    private final OrgaosJulgadoresRepository orgaosJulgadoresRepository;
    private final MinutaService minutaService;
    private final ProcessosRepository processosRepository;

    private final ProcessoService processoService;
    @Autowired
    public RevisorService(OrgaosJulgadoresRepository orgaosJulgadoresRepository, MinutaService minutaService, ProcessosRepository processosRepository,ProcessoService processoService) {
        this.orgaosJulgadoresRepository = orgaosJulgadoresRepository;
        this.minutaService = minutaService;
        this.processosRepository = processosRepository;
        this.processoService = processoService;
    }

    public Map<String, List<OrgaoJulgador>> findSouRevisadoPor(List<OrgaoJulgador> orgaoJulgadores) {
        Map<String, List<OrgaoJulgador>> mapOrgaoJulgadoresRevisor = new HashMap<>();
        orgaoJulgadores.forEach(orgaoJulgador -> {
            List<OrgaoJulgador> ojsRevisores = orgaosJulgadoresRepository.findSouRevisadoPor(orgaoJulgador);
            if (!ojsRevisores.isEmpty())
                mapOrgaoJulgadoresRevisor.put(orgaoJulgador.getId(), ojsRevisores);
        });
        return mapOrgaoJulgadoresRevisor;
    }

    public Map<String, List<OrgaoJulgador>> findAllOrgaosJulgadoresQueSouRevisor(List<OrgaoJulgador> orgaoJulgadores) {
        Map<String, List<OrgaoJulgador>> mapOrgaoJulgadoresRevisados = new HashMap<>();
        orgaoJulgadores.forEach(orgaoJulgador -> {
            List<OrgaoJulgador> souReviorDe = orgaosJulgadoresRepository.findQueSouRevisor(orgaoJulgador);
            if (!souReviorDe.isEmpty())
                mapOrgaoJulgadoresRevisados.put(orgaoJulgador.getId(), souReviorDe);
        });
        return mapOrgaoJulgadoresRevisados;
    }

    public MinutaAnexo getAnexoRelatorio(long id, String idRelatorio) throws Exception {
        return minutaService.pegaUltimaMinutaByProcesso(id).getAnexos().stream()
            .filter(minutaAnexo -> minutaAnexo.getTipoDocumento().getId().equals(idRelatorio)).findFirst().orElseThrow(() -> new Exception("Não foi localizado o relatório."));
    }

    public Page<Processo> getProcessosPorOJETarefaRevisarPaginada(Pageable pageable, List<OrgaoJulgador> orgaosJulgadoresCompleto) {
        Map<String, List<OrgaoJulgador>> revisadosPor = findAllOrgaosJulgadoresQueSouRevisor(orgaosJulgadoresCompleto);
        List<OrgaoJulgador> todosRevisados = new ArrayList<>();
        revisadosPor.forEach((s, orgaoJulgadors) -> orgaoJulgadors.forEach(orgaoJulgador -> {
            if(!todosRevisados.contains(orgaoJulgador)){
                todosRevisados.add(orgaoJulgador);
            }
        }));
        return processosRepository.getProcessosPorOJETarefaRevisarPaginada(new ProcessoFilter(),pageable,todosRevisados,TarefaEnum.Revisar);
    }

    public void enviaProcessoParaRevisor(long idProcesso, Usuario usuario) throws Exception {
        Processo processo = processoService.findById(idProcesso).orElseThrow(() ->
            new Exception("Não foi localizado o processo."));
        List<OrgaoJulgador> orgaoJulgadorList = usuario.getOrgaosJulgadoresCompleto().stream().filter(orgaoJulgador ->
            orgaoJulgador.getId().equals(processo.getOrgaoJulgador())).toList();
        if(possuiRevisor(orgaoJulgadorList)){
            processo.setTarefa(TarefaEnum.Revisar);
            processoService.save(processo);
        }
    }

    public boolean possuiRevisor(List<OrgaoJulgador> orgaoJulgadores){
        return !findSouRevisadoPor(orgaoJulgadores).isEmpty();
    }

    public boolean verificaSePossuoRevisor(String idOrgaoJulgador) {
        OrgaoJulgador orgaoJulgador = orgaosJulgadoresRepository.findById(idOrgaoJulgador).orElseThrow(NullPointerException::new);
        return possuiRevisor(List.of(orgaoJulgador));
    }
}
