package br.jus.tjro.gabinete.service.remoto.alvara;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.model.gab.alvara.*;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaTarefaLog;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.alvara.PagamentoAlvaraRepository;
import br.jus.tjro.gabinete.service.local.MinutaTarefaLogService;
import br.jus.tjro.gabinete.service.remoto.RemotoServiceAbstract;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class PagamentosRemotoService extends RemotoServiceAbstract {


    private final RestTemplate restTemplate;
    private final String url;
    private final String webJud;
    private final boolean mockIntegracaoCaixa;
    private final int validadeAlvara;

    private final Conta contaCentralizadora;
    @Autowired
    private PagamentoAlvaraRepository pagamentoAlvaraRepository;
    @Autowired
    private MinutaTarefaLogService minutaTarefaLogService;

    private final Logger logger = LoggerFactory.getLogger(PagamentosRemotoService.class);

    public PagamentosRemotoService(RestTemplate restTemplate,
                                   @Value("${ALVARA_VALIDADE:30}") int validadeAlvara,
                                   @Value("${integracao.caixa.url:http://localhost:8080}") String url,
                                   @Value("${webjud.url:http://webapp.tjro.jus.br}") String webJud,
                                   @Value("${integracao.caixa.mock:false}") boolean mockIntegracaoCaixa,
                                   @Value("${conta.centralizadora:{\"banco\":\"Default\",\"agencia\": \"0000\"," +
                                       "\"operacao\": \"000.0\",\"numeroConta\": \"00000000\",\"digitoVerificador\": \"0\"" +
                                       ",\"numeroProcesso\":\"00000000000000000\",\"idLegado\":0}}")
                                   String contaCentralizadora) throws JsonProcessingException {
        this.restTemplate = restTemplate;
        this.url = url;
        this.webJud = webJud + "/de-para/rest/orgaoJulgador";
        this.mockIntegracaoCaixa = mockIntegracaoCaixa;
        this.validadeAlvara = validadeAlvara;
        this.contaCentralizadora = new ObjectMapper().readValue(contaCentralizadora, Conta.class);
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }

    @Deprecated
    private void enviarAlvaraIntegracaoCaixa(GerarAlvara21Request gerarAlvara21Request) throws Exception {
        try {
            if (!mockIntegracaoCaixa)
                post(url + "/alvara/beneficiario", gerarAlvara21Request, String.class, null);
        } catch (Exception e) {
            throw new Exception("Erro ao tentar enviar alvará eletrônico. Número Processo: " + gerarAlvara21Request.getNumeroProcesso());
        }
    }

    private void enviarDepositoEAlvara27(GerarDepositoEAlvara27Request depositoEAlvara27Request) throws Exception {
        try {
            if (!mockIntegracaoCaixa)
                post(url+"/alvara/depositoEtransferencia", depositoEAlvara27Request, String.class, null);
        } catch (Exception e) {
            throw new Exception("Erro ao tentar enviar alvará eletrônico. Número Processo: " + depositoEAlvara27Request.getGerarAlvara27Request().getNumeroProcesso());
        }
    }
    public Boolean enviarAlvaraPagamento(Processo processo) throws Exception {
        Optional<Minuta> minutaOpt = processo.getMinutaComAlvaraParaIntegracao();
        if (minutaOpt.isPresent() && minutaOpt.get().temAlvara()) {
            Alvara alvara = minutaOpt.get().getAlvara();
            Requisitante autorizador = getAutorizador(alvara.getMinuta().getId());
            Long idOrgaoJulgadorSdsg = getIdOrgaoJulgador(alvara.getMinuta());
            List msgErro = new ArrayList();
            alvara.getPagamentoAlvara()
                .stream().filter(pa -> pa.isAtivo() && !pa.isEnviado())
                .forEach(pa -> {
                    try {
                        if (this.contaCentralizadora.isDefault()) {
                            throw new RuntimeException("Erro ao tentar enviar alvará eletrônico, conta centralizadora não informada. Informe os valores da conta centralizadora nas variáveis de ambiente.");
                        } else {
                            if (pa.getFormaPagamento().equals("T") && pa.getConta().isContaCentralizadora(this.contaCentralizadora)) {
                                GerarAlvara27Request alvara27Request = pa.gerarAlvara27Request(idOrgaoJulgadorSdsg);
                                GerarDepositoRequest depositoRequest = pa.gerarDepositoRequest(idOrgaoJulgadorSdsg, this.contaCentralizadora);
                                GerarDepositoEAlvara27Request depositoEAlvara27Request = new GerarDepositoEAlvara27Request(depositoRequest, alvara27Request);
                                enviarDepositoEAlvara27(depositoEAlvara27Request);
                            } else {
                                enviarAlvaraIntegracaoCaixa(pa.gerarAlvaraRequest(idOrgaoJulgadorSdsg, autorizador, validadeAlvara));
                            }
                            pa.setEnviado(true);
                            pagamentoAlvaraRepository.save(pa);
                        }
                    } catch (Exception e) {
                        String msg = "Falha ao enviar pagamento alvara. Pagamento id: " + pa.getId() + ", Número Processo: " + processo.getNumeroProcesso();
                        msgErro.add(msg);
                        logger.error(msg, e);
                    }
                });
            return !(msgErro.size() > 0);
        }
        return true;
    }

    private Requisitante getAutorizador(Long idMinuta) throws Exception {
        Optional<MinutaTarefaLog> optMinutaTarefaLog = minutaTarefaLogService.getPorIdMinutaAndTarefaEnum(idMinuta,
            TarefaEnum.ParaIntegracao).stream().findFirst();
        if (optMinutaTarefaLog.isPresent())
            return new Requisitante(optMinutaTarefaLog.get().getNomeAutor(), optMinutaTarefaLog.get().getAutorVersao());
        throw new Exception("Erro ao tentar buscar autorizador.");
    }

    private Long getIdOrgaoJulgador(Minuta minuta) throws ServicoRemotoException {
        try{
            Resposta respostaOJ = restTemplate.getForObject(webJud + "/" + minuta.getProcesso().getOrgaoJulgadorObj().getSistema() + "/" + minuta.getProcesso().getOrgaoJulgadorObj().getIdLegado(), Resposta.class);
            HashMap<String, String> sistemas = respostaOJ.getConteudo().getSistemas();
            final String[] idOrgaoJulgadorSdsg = {""};
            sistemas.forEach((s, s2) -> {
                if (s.equals("SDSG")) {
                    idOrgaoJulgadorSdsg[0] = s2;
                }
            });
            return Long.parseLong(idOrgaoJulgadorSdsg[0]);
        }catch (Throwable e){
            throw new ServicoRemotoException("Erro ao buscar orgao julgador no [de-para] para o processo: "+ minuta.getProcesso().numeroProcessoSemFormatacao(),e);
        }
    }
}
