package br.jus.tjro.gabinete.service.assinaturadigital;

import br.jus.tjro.assinaturadigital.utils.PKCS7Utils;
import br.jus.tjro.assinaturadigital.utils.vo.PKCS7;
import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.exceptions.AssinaturaException;
import br.jus.tjro.gabinete.interfaces.StorageService;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.enums.TipoAssinatura;
import br.jus.tjro.gabinete.model.gab.enums.TipoDocumentoAssinatura;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.model.gab.minuta.PedidoInclusaoPauta;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.PedidoInclusaoPautaRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.service.local.*;
import org.bouncycastle.cms.CMSSignerDigestMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class RetornoAssinaturaService {

    private final ProcessoService processoService;
    private final MinutaService minutaService;
    private final MinutaAnexoService minutaAnexoService;
    private final PedidoInclusaoPautaRepository pedidoInclusaoPautaRepository;
    private final boolean validaAssinatura;
    private final PKCS7Utils pkcs7Util;
    private final StorageService storageService;
    private final ValidadorAssinaturaDigitalService assinaturaDigitalService;
    private final Logger looger = LoggerFactory.getLogger(AssinaturaService.class);
    private final ProcessosRepository processosRepository;
    private final MinutaTarefaLogService minutaTarefaLogService;

    @Autowired
    public RetornoAssinaturaService(ProcessoService processoService,MinutaService minutaService, MinutaAnexoService minutaAnexoService,
                                    PedidoInclusaoPautaRepository pedidoInclusaoPautaRepository,
                                    ProcessosRepository processosRepository,
                                    @Value("${assinatura.digital.validar:true}") boolean validaAssinatura, StorageService storageService, ValidadorAssinaturaDigitalService assinaturaDigitalService, MinutaTarefaLogService minutaTarefaLogService) {
        this.minutaService = minutaService;
        this.processoService = processoService;
        this.minutaAnexoService = minutaAnexoService;
        this.pedidoInclusaoPautaRepository = pedidoInclusaoPautaRepository;
        this.validaAssinatura = validaAssinatura;
        this.storageService = storageService;
        this.assinaturaDigitalService = assinaturaDigitalService;
        this.minutaTarefaLogService = minutaTarefaLogService;
        this.pkcs7Util = PKCS7Utils.getInstance();
        this.processosRepository = processosRepository;
    }

    public RetornoAssinaturaService(ProcessoService processoService,MinutaService minutaService, MinutaAnexoService minutaAnexoService,
                                    PedidoInclusaoPautaRepository pedidoInclusaoPautaRepository,
                                    ProcessosRepository processosRepository,
                                    @Value("${assinatura.digital.validar:true}") boolean validaAssinatura,
                                    StorageService storageService,
                                    ValidadorAssinaturaDigitalService assinaturaDigitalService,
                                    MinutaTarefaLogService minutaTarefaLogService,PKCS7Utils pkcs7Util) {
        this.minutaService = minutaService;
        this.processoService = processoService;
        this.minutaAnexoService = minutaAnexoService;
        this.pedidoInclusaoPautaRepository = pedidoInclusaoPautaRepository;
        this.validaAssinatura = validaAssinatura;
        this.storageService = storageService;
        this.assinaturaDigitalService = assinaturaDigitalService;
        this.minutaTarefaLogService = minutaTarefaLogService;
        this.pkcs7Util = pkcs7Util;
        this.processosRepository = processosRepository;
    }

    @Transactional
    public Processo assinaturaDetached(Long id, MultipartFile assinatura, TipoDocumentoAssinatura tipoDocumento, Usuario usuario) throws Exception {
        Minuta retorno;
        if (tipoDocumento == TipoDocumentoAssinatura.Principal)
            retorno = assinaMinuta(id, assinatura);
        else if (tipoDocumento == TipoDocumentoAssinatura.Anexo)
            retorno = assinaMinutaAnexo(id, assinatura).getMinutaPai();
        else if (tipoDocumento == TipoDocumentoAssinatura.PedidoInclusaoPauta) {
            PedidoInclusaoPauta pedidoInclusaoPauta = assinaPedidoInclusaoEmPauta(id, assinatura);
            return verificaEhPreparaPocessoParaVoltarOrigem(pedidoInclusaoPauta, usuario);
        }
        else
            throw new Exception("Tipo de objeto invalido para assinatura");
        return verificaEhPreparaPocessoParaVoltarOrigem(retorno,usuario);
    }

    private Minuta assinaMinuta(Long id, MultipartFile assinatura) throws Exception {
        try{
            Minuta documento = minutaService.findOne(id);
            Usuario usuario = new Usuario("windson","cpf",new ArrayList<>(),"token");
            byte[] assinaturaAttached = converterParaAtachado(documento.getMinutaHtmlRenderizadoBytes(),
                assinatura.getBytes());
            validaAssinatura(assinaturaAttached);
            if (assinatura.isEmpty())
                throw new AssinaturaException("A assinatura não esta disponivel para ser movida ao Storage");
            documento.setHashP7s(storageService.store(assinatura));

            return minutaService.salvarComVersao(documento,usuario);
        }catch (Exception e){
            Processo processo = minutaService.findOne(id).getProcesso();
            looger.info("Devolvendo processo para assinar apos ocorrer um erro na assinatura "+processo.getNumeroProcesso(),e);
            processo.setTarefa(TarefaEnum.Assinar);
            processosRepository.save(processo);
            throw e;
        }
    }
    private PedidoInclusaoPauta assinaPedidoInclusaoEmPauta(Long id, MultipartFile assinatura) throws Exception {
        PedidoInclusaoPauta pedidoInclusaoPauta = pedidoInclusaoPautaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Pedido de inclusão em pauta não encontrado ao assinar"));

        try{
            if(!pedidoInclusaoPauta.getMinuta().getTipoDocumento().isMinutaColegiado()) {
                throw new Exception("A minuta deve ser um acórdão");
            }
            String referenciaStorage = storageService.store(assinatura);

            pedidoInclusaoPauta.setHashP7s(referenciaStorage);
            byte[] assinaturaAttached = converterParaAtachado(pedidoInclusaoPauta.getHtmlRenderizadoBytes(), assinatura.getBytes());
            validaAssinatura(assinaturaAttached);
            if (assinatura.isEmpty())
                throw new AssinaturaException("A assinatura não esta disponivel para ser movida ao Storage");
            pedidoInclusaoPauta.setDataAssinatura(new Date());
            return pedidoInclusaoPautaRepository.save(pedidoInclusaoPauta);
        }catch (Exception e){
            Processo processo = pedidoInclusaoPauta.getMinuta().getProcesso();
            looger.info("Devolvendo processo para assinar apos ocorrer um erro na assinatura "+processo.getNumeroProcesso(),e);
            processo.setTarefa(TarefaEnum.Assinar);
            processosRepository.save(processo);
            throw e;
        }
    }

    private MinutaAnexo assinaMinutaAnexo(Long id, MultipartFile assinatura) throws Exception {
        try{
            MinutaAnexo documentoBin = minutaAnexoService.findOne(id);
            byte[] documento = storageService.loadBytes(documentoBin.getHash());
            byte[] assinaturaAttached = converterParaAtachado(documento, assinatura.getBytes());
            validaAssinatura(assinaturaAttached);
            if (assinatura.isEmpty())
                throw new AssinaturaException("A assinatura não esta disponivel para ser movida ao Storage");
            documentoBin.setHashP7s(storageService.store(assinatura));
            documentoBin.setTipoAssinatura(TipoAssinatura.Cms);
            return minutaAnexoService.save(documentoBin);
        }catch (Exception e){
            Processo processo = minutaAnexoService.findOne(id).getMinutaPai().getProcesso();
            looger.info("Devolvendo processo para assinar apos ocorrer um erro na assinatura "+processo.getNumeroProcesso(),e);
            processo.setTarefa(TarefaEnum.Assinar);
            processosRepository.save(processo);
            throw e;
        }
    }

    public Processo verificaEhPreparaPocessoParaVoltarOrigem(PedidoInclusaoPauta pedidoInclusaoPauta, Usuario usuario) throws Exception {
        Processo processo = pedidoInclusaoPauta.getMinuta().getProcesso();
        if (verificaSePermitePelaTarefaAssinatura(processo.getTarefa())) {
            if (pedidoInclusaoPauta.isAssinado())
                return retornoOriginPauta(processo,usuario);
        }
        return processo;
    }

    public Processo verificaEhPreparaPocessoParaVoltarOrigem(Minuta documento, Usuario usuario) throws Exception {
        if (verificaSePermitePelaTarefaAssinatura(documento.getProcesso().getTarefa())) {
            iniciaAssinatura(documento.getProcesso(),usuario);
            if (verificaMinutaTemTodosDocumentosAssinados(documento))
                return retornarOrigem(documento.getProcesso(),usuario);
        }
        return documento.getProcesso();
    }

    public Processo verificaEhPreparaPocessoParaVoltarOrigemMobile(Minuta documento, Usuario usuario) throws Exception {
        if (verificaSePermitePelaTarefaAssinatura(documento.getProcesso().getTarefa())) {
            iniciaAssinatura(documento.getProcesso(),usuario);
            if (verificaMinutaTemTodosDocumentosAssinadosMobile(documento))
                return retornarOrigem(documento.getProcesso(),usuario);
        }
        return documento.getProcesso();
    }

    private Boolean verificaSePermitePelaTarefaAssinatura(TarefaEnum tarefa) {
        return tarefa == TarefaEnum.Assinar || tarefa == TarefaEnum.Minutar || tarefa == TarefaEnum.Assinando
            || tarefa == TarefaEnum.Corrigir || tarefa == TarefaEnum.Revisar;
    }


    private void iniciaAssinatura(Processo processo, Usuario usuario) throws Exception {
        processo.assinando();
        minutaTarefaLogService.registrarLog(processo,usuario);
        processosRepository.save(processo);
    }

    private Processo retornarOrigem(Processo processo, Usuario usuario) throws Exception {
        processo.paraIntegracao();
        minutaTarefaLogService.registrarLog(processo,usuario);
        return processosRepository.save(processo);
    }

    private Processo retornoOriginPauta(Processo processo, Usuario usuario) throws Exception {
        processo.paraIntegracaoPauta();
        minutaTarefaLogService.registrarLog(processo,usuario);
        return processosRepository.save(processo);
    }

    private boolean verificaMinutaTemTodosDocumentosAssinados(Minuta documento) {
        List<MinutaAnexo> anexos = minutaAnexoService.getAnexosByminutaPai(documento);
        if (documento.getHashP7s() == null)
            return false;
        if (anexos != null && anexos.size() > 0)
            for (MinutaAnexo anexo : anexos)
                if (anexo.getHashP7s() == null)
                    return false;

        return true;
    }

    private boolean verificaMinutaTemTodosDocumentosAssinadosMobile(Minuta documento) {
        List<MinutaAnexo> anexos = minutaAnexoService.getAnexosByminutaPai(documento);
        if (documento.getAssinatura() == null)
            return false;
        if (anexos != null && anexos.size() > 0)
            for (MinutaAnexo anexo : anexos)
                if (anexo.getAssinatura() == null)
                    return false;

        return true;
    }

    public byte[] converterParaAtachado(byte[] mensagemOriginal, byte[] detachado) throws IOException {
        return pkcs7Util.converterParaAtachado(mensagemOriginal, detachado);
    }

    public byte[] converterParaAtachado(PedidoInclusaoPauta pauta) throws Exception {
        byte[] mensagemOriginal = pauta.getHtmlRenderizadoBytes();
        byte[] detachado = storageService.loadBytes(pauta.getHashP7s());
        return converterParaAtachado(mensagemOriginal, detachado);
    }

    public byte[] converterParaAtachado(MinutaAnexo anexo) throws Exception {
        byte[] mensagemOriginal;
        if(anexo.getMinutaPai().getTipoDocumento().isMinutaColegiado()){
            return anexo.getHtml().getBytes(StandardCharsets.UTF_8);
        }else if(anexo.getAssinatura()!=null){
            return storageService.loadBytes(anexo.getHash());
        }else{
            mensagemOriginal = storageService.loadBytes(anexo.getHash());
        }
        byte[] detachado = storageService.loadBytes(anexo.getHashP7s());
        return converterParaAtachado(mensagemOriginal, detachado);
    }

    public void validaAssinatura(byte[] bs) throws AssinaturaException {
        PKCS7 pkcs7 = this.pkcs7Util.parse(bs);
        try {
            if (this.validaAssinatura)
                assinaturaDigitalService.validarAssinaturaDigital(pkcs7.getSignedData());
        } catch (Exception t) {
            looger.error(t.getMessage(),t);
            if (t.getClass() == CMSSignerDigestMismatchException.class)
                throw new AssinaturaException("Erro ao realizar a assinatura, a assinatura não corresponde com a mensagem",t);
            else
                throw new AssinaturaException("Erro ao realizar a assinatura. Causa: "+t.getMessage(),t);
        }
    }

    public Boolean removerAssinatura(Long idProcesso,Usuario usuario) throws Exception {
        Optional<Processo> processoEntity = processosRepository.findById(idProcesso);
        try {
            if(processoEntity.isPresent()){
                Processo processo = processoEntity.get();
                processo.setTarefa(TarefaEnum.Minutar);

                Minuta minuta = minutaService.pegaUltimaMinutaByProcesso(processoEntity.get().getId());
                minuta.setHashP7s(null);

                minutaService.salvarComVersao(minuta,usuario);
                processoService.save(processo);
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            throw e;
        }
    }
}
