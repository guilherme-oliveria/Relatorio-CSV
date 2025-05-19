package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.interfaces.Tarefa;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.repository.gab.localizador.CaixasRepository;
import br.jus.tjro.gabinete.service.remoto.CaixaRemotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CaixaService {

    @Autowired
    CaixasRepository caixasRepository;

    @Autowired
    CaixaRemotoService caixaRemotoService;

    @Autowired
    private List<Tarefa> tarefas;

    private final String nomeCaixaValidarExpedienteCartorio;

    @Autowired
    public CaixaService(@Value("${CAIXA_VALIDA_EXPEDIENTE_CARTORIO:Validar Expediente Cartório}") String nomeCaixaValidarExpedienteCartorio) {
        this.nomeCaixaValidarExpedienteCartorio = nomeCaixaValidarExpedienteCartorio;
    }

    public List<Caixa> listaCaixasAgrupadas(List<OrgaoJulgador> idOrgaoJulgadores) {
        List<TarefaEnum> tarefasVisiveis = new ArrayList<TarefaEnum>();

        for (Tarefa t : tarefas) {
            if (t.isTarefaVisivel())
                tarefasVisiveis.add(t.getTarefa());
        }
        return caixasRepository.obtemListaCaixasAgrupadas(tarefasVisiveis, idOrgaoJulgadores);
    }

    public Caixa procuraOuCadastraPorNomeCaixa(String nome) {
        return caixasRepository.findByNome(nome).orElseGet(() -> caixasRepository.save(new Caixa(nome)));
    }

    public Caixa findAny() {
        return caixasRepository.findAll().iterator().next();
    }

    public Optional<Caixa> findById(Integer idCaixa) {
        return caixasRepository.findById(idCaixa);
    }

    public Caixa selecionaCaixa(Processo processo) throws Exception {
        Caixa caixa;
        if(processo.getTarefa() != TarefaEnum.NaoConcluso) {
            String nomeCaixaSistemaLegado = caixaRemotoService
                .getCaixaPorIdProcesso(processo.getIdProcessoSistemaLegado(),processo.getSistema());
            caixa = procuraOuCadastraPorNomeCaixa(nomeCaixaSistemaLegado);
        }else
            caixa = findAny();
        return caixa;
    }

    public Caixa selecionaCaixaPje(Processo processo) throws Exception {
        Caixa caixa;
            String nomeCaixaSistemaLegado = caixaRemotoService
                .getCaixaPorIdProcesso(processo.getIdProcessoSistemaLegado(),processo.getSistema());
            caixa = procuraOuCadastraPorNomeCaixa(nomeCaixaSistemaLegado);

        return caixa;
    }

    public Caixa getCaixaValidarExpedienteCartorio() {
        return caixasRepository.findByNome(this.nomeCaixaValidarExpedienteCartorio)
            .orElseGet(() -> caixasRepository.save(new Caixa(this.nomeCaixaValidarExpedienteCartorio)));
    }
}
