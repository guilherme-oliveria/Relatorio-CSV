package br.jus.tjro.gabinete.scheduled.devolve.origem;

import br.jus.tjro.gabinete.api.RequisicaoRetornoConcluso;
import br.jus.tjro.gabinete.api.util.CodigoSegurancaUtils;
import br.jus.tjro.gabinete.dto.TagProcessoDTO;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.scheduled.devolve.origem.dto.RequisicaoRetornoConclusoDTO;
import br.jus.tjro.gabinete.service.assinaturadigital.RetornoAssinaturaService;
import br.jus.tjro.gabinete.service.local.*;
import br.jus.tjro.gabinete.service.local.verifica_integracao.VerificaIntegracaoService;
import br.jus.tjro.gabinete.service.remoto.TagRemotoService;
import br.jus.tjro.gabinete.service.remoto.processo.ProcessoRemotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DevolveOrigemSemManifestacao {
    private final Logger looger = LoggerFactory.getLogger(DevolveOrigemSemManifestacao.class);

    private final MinutaService documentoService;
    private final AssinaturaService assinaturaService;
    private final RetornoAssinaturaService retornoAssinaturaService;
    private final MinutaMovimentoService minutaMovimentoService;
    private final ProcessoRemotoService processoRemotoService;
    private final MinutaMovimentoComplementoService minutaMovimentoComplementoService;
    private final VerificaIntegracaoService verificaIntegracaoService;
    private final MinutaTarefaLogService minutaTarefaLogService;
    private final ProcessoService processoService;

    private final TagRemotoService tagRemotoService;

    private final Logger logger = LoggerFactory.getLogger(DevolveOrigemSemManifestacao.class);

    @Autowired
    public DevolveOrigemSemManifestacao(MinutaService documentoService, AssinaturaService assinaturaService,
                                        RetornoAssinaturaService retornoAssinaturaService,
                                        MinutaMovimentoService minutaMovimentoService, ProcessoRemotoService processoRemotoService,
                                        MinutaMovimentoComplementoService minutaMovimentoComplementoService, VerificaIntegracaoService verificaIntegracaoService,TagRemotoService tagRemotoService, MinutaTarefaLogService minutaTarefaLogService, ProcessoService processoService) {
        this.documentoService = documentoService;
        this.assinaturaService = assinaturaService;
        this.retornoAssinaturaService = retornoAssinaturaService;
        this.minutaMovimentoService = minutaMovimentoService;
        this.processoRemotoService = processoRemotoService;
        this.minutaMovimentoComplementoService = minutaMovimentoComplementoService;
        this.verificaIntegracaoService = verificaIntegracaoService;
        this.minutaTarefaLogService = minutaTarefaLogService;
        this.processoService = processoService;
        this.tagRemotoService = tagRemotoService;
    }


    public Processo devolve(Processo processo, Usuario usuario) throws Exception {
        boolean processoConcluso = true;
        try{
            processoConcluso = verificaIntegracaoService.processoIsConclusoGabinete(processo);
        }catch (Exception e){
            logger.error("Não foi possivel verificar tarefa no PJe",e);
        }
        if(!processoConcluso){
            processo.naoConcluso();
            minutaTarefaLogService.registrarLog(processo,usuario);
        }else{
            String codigoSeguranca = CodigoSegurancaUtils.gerarCodigoSeguranca("ambienteTeste");
            boolean lancarMov = true;
            if(!processo.minutaEmElaboracao().isEmpty()){
                Optional<Minuta> minuta = processo.minutaEmElaboracao();
                if(minuta.isPresent()){
                    if(minuta.get().getTiposDocumentos()!=null){
                        lancarMov=false;
                    }
                }
            }
            RequisicaoRetornoConcluso requisicao = new RequisicaoRetornoConclusoDTO(processo.getIdProcessoSistemaLegado().toString(), codigoSeguranca, true,lancarMov);
            TagProcessoDTO tagProcessoDTO = new TagProcessoDTO(processo.getIdProcessoSistemaLegado().toString());

            tagProcessoDTO= processoService.concluirImportacaoTag(tagProcessoDTO,processo);

            String retorno = null;
            retorno = processoRemotoService.devolveProcessoOrigem(requisicao, processo.getSistema());


            try{
                if(tagProcessoDTO!=null)
                    tagRemotoService.devolveTagPje(tagProcessoDTO,processo.getSistema());
            }catch (Exception e){
                looger.error("Erro ao devolvendo Tag para Pje do processo: "+processo.getNumeroProcesso(),e);
            }

            if (retorno != null) {
                processo.naoConcluso();
                minutaTarefaLogService.registrarLog(processo,usuario);
            }
        }
        return processoService.save(processo);
    }
}
