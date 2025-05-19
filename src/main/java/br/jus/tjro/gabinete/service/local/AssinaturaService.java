package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.assinaturadigital.utils.PKCS7Utils;
import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.dto.UsuarioMobileDTO;
import br.jus.tjro.gabinete.interfaces.StorageService;
import br.jus.tjro.gabinete.model.gab.ParametrosEnvioArquivosParaAssinaturaTjOffice;
import br.jus.tjro.gabinete.model.gab.enums.TipoAssinatura;
import br.jus.tjro.gabinete.model.gab.enums.TipoDocumentoAssinatura;
import br.jus.tjro.gabinete.model.gab.minuta.*;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.PedidoInclusaoPautaRepository;
import br.jus.tjro.gabinete.scheduled.devolve.origem.DevolveOrigem;
import br.jus.tjro.gabinete.service.assinatura.AssinaturaMobileService;
import br.jus.tjro.gabinete.service.assinatura.CadeiaCertificadoService;
import br.jus.tjro.gabinete.service.assinaturadigital.RetornoAssinaturaService;
import br.jus.tjro.gabinete.service.assinaturadigital.ValidadorAssinaturaDigitalService;
import br.jus.tjro.gabinete.service.assinaturamobile.AssinadorA1;
import br.jus.tjro.gabinete.service.remoto.UsuarioRemotoService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import br.jus.tjro.gabinete.util.ParametrosUtil;
import br.jus.tjro.gabinete.util.StringUtils;
import br.jus.tjro.gabinete.model.gab.Usuario;
import org.apache.commons.codec.binary.Base64;
import br.jus.tjro.gabinete.util.Sha256Util;
import br.jus.tjro.gabinete.model.gab.assinaturamobile.ResultadoAssinatura;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AssinaturaService {

    private final Logger looger = LoggerFactory.getLogger(AssinaturaService.class);

    PKCS7Utils pkcs7Util;
    private final MinutaService minutaService;
    private final ProcessoService processoService;
    private final StorageService storageService;

    @Autowired
    private MinutaAnexoService minutaAnexoService;
    @Autowired
    private UsuarioRemotoService usuarioRemotoService;
    @Autowired
    private AssinadorA1 assinadorA1;
    @Autowired
    private ValidadorAssinaturaDigitalService assinaturaDigitalService;
    @Autowired
    private ParametrosUtil parametro;
    @Autowired
    private CadeiaCertificadoService cadeiaCertificadoService;
    @Autowired
    private AssinaturaMobileService assinaturaMobileService;
    @Autowired
    private RetornoAssinaturaService retornoAssinaturaService;
    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    public AssinaturaService(MinutaService minutaService,
                             ProcessoService processoService, MinutaAnexoService minutaAnexoService, StorageService storageService,
                             MinutaTarefaLogService minutaTarefaLogService,
                             PedidoInclusaoPautaRepository pedidoInclusaoPautaRepository) {
        this.processoService = processoService;
        this.minutaService = minutaService;
        this.storageService = storageService;
        this.pkcs7Util = PKCS7Utils.getInstance();
    }

    public AssinaturaService(MinutaService minutaService,
                             ProcessoService processoService, MinutaAnexoService minutaAnexoService, PKCS7Utils pkcs7,
                             StorageService storageService, @Value("${assinatura.digital.validar:true}") boolean validaAssinatura,
                             KafkaProducerService kafkaProducerService,
                             MinutaTarefaLogService minutaTarefaLogService,
                             PedidoInclusaoPautaRepository pedidoInclusaoPautaRepository
    ) {
        this.processoService = processoService;
        this.minutaService = minutaService;
        this.pkcs7Util = pkcs7;
        this.storageService = storageService;
    }

    public String getUrlGabinete() {
        return parametro.getValorString("GABINETE_HOST");
    }

    public byte[] getConteudoMinutaBytesAssinado(Minuta documento) throws Exception {
        byte[] mensagemOriginal = documento.getMinutaHtmlRenderizadoBytes();

        if(documento.getAssinatura() == null){
            byte[] detachado = storageService.loadBytes(documento.getHashP7s());
            return converterParaAtachado(mensagemOriginal, detachado);
        }else{
            return mensagemOriginal;
        }
    }

    public byte[] converterParaAtachado(byte[] mensagemOriginal, byte[] detachado) throws IOException {
        return pkcs7Util.converterParaAtachado(mensagemOriginal, detachado);
    }

    public List<ParametrosEnvioArquivosParaAssinaturaTjOffice> parametrosBuilder(Iterable<Processo> processos) {
        List<ParametrosEnvioArquivosParaAssinaturaTjOffice> parametros = new ArrayList<ParametrosEnvioArquivosParaAssinaturaTjOffice>();
        List<ParametrosEnvioArquivosParaAssinaturaTjOffice> parametroAux;
        for (Processo processo : processos) {
            try {
                parametroAux = parametrosBuilder(processo);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(),e);
            }
            parametros.addAll(parametroAux);
        }
        return parametros;
    }

    public List<ParametrosEnvioArquivosParaAssinaturaTjOffice> parametrosBuilder(Processo processo) throws Exception {
        List<ParametrosEnvioArquivosParaAssinaturaTjOffice> arquivos = new ArrayList<ParametrosEnvioArquivosParaAssinaturaTjOffice>();
        Minuta minuta = processo.minutaEmElaboracao().orElseThrow(() -> new Exception("Não foi possivel encontra minuta para o processo "+processo.getNumeroProcesso()));
        //TODO colocar essa logica de movimento no modelos
        if(minuta.getMinutaMovimentos() == null || minuta.getMinutaMovimentos().size() <= 0)
            throw new RuntimeException("Não é possivel assinar o processo "+processo.getNumeroProcesso()+". Selecionar um movimento processual.");

        if(minuta.isAssinaPauta())
            return parametrosPedidoInclusaoPautaBuilder(minuta);
        else{
            arquivos.add(new ParametrosEnvioArquivosParaAssinaturaTjOffice("Minuta - id " + minuta.getId(),
                getUrlGabinete() + "/minutas/stream/" + minuta.getId(), minuta));
            arquivos.addAll(minuta.getAnexos().stream().map(this::anexoParametrosAssinatura).toList());
            return arquivos;
        }
    }

    @Deprecated
    private ParametrosEnvioArquivosParaAssinaturaTjOffice anexoParametrosAssinatura(MinutaAnexo doc) {
        if (!doc.temConteudo())
            throw new RuntimeException("Não é possivel assinar anexo que não contem conteudo, porfavor faça o upload do anexo "+ doc.getDescricao());
        String url = getUrlGabinete() + "/anexo/";
        String nome = StringUtils.createNameWithSafeExtension(doc.getDescricao() + " - id " + doc.getId(), "pdf");
        return new ParametrosEnvioArquivosParaAssinaturaTjOffice(nome,url + doc.getId(), doc);
    }

    @Deprecated
    public List<ParametrosEnvioArquivosParaAssinaturaTjOffice> parametrosPedidoInclusaoPautaBuilder(Minuta minuta) throws Exception {
        List<ParametrosEnvioArquivosParaAssinaturaTjOffice> arquivos = new ArrayList<ParametrosEnvioArquivosParaAssinaturaTjOffice>();
        PedidoInclusaoPauta pedidoInclusaoPauta = minuta
            .getPedidoInclusaoPauta()
            .orElseThrow(() -> new Exception("Pedido de inclusão não encontrado"));
        if(!pedidoInclusaoPauta.foiRealizado()) {
            String url = getUrlGabinete() + "/assinatura/stream/"+ minuta.getProcesso().getId().toString() +"/pedido-inclusao-pauta";
            arquivos.add(new ParametrosEnvioArquivosParaAssinaturaTjOffice("Pedido de Pauta - id " + pedidoInclusaoPauta.getId(), url, pedidoInclusaoPauta));
        }
        return arquivos;
    }

    public Boolean assinaturaComErro(String idOJ) {
        Processo processos = processoService.getProcessosPorTarefaLimitOne(TarefaEnum.Assinando,idOJ);
        return processos != null;
    }

    public Minuta documentoAssinarMobile(Processo processo, Usuario usuario) throws Exception {
        List<UsuarioMobileDTO> listMobile = usuarioRemotoService.getDispositivosMobile(usuario.getCpf());

        if (listMobile.size() > 0) {
            Minuta minuta = obterMinutaParaAssinatura(processo, usuario);

            if (minuta.getAnexos().size() > 0) {
                assinarAnexos(minuta, usuario);
            }

            return assinarMinuta(minuta, usuario, processo);
        } else {
            throw new RuntimeException("Não é possível assinar o processo " + processo.getNumeroProcesso() + ". Dispositivos Mobile não vinculados no PJE.");
        }
    }

    private Minuta obterMinutaParaAssinatura(Processo processo, Usuario usuario) throws Exception {
        Minuta minuta = processo.minutaEmElaboracao().orElseThrow(() -> new Exception("Não foi possível encontrar minuta para o processo " + processo.getNumeroProcesso()));
        if(minuta.getMinutaMovimentos() == null || minuta.getMinutaMovimentos().size() <= 0)
            throw new RuntimeException("Não é possivel assinar o processo "+processo.getNumeroProcesso()+". Selecionar um movimento processual.");
        return minuta;
    }

    private void assinarAnexos(Minuta minuta, Usuario usuario) throws Exception {
        for (MinutaAnexo anexo : minuta.getAnexos()) {
            MinutaAnexo minutaAnexo = minutaAnexoService.findOne(anexo.getId());
            byte[] documento = storageService.loadBytes(minutaAnexo.getHash());

            String conteudo = new String(Base64.encodeBase64(documento));

            Assinatura assinatura = realizarAssinatura(conteudo, usuario);

            minutaAnexo.setAssinatura(assinatura);
            minutaAnexo.setTipoAssinatura(TipoAssinatura.Detached);
            minutaAnexoService.save(minutaAnexo);
        }
    }

    private Minuta assinarMinuta(Minuta minuta, Usuario usuario, Processo processo) throws Exception {
        minuta = minutaService.salvaHtmlRenderizado(minuta,usuario,processo,true);

        String conteudo = new String(Base64.encodeBase64(minuta.getMinutaHtmlRenderizadoBytes()));
        Assinatura assinatura = realizarAssinatura(conteudo, usuario);

        minuta.setAssinatura(assinatura);
        minutaService.salvarComVersao(minuta, usuario);
        return minuta;
    }

    private Assinatura realizarAssinatura(String conteudo, Usuario usuario) throws Exception {
        String hash = Sha256Util.sha256(conteudo);
        ResultadoAssinatura res = assinadorA1.assinarHash(hash);
        CadeiaCertificado cadeiaCertificado = cadeiaCertificadoService
            .verificarSeExisteESalvar(Sha256Util.sha256(res.getCadeiaCertificado()), res.getCadeiaCertificado());

        Assinatura assinatura = criarAssinatura(usuario, res, cadeiaCertificado);
        return assinaturaMobileService.save(assinatura);
    }

    private Assinatura criarAssinatura(Usuario usuario, ResultadoAssinatura res, CadeiaCertificado cadeiaCertificado) {
        Assinatura assinatura = new Assinatura();
        assinatura.setAssinatura(res.getAssinatura());
        assinatura.setDataAssinatura(new Date());
        assinatura.setAlgoritmoDigest("SHA256");
        assinatura.setCadeiaCertificado(cadeiaCertificado);
        assinatura.setCpfUsuario(usuario.getCpf());

        return assinatura;
    }

    public boolean processosAssinarMobile(Iterable<Processo> processos, Usuario usuario) {
        boolean sucesso = true;
        for (Processo processo : processos) {
            try {
                processo = assinaturaDetached(processo, usuario);
                if(processo.getTarefa().isEnviaMsgDevolveOrigem())
                    kafkaProducerService.send(DevolveOrigem.TOPIC_DEVOLVE_ORIGEM,processo);
            } catch (Exception e) {
                sucesso = false;
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return sucesso;
    }

    public Processo assinaturaDetached(Processo processo, Usuario usuario) throws Exception {
        Minuta retorno;
            retorno = documentoAssinarMobile(processo, usuario);
        if(retorno!=null)
            return retornoAssinaturaService.verificaEhPreparaPocessoParaVoltarOrigemMobile(retorno,usuario);
        else
            return processo;
    }


}
