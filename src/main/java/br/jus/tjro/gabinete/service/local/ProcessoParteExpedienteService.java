package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.listener.kafka.consumers.BuscaProcessoParteExpedienteKafkaListener;
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.processo.*;
import br.jus.tjro.gabinete.model.gab.transiente.ProcessoParteExpedienteTransiente;
import br.jus.tjro.gabinete.repository.gab.filter.ProcessoParteExpedienteFilter;
import br.jus.tjro.gabinete.repository.gab.pessoa.PessoaRepository;
import br.jus.tjro.gabinete.repository.gab.processo.*;
import br.jus.tjro.gabinete.service.importacao.ObtemItensParaAdicionarAtualizarOuExcluir;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import br.jus.tjro.gabinete.service.remoto.processo.ProcessoParteExpedienteRemotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ProcessoParteExpedienteService {

    private final Logger looger = LoggerFactory.getLogger(ProcessoParteExpedienteService.class);
    private final KafkaProducerService kafkaProducerService;
    private final List<FonteDados> fonteDados;
    private final ProcessoParteExpedienteRepository processoParteExpedienteRepository;
    private final ProcessoExpedienteRepository processoExpedienteRepository;
    private final ProcuradoriaRepository procuradoriaRepository;
    private final ProcessoDocumentoRepository processoDocumentoRepository;
    private final PessoaRepository pessoaRepository;
    private final ProcessoParteExpedienteRemotoService processoParteExpedienteRemotoService;


    @Autowired
    public ProcessoParteExpedienteService(ProcessoDocumentoRepository processoDocumentoRepository,
                                          PessoaRepository pessoaRepository,
                                          ProcuradoriaRepository procuradoriaRepository,
                                          ProcessoExpedienteRepository processoExpedienteRepository,
                                          ProcessoParteExpedienteRepository repository,
                                          List<FonteDados> fonteDados,
                                          ProcessoParteExpedienteRemotoService processoParteExpedienteRemotoService,
                                          KafkaProducerService kafkaProducerService) {
        this.processoParteExpedienteRepository = repository;
        this.processoExpedienteRepository = processoExpedienteRepository;
        this.procuradoriaRepository = procuradoriaRepository;
        this.processoParteExpedienteRemotoService = processoParteExpedienteRemotoService;
        this.fonteDados = fonteDados;
        this.pessoaRepository = pessoaRepository;
        this.processoDocumentoRepository = processoDocumentoRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    public void importaOuAtualizaExpedientesDoProcesso(Processo processo) throws Exception {
        List<ProcessoParteExpedienteTransiente> expedientesRemotos = Arrays.asList(processoParteExpedienteRemotoService.findAllForProcesso(processo, getFonte(FonteDadosEnum.PJEPG)));
        List<ProcessoParteExpediente> expedientesLocais = processo.getProcessoExpedientes().stream().flatMap(p -> p.getProcessoParteExpedientes().stream()).collect(Collectors.toList());
        ObtemItensParaAdicionarAtualizarOuExcluir listaDiferencas = new ObtemItensParaAdicionarAtualizarOuExcluir(expedientesLocais, expedientesRemotos);

        List<ProcessoParteExpedienteTransiente> processoParteExpedienteTransientes = listaDiferencas.getListaAdicao();
        processoParteExpedienteTransientes.addAll(listaDiferencas.getListaAtualizacao().values());

        processoParteExpedienteTransientes
            .forEach(ppe -> {
                ppe.setProcesso(processo);
                this.kafkaProducerService.send(BuscaProcessoParteExpedienteKafkaListener.TOPIC_NAME,ppe);
            });

        processoParteExpedienteRepository.deleteAll(listaDiferencas.getListaExclusao());
    }

    public ProcessoParteExpediente importaOuAtualizaProcessoParteExpediente(ProcessoParteExpedienteTransiente ppe, Processo processo){
        ProcessoParteExpediente processoParteExpediente = processoParteExpedienteRepository.findByIdLegado(ppe.getIdLegado()).orElse(new ProcessoParteExpediente());
        // Pessoa Parte
        Pessoa pessoaParte = salvaPessoa(ppe.getPessoaParte(),processo.getSistema());
        // Pessoa ciencia
        Pessoa pessoaCiencia = salvaPessoa(ppe.getPessoaCiencia(),processo.getSistema());
        // Procuradoria
        Procuradoria procuradoria = salvaProcuradoria(ppe.getProcuradoria());
        // ProcessoExpediente
        ProcessoExpediente processoExpediente = salvaProcessoExpediente(ppe.getProcessoExpediente(), processo);
        // ProcessoParteExpediente
        processoParteExpediente.setPropriedadesRemotas(ppe, pessoaParte, pessoaCiencia, procuradoria, processoExpediente);
        return processoParteExpedienteRepository.save(processoParteExpediente);
    }

    private ProcessoExpediente salvaProcessoExpediente(ProcessoExpediente remoto, Processo processo) {
        ProcessoExpediente procExp = null;
        if(remoto != null) {
            procExp = processoExpedienteRepository.findByIdLegado(remoto.getIdLegado()).orElse(new ProcessoExpediente());
            ProcessoDocumento procDocLocal = salvaDocumento(remoto.getProcessoDocumento(), processo);
            ProcessoDocumento procDocVinculado = salvaDocumento(remoto.getProcessoDocumentoVinculadoExpediente(), processo);
            procExp.setPropriedadesRemota(processo, remoto, procDocLocal, procDocVinculado);
            procExp = processoExpedienteRepository.save(procExp);
        }
        return procExp;
    }

    private ProcessoDocumento salvaDocumento(ProcessoDocumento remoto, Processo processo) {
        ProcessoDocumento procDoc = null;
        if(remoto != null) {
            procDoc = processoDocumentoRepository.findFirstByProcessoAndIdDocumentoSistemaLegado(processo, remoto.getId().toString());
            if(procDoc == null ) {
                procDoc = new ProcessoDocumento();
            }
            procDoc.setPropriedadesRemota(remoto, processo);
            procDoc = processoDocumentoRepository.save(procDoc);
        }
        return procDoc;
    }

    private Procuradoria salvaProcuradoria(Procuradoria remoto) {
        Procuradoria procuradoria = null;
        if(remoto != null) {
            procuradoria = procuradoriaRepository
                .findByIdLegado(remoto.getId()).orElse(new Procuradoria())
                .setPropriedadesRemota(remoto);
            procuradoria = procuradoriaRepository.save(procuradoria);
        }
        return procuradoria;
    }

    private Pessoa salvaPessoa(Pessoa pessoaRemota, FonteDadosEnum sistema) {
        Pessoa pessoa = null;
        if(pessoaRemota != null) {
            pessoa = pessoaRepository.findByIdPessoaLegadoAndSistema(pessoaRemota.getId(),sistema).orElseGet(() -> pessoaRepository.save(new Pessoa(pessoaRemota.getId(),
                pessoaRemota.getNome(),
                pessoaRemota.getEmail(),
                pessoaRemota.getDataNascimento(),
                pessoaRemota.getDataObito(),
                pessoaRemota.getTipoPessoa(),
                sistema)));
        }
        return pessoa;
    }

    private FonteDados getFonte(FonteDadosEnum fonte) {
        return fonte.selecionaFonte(this.fonteDados);
    }

    public List<ProcessoParteExpediente> buscaExpedientesDoProcesso(Processo processo) {
        return processoParteExpedienteRepository.findAllByProcesso(processo.getId());
    }

    public Page<ProcessoParteExpediente> listaPaginada(Processo processo, ProcessoParteExpedienteFilter processoParteExpedienteFilter, Pageable pageable, String query) {
        return processoParteExpedienteRepository.filtraProcessoParteExpediente(processo, processoParteExpedienteFilter, pageable, query);
    }
}
