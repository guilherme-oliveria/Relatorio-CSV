package br.jus.tjro.gabinete.scheduled.devolve.origem;

import br.jus.tjro.gabinete.api.RequisicaoRetornoConcluso;
import br.jus.tjro.gabinete.api.modelo.DocumentoApi;
import br.jus.tjro.gabinete.api.modelo.Movimento;
import br.jus.tjro.gabinete.api.util.CodigoSegurancaUtils;
import br.jus.tjro.gabinete.exceptions.DevolveOrigemException;
import br.jus.tjro.gabinete.exceptions.StorageException;
import br.jus.tjro.gabinete.listener.kafka.consumers.PagamentosAlvaraKafkaListener;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimento;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.scheduled.devolve.origem.builder.DocumentoBuilderApi;
import br.jus.tjro.gabinete.scheduled.devolve.origem.builder.MovimentoBuilderApi;
import br.jus.tjro.gabinete.scheduled.devolve.origem.builder.PublicarDjeBuilderApi;
import br.jus.tjro.gabinete.scheduled.devolve.origem.builder.VariaveisInstanciaBuilder;
import br.jus.tjro.gabinete.service.assinaturadigital.RetornoAssinaturaService;
import br.jus.tjro.gabinete.service.local.*;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import br.jus.tjro.gabinete.service.remoto.processo.ProcessoRemotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.jus.tjro.gabinete.Tarefas.core.TarefaEnum.ParaIntegracaoAlvara;
import static br.jus.tjro.gabinete.scheduled.devolve.origem.ErrosDevolveOrigem.FalhaRecuperarAssinaturaStorage;
import static br.jus.tjro.gabinete.scheduled.devolve.origem.ErrosDevolveOrigem.SemMovimento;
import static br.jus.tjro.gabinete.util.AuthenticationUtil.getUsuarioSistema;

@Service
public class DevolveOrigemMinutaAnexo {

    private final RetornoAssinaturaService retornoAssinaturaService;
    private final MinutaMovimentoService minutaMovimentoService;
    private final ProcessoRemotoService processoRemotoService;
    private final MinutaMovimentoComplementoService minutaMovimentoComplementoService;
    private final MinutaAnexoService documentoBinService;
    private final MinutaTarefaLogService minutaTarefaLogService;
    private final KafkaProducerService kafkaProducerService;
    private final ProcessosRepository processosRepository;

    private final Logger logger = LoggerFactory.getLogger(DevolveOrigemMinutaAnexo.class);

    @Autowired
    public DevolveOrigemMinutaAnexo(RetornoAssinaturaService retornoAssinaturaService,
                                    MinutaMovimentoService minutaMovimentoService, ProcessoRemotoService processoRemotoService,
                                    MinutaMovimentoComplementoService minutaMovimentoComplementoService, MinutaAnexoService documentoBinService, MinutaTarefaLogService minutaTarefaLogService, KafkaProducerService kafkaProducerService, ProcessosRepository processosRepository) {
        this.minutaTarefaLogService = minutaTarefaLogService;
        this.retornoAssinaturaService = retornoAssinaturaService;
        this.minutaMovimentoService = minutaMovimentoService;
        this.processoRemotoService = processoRemotoService;
        this.minutaMovimentoComplementoService = minutaMovimentoComplementoService;
        this.documentoBinService = documentoBinService;
        this.kafkaProducerService = kafkaProducerService;
        this.processosRepository = processosRepository;
    }

    public Processo devolve(List<MinutaAnexo> anexos, Processo processo) throws Exception {
        logger.info("Enviando anexos a origem");
        anexos.stream().filter(m -> m.getIdMinutaSistemaLegado() == null).forEach( m-> {
            try {
                enviaEhSalva(m);
            } catch (DevolveOrigemException e) {
                throw new RuntimeException(e.getMessage(),e);
            }
        });
        if(processo.minutaFoiIntegrado()) {
            processo.naoConcluso();
            minutaTarefaLogService.registrarLog(processo, getUsuarioSistema());
            if (processo.getTarefa().equals(ParaIntegracaoAlvara))
                kafkaProducerService.send(PagamentosAlvaraKafkaListener.TOPIC_NAME, processo.getId());
            return processosRepository.save(processo);
        }
        return processo;
    }

