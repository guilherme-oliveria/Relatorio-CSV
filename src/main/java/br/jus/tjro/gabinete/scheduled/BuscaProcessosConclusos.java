package br.jus.tjro.gabinete.scheduled;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.infrastructure.notifications.TelegramRequest;
import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.listener.kafka.consumers.AtualizaProcessoKafkaListener;
import br.jus.tjro.gabinete.listener.kafka.consumers.BuscaProcessosConclusosKafkaListener;
import br.jus.tjro.gabinete.listener.kafka.consumers.BuscaProcessosConclusosSgKafkaListener;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import br.jus.tjro.gabinete.model.gab.processo.WrapperAtualizaProcesso;
import br.jus.tjro.gabinete.model.gab.transiente.ProcessoConcluso;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.*;
import br.jus.tjro.gabinete.service.remoto.AssuntoRemotoService;
import br.jus.tjro.gabinete.service.remoto.CaixaRemotoService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import br.jus.tjro.gabinete.service.remoto.processo.ProcessoRemotoService;
import br.jus.tjro.gabinete.util.AuthenticationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BuscaProcessosConclusos {

    private final long SEGUNDO = 1000;
    private final boolean isAtivo;
    @Autowired
    private MinutaService minutaService;
    @Autowired
    private AssuntoRemotoService assuntoRemotoService;
    @Autowired
    private List<FonteDados> fontes;
    @Autowired
    private ProcessoService processoService;
    @Autowired
    private CaixaRemotoService caixaRemotoService;
    @Autowired
    private KafkaProducerService kafkaProducerService;
    @Autowired
    private AuthenticationUsuarioService auth;
    @Autowired
    private ProcessoRemotoService processoRemotoService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ProcessoTagService processoTagService;
    @Autowired
    private TelegramRequest telegram;

    private final Logger looger = LoggerFactory.getLogger(BuscaProcessosConclusos.class);

    @Autowired
    public BuscaProcessosConclusos(@Value("${scheduled.busca.processos.conclusos}") boolean isAtivo) {
        this.isAtivo = isAtivo;
    }

    @Scheduled(fixedDelay = SEGUNDO * 30)
    public void executa() {
        AuthenticationUtil.setAuthenticationAdminInContext();
        if (isAtivo) {
            for (FonteDados fonteDados : fontes) {
                try {
                    if(!fonteDados.getUrlBase().equals(""))
                        buscaProcessosConclusosNoWebService(fonteDados);
                } catch (Exception e) {
                    looger.error(e.getMessage(),e);
                }
            }
        }
    }

    private void buscaProcessosConclusosNoWebService(FonteDados fonteDados){
        ProcessoConcluso[] listaProcessosConclusos = new ProcessoConcluso[0];
        try {
            listaProcessosConclusos = processoRemotoService
                .buscaProcessosConclusos(fonteDados.getFonteDadosEnum());
        } catch (Exception e) {
            looger.error("Não foi possivel buscar lista de processos concluso na fonte de dados - "+fonteDados.getFonteDadosEnum()+" -> "+fonteDados.getUrlBase(),e);
        }
        if (listaProcessosConclusos != null) {
            for (ProcessoConcluso processoConcluso : listaProcessosConclusos) {
                try {
                    enviaMensagemKafka(processoConcluso,fonteDados);
                    processoRemotoService.confirmaRecebimentoProcesso(processoConcluso.getIdProcessoLegado(), fonteDados.getFonteDadosEnum());
                } catch (Exception e) {
                    looger.error("Erro ao confirmar recebimento do processo",e);
                }
            }
        }
    }

    private void enviaMensagemKafka(ProcessoConcluso processoConcluso, FonteDados fonteDados) {
        if(fonteDados.getFonteDadosEnum() == FonteDadosEnum.PJESG){
            processoConcluso.setFonteDadosEnum(fonteDados.getFonteDadosEnum());
            kafkaProducerService.send(BuscaProcessosConclusosSgKafkaListener.TOPIC_NAME,processoConcluso);
        }else if(fonteDados.getFonteDadosEnum() == FonteDadosEnum.PJEPG){
            processoConcluso.setFonteDadosEnum(fonteDados.getFonteDadosEnum());
            kafkaProducerService.send(BuscaProcessosConclusosKafkaListener.TOPIC_NAME,processoConcluso);
        }
    }

    public Processo buscaProcessosNoWebService(FonteDadosEnum fonteDados, Long idProcessoSistemaLegado, TarefaEnum tarefa,Usuario usuarioLogado) throws Exception {
        ProcessoConcluso processoConcluso = processoRemotoService.buscaProcessoConcluso(fonteDados,idProcessoSistemaLegado);
        if(processoConcluso == null)
            throw new Exception("Não foi possivel encontrar o processo na fonte de dados "+idProcessoSistemaLegado);
        return buscaProcessosNoWebService(fonteDados,processoConcluso,tarefa,usuarioLogado);
    }

    public Processo buscaProcessosNoWebService(FonteDadosEnum fonteDados, ProcessoConcluso processoConcluso, TarefaEnum tarefa,Usuario usuarioLogado) throws Exception {
        try {
            Processo processo = processoService.atualizaOuCriaProcesso(processoConcluso,
                fonteDados,tarefa,usuarioLogado);
            executaProcessoTagUmaVez(processo);
            minutaService.criaMinutaVoid(processo,usuarioLogado,processoConcluso);
            kafkaProducerService.send(AtualizaProcessoKafkaListener.TOPIC_NAME,new WrapperAtualizaProcesso(processo.getId(),usuarioLogado));
            telegram.notify(processo);
            return processo;
        } catch (Exception e) {
            throw new Exception("Erro ao importar processo "+processoConcluso.getNumeroProcesso(),e);
        }
    }

    public void executaProcessoTagUmaVez(Processo processo) {
        try {
            List<String> listTag = new ArrayList<>();
            tagService.importaOuAtualizaTagsDoProcesso(processo,listTag);
            processoTagService.atualizaTagImportPorProcesso(processo, listTag);
        } catch (Exception e) {
            looger.error("Problema na importacao das tags do processo id: "+processo.getId()+" "+processo.getNumeroProcesso(),e);
        }
    }
}
