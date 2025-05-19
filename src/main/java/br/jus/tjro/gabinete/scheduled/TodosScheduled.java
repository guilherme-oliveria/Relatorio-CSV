package br.jus.tjro.gabinete.scheduled;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.listener.model.processo.listener.LocalizadorListener;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.scheduled.devolve.origem.DevolveOrigem;
import br.jus.tjro.gabinete.util.AuthenticationUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Component
@EnableScheduling
public class TodosScheduled {

    private static final String TIME_ZONE = "America/Porto_Velho";
    private final long SEGUNDO = 1000;
    private final long MINUTO = SEGUNDO * 60;  // 1 minuto
    private final long HORA = MINUTO * 60;  // 1 hora

    private final BuscaPartesDosProcessos buscaPartesDosProcessos;
    private final BuscaEnderecosDasPartesDosProcessos buscaEnderecosDasPartesDosProcessos;

    @Autowired
    private BuscaDocumentosDasPessoas buscaDocumentosDasPessoas;

    @Autowired
    private BuscaAssuntosDosProcessosConclusos buscaAssuntosDosProcessosConclusos;

    @Autowired
    private BuscaDocumentosDosProcessosConclusos buscaDocumentosDosProcessosConclusos;

    @Autowired
    private ProcessosRepository processosRepository;

    @Autowired
    private BuscaTagsDosProcessos buscaTagsDosProcessos;

    @Autowired
    private BuscaMovimentosDosProcessos buscaMovimentosDosProcessos;

    @Autowired
    private BuscaExpedientesDoProcesso buscaExpedientesDoProcesso;

    private boolean isAtivo = false;
    private final Logger logger = LoggerFactory.getLogger(TodosScheduled.class);
    private final LocalizadorListener localizadorListener;

    @Autowired
    public TodosScheduled(@Value("${scheduled.main:false}") boolean isAtivo,
                          BuscaPartesDosProcessos buscaPartesDosProcessos,
                          BuscaEnderecosDasPartesDosProcessos buscaEnderecosDasPartesDosProcessos,
                          LocalizadorListener localizadorListener) {
        this.buscaPartesDosProcessos = buscaPartesDosProcessos;
        this.isAtivo = isAtivo;
        this.buscaEnderecosDasPartesDosProcessos = buscaEnderecosDasPartesDosProcessos;
        this.localizadorListener = localizadorListener;
    }

    @Scheduled(fixedDelay = 10000)
    public void executaTodas() {
        if (isAtivo) {
            Boolean integrado = false;
            AuthenticationUtil.setAuthenticationAdminInContext();
            Date dataAtual = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
            String numeroProcesso = "";
            LocalDateTime inicioDaExecucao = LocalDateTime.now();
            LocalDateTime dataUltimaHora = LocalDateTime.now().plusHours(-1);
            Date ut = Date.from(dataUltimaHora.atZone(ZoneId.systemDefault()).toInstant());
            Processo processo = null;
            try {
                processo = processosRepository.buscaOsProcessosQueDevemSincronizar(ut);
                if(processo !=null) {
                    numeroProcesso = processo.getNumeroProcesso();
                    try {
                        integrado = executaPorProcesso(processo);
                    } catch (Exception e) {
                        logger.error("Erro ao sincronizar processo", processo.getNumeroProcesso(), processo.getId(), e.getMessage(), e);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }finally {
                if(processo != null){
                    if(integrado){
                        processo.atualizaDataSincronizacaoSucesso(dataAtual);
                        registraLog(inicioDaExecucao,"Tempo para importar o processo "+numeroProcesso,false);
                    }
                    else{
                        processo.atualizaDataSincronizacaoErro(dataAtual);
                        registraLog(inicioDaExecucao,"Tempo para importar o processo "+numeroProcesso,true);
                    }
                    Processo processoUpdated = processosRepository.save(processo);
                    localizadorListener.send(processoUpdated);
                }
            }
        }
    }

    private void registraLog(LocalDateTime inicioDaExecucao, String msg, boolean falha){
        LocalDateTime fimDaExecucao = LocalDateTime.now();

        Duration duracao = Duration.between(inicioDaExecucao, fimDaExecucao);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss:SS");
        String texto = "Início: " + inicioDaExecucao.format(formato) + ", Fim: " + fimDaExecucao.format(formato) + "" +
            ", Duração: " + duracao.toMillis() + " milissegundos.";
        if(falha)
            logger.error("Erro na sincronização: "+texto+" - "+msg);
        else
            logger.info(texto+" - "+msg);

    }

    @Async
    @Transactional
    public Boolean executaPorProcesso(final Processo processo) {
        try{
            logger.info("Busca Partes do processo "+processo.getNumeroProcesso());
            var importacaoComSucesso = buscaPartesDosProcessos.executaPorProcesso(processo);
            logger.info("Busca enderecos das partes do processo "+processo.getNumeroProcesso());
            importacaoComSucesso = buscaEnderecosDasPartesDosProcessos.executaPorProcesso(processo) && importacaoComSucesso;
            logger.info("Busca documentos das partes do processo "+processo.getNumeroProcesso());
            importacaoComSucesso = buscaDocumentosDasPessoas.executaPorProcesso(processo) && importacaoComSucesso;
            logger.info("Busca assuntos do processo "+processo.getNumeroProcesso());
            importacaoComSucesso = buscaAssuntosDosProcessosConclusos.executaPorProcesso(processo) && importacaoComSucesso;
            logger.info("Busca documentos do processo "+processo.getNumeroProcesso());
            importacaoComSucesso = buscaDocumentosDosProcessosConclusos.executaPorProcesso(processo,0) && importacaoComSucesso;
            logger.info("Busca Movimentos do processo "+processo.getNumeroProcesso());
            importacaoComSucesso = buscaMovimentosDosProcessos.executaPorProcesso(processo) && importacaoComSucesso;
            logger.info("Busca Expedientes do processo "+processo.getNumeroProcesso());
            importacaoComSucesso = buscaExpedientesDoProcesso.executaPorProcesso(processo) && importacaoComSucesso;

            return importacaoComSucesso;
        }catch (Exception e){
            logger.error("Erro na importação do processo. IdProcesso: "+processo.getId()+" "+processo.getNumeroProcesso(),e);
            return false;
        }
    }

    @CacheEvict(allEntries = true, cacheNames = {"tiposDocumentos", "tipoDocumentoCache","tipoDocumento","tiposDocumentosIds"})
    @Scheduled(fixedDelay =60 * 60 * 1000, initialDelay = 500)
    public void reportCacheEvict() {}

    @CacheEvict(allEntries = true, cacheNames = {"orgao-julgador",
        "usuario-orgao-julgador",
        "usuario-papel",
        "tpuClasseArvore",
        "tpuClasse"})
    @Scheduled(fixedDelay = 12 * 3600000, initialDelay = 500)
    public void reportCacheEvictAll() {}
}
