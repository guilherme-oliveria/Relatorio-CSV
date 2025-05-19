package br.jus.tjro.gabinete.scheduled.devolve.origem;

import br.jus.tjro.gabinete.api.RequisicaoRetornoConcluso;
import br.jus.tjro.gabinete.api.modelo.DocumentoApi;
import br.jus.tjro.gabinete.api.modelo.Movimento;
import br.jus.tjro.gabinete.api.util.CodigoSegurancaUtils;
import br.jus.tjro.gabinete.dto.TagProcessoDTO;
import br.jus.tjro.gabinete.exceptions.DevolveOrigemException;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimento;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoTag;
import br.jus.tjro.gabinete.model.gab.question.QuestionBase;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoTagRepository;
import br.jus.tjro.gabinete.scheduled.devolve.origem.builder.DocumentoBuilderApi;
import br.jus.tjro.gabinete.scheduled.devolve.origem.builder.MovimentoBuilderApi;
import br.jus.tjro.gabinete.scheduled.devolve.origem.builder.PublicarDjeBuilderApi;
import br.jus.tjro.gabinete.scheduled.devolve.origem.builder.VariaveisInstanciaBuilder;
import br.jus.tjro.gabinete.scheduled.devolve.origem.dto.DocumentoApiDTO;
import br.jus.tjro.gabinete.service.assinaturadigital.RetornoAssinaturaService;
import br.jus.tjro.gabinete.service.local.*;
import br.jus.tjro.gabinete.service.remoto.TagRemotoService;
import br.jus.tjro.gabinete.service.remoto.processo.ProcessoRemotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import java.util.List;

import static br.jus.tjro.gabinete.scheduled.devolve.origem.ErrosDevolveOrigem.FalhaRecuperarAssinaturaStorage;
import static br.jus.tjro.gabinete.scheduled.devolve.origem.ErrosDevolveOrigem.SemMovimento;

@Service
public class DevolveOrigemMinuta {

    private final MinutaService documentoService;
    private final AssinaturaService assinaturaService;
    private final RetornoAssinaturaService retornoAssinaturaService;
    private final MinutaMovimentoService minutaMovimentoService;
    private final ProcessoRemotoService processoRemotoService;
    private final MinutaMovimentoComplementoService minutaMovimentoComplementoService;
    private final TagRemotoService tagRemotoService;
    private final ProcessoTagRepository processoTagRepository;

    private final ProcessoService processoService;
    private final Logger looger = LoggerFactory.getLogger(DevolveOrigemMinuta.class);

    @Autowired
    public DevolveOrigemMinuta(MinutaService documentoService, AssinaturaService assinaturaService,
                               RetornoAssinaturaService retornoAssinaturaService,
                               MinutaMovimentoService minutaMovimentoService, ProcessoRemotoService processoRemotoService,
                               MinutaMovimentoComplementoService minutaMovimentoComplementoService, TagRemotoService tagRemotoService, ProcessoTagRepository processoTagRepository,ProcessoService processoService) {
        this.documentoService = documentoService;
        this.assinaturaService = assinaturaService;
        this.retornoAssinaturaService = retornoAssinaturaService;
        this.minutaMovimentoService = minutaMovimentoService;
        this.processoRemotoService = processoRemotoService;
        this.minutaMovimentoComplementoService = minutaMovimentoComplementoService;
        this.tagRemotoService = tagRemotoService;
        this.processoTagRepository = processoTagRepository;
        this.processoService = processoService;
    }


    public List<MinutaAnexo> devolve(Processo processo)
        throws Exception {
        if(processo.minutaEmElaboracao().isEmpty())
            return List.of();
        Minuta minuta = processo.minutaEmElaboracao()
            .orElseThrow(() -> new RuntimeException("Não há minuta para integrar para o processo: " + processo.getNumeroProcesso()));
        if(minuta == null)
            throw new DevolveOrigemException("Não existe minuta para enviar a origem");
        String mensagemErroIdMinutaNumeroProcesso = " idMinuta = " + minuta.getId() + " - Numero Processo " + processo.getNumeroProcesso();
        if(minuta.getQuestions() != null && !minuta.getQuestions().isEmpty()) {
            QuestionBase docSigiloso = minuta.getQuestions().stream().filter(q -> q.getKey().equals("sigilo")).collect(Collectors.toList()).get(0);
            if(docSigiloso.getValue() != null && docSigiloso.getValue().equals("true"))
                minuta.setEhSigiloso(true);
            else
                minuta.setEhSigiloso(false);
        } else {
            minuta.setEhSigiloso(false);
        }
        if (minuta.getIdMinutaSistemaLegado() != null)
            return minuta.getAnexos() == null ? List.of() : minuta.getAnexos();
        String idLegado = minuta.getProcesso().getIdProcessoSistemaLegado().toString();

        byte[] arquivoAssinado = new byte[0];

        try {
            arquivoAssinado = this.assinaturaService.getConteudoMinutaBytesAssinado(minuta);
        } catch (Exception e) {
            throw new DevolveOrigemException("Erro ao recuperar assinatura no storage. Hash do P7s "
                + minuta.getHashP7s()
                + mensagemErroIdMinutaNumeroProcesso
                +" "+FalhaRecuperarAssinaturaStorage, e);
        }

        minuta.setArquivoAssinado(arquivoAssinado);

        DocumentoApiDTO documentoApi = null;
        try {
            documentoApi = new DocumentoBuilderApi(retornoAssinaturaService).parse(minuta);
            documentoApi.setSigilo(minuta.getEhSigiloso());
        } catch (Exception e) {
            throw new DevolveOrigemException("Erro ao converter minuta para documento builder. " + mensagemErroIdMinutaNumeroProcesso, e);
        }
        RequisicaoRetornoConcluso requisicao = preparaRequisicao(documentoApi, idLegado);
        TagProcessoDTO tagProcessoDTO = new TagProcessoDTO(idLegado);

        if (verificaSeEhUltimoDocumento(minuta.getAnexos())) {
            requisicao = concluiImportacao(requisicao, minuta);

            tagProcessoDTO= processoService.concluirImportacaoTag(tagProcessoDTO,processo);
        }

        String idDocumentoSistemaLegado = null;
        try {
            idDocumentoSistemaLegado = processoRemotoService.devolveProcessoOrigem(requisicao, processo.getSistema());

            try{
                if(tagProcessoDTO!=null && tagProcessoDTO.getTagsList() != null)
                    tagRemotoService.devolveTagPje(tagProcessoDTO,processo.getSistema());
            }catch (Exception e){
                looger.error("Erro ao devolvendo Tag para Pje do processo: "+processo.getNumeroProcesso(),e);
            }
        } catch (Exception e) {
            throw new DevolveOrigemException("Erro ao enviar minuta a origem. " + mensagemErroIdMinutaNumeroProcesso, e);
        }

        if (idDocumentoSistemaLegado == null)
            return null;
        minuta.setIdMinutaSistemaLegado(idDocumentoSistemaLegado);
        return minuta.getAnexos();
    }

    private RequisicaoRetornoConcluso preparaRequisicao(DocumentoApiDTO documentoApi, String idLegado) {
        return new RequisicaoRetornoConcluso(documentoApi, idLegado,
            CodigoSegurancaUtils.gerarCodigoSeguranca("ambienteTeste"));
    }

    @Deprecated
    private boolean verificaSeEhUltimoDocumento(List<MinutaAnexo> anexo) {
        return anexo == null || anexo.size() < 1;
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