    protected void enviaEhSalva(MinutaAnexo anexo) throws DevolveOrigemException {
        logger.info("Enviando DocumentoBin: " + anexo.getId());
        Processo processo = anexo.getMinutaPai().getProcesso();
        String idLegado = processo.getIdProcessoSistemaLegado().toString();
        DocumentoApi documentoApi = null;
        String mensagemErroIdMinutaNumeroProcesso = " idMinutaAnexo = " + anexo.getId() + " - Numero Processo " + processo.getNumeroProcesso();
        try {
            documentoApi = new DocumentoBuilderApi(retornoAssinaturaService).parse(anexo);
        } catch (StorageException e) {
            throw new DevolveOrigemException("Erro ao recuperar assinatura no storage. Hash do P7s "
                + anexo.getHashP7s()
                + mensagemErroIdMinutaNumeroProcesso
                +" "+FalhaRecuperarAssinaturaStorage, e);
        }catch (Exception e) {
            throw new DevolveOrigemException("Erro ao converter anexo para documento builder. " + mensagemErroIdMinutaNumeroProcesso, e);
        }
        RequisicaoRetornoConcluso requisicao = preparaRequisicao(documentoApi, idLegado);
        if (verificaSeEhUltimoAnexo(anexo)) {
            requisicao = concluiImportacao(requisicao, anexo.getMinutaPai());
        }
        String idDocumentoSistemaLegado = null;
        try {
            idDocumentoSistemaLegado = processoRemotoService.devolveProcessoOrigem(requisicao,
                anexo.getMinutaPai().getProcesso().getSistema());
        } catch (Exception e) {
            throw new DevolveOrigemException("Erro ao enviar anexo a origem. " + mensagemErroIdMinutaNumeroProcesso, e);
        }
        if (idDocumentoSistemaLegado == null)
            return;
        anexo.setIdMinutaSistemaLegado(idDocumentoSistemaLegado);
        try {
            documentoBinService.save(anexo);
        } catch (Exception e) {
            throw new DevolveOrigemException("Erro ao atualizar anexo no banco. " + mensagemErroIdMinutaNumeroProcesso, e);
        }
    }

    private RequisicaoRetornoConcluso preparaRequisicao(DocumentoApi documentoApi, String idLegado) {
        return new RequisicaoRetornoConcluso(documentoApi, idLegado,
            CodigoSegurancaUtils.gerarCodigoSeguranca("ambienteTeste"));
    }

    @Deprecated
    private boolean verificaSeEhUltimoAnexo(MinutaAnexo documentoBin) {
        List<MinutaAnexo> anexos = documentoBinService.getAnexosByminutaPai(documentoBin.getMinutaPai());
        int quantidadeDocumentos = 0;
        for (MinutaAnexo anexo : anexos) {
            if (anexo.getIdMinutaSistemaLegado() == null)
                quantidadeDocumentos++;

        }
        return quantidadeDocumentos == 1;
    }

    @Deprecated
    private RequisicaoRetornoConcluso concluiImportacao(RequisicaoRetornoConcluso requisicao, Minuta minuta) throws DevolveOrigemException {
        requisicao.setConcluirImportacao(true);
        requisicao.setVariaveis(VariaveisInstanciaBuilder.criaVariaveisDaRequisicao(minuta));
        List<Movimento> movimentos = getMovimentos(minuta);
        requisicao.setMovimentos(movimentos);
        requisicao.setPublicarDje(
            PublicarDjeBuilderApi.convertePublicarProcessoDjeParaPublicarDje(minuta.getPublicacaoDje()));
        return requisicao;
    }

    @Deprecated
    private List<Movimento> getMovimentos(Minuta documento) throws DevolveOrigemException {
        List<MinutaMovimento> docMovs = minutaMovimentoService.findByMinuta(documento);
        if(docMovs == null || docMovs.size() <= 0)
            throw new DevolveOrigemException("Não existe movimento para devolver origem. "+SemMovimento);
        for (MinutaMovimento docMov : docMovs) {
            docMov.setMinutaMovimentoComplementos(minutaMovimentoComplementoService.findByMinutaMovimento(docMov));
        }
        List<Movimento> movs = MovimentoBuilderApi.converteMovimentosMinutaParaMovimentosApi(docMovs);
        return movs;
    }
}
