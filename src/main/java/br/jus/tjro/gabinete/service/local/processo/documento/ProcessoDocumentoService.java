package br.jus.tjro.gabinete.service.local.processo.documento;

import br.jus.tjro.gabinete.exceptions.service.local.ProcessoDocumentoServiceException;
import br.jus.tjro.gabinete.interfaces.StorageService;
import br.jus.tjro.gabinete.listener.kafka.consumers.BuscaProcessoExpedienteKafkaListener;
import br.jus.tjro.gabinete.listener.kafka.consumers.BuscaProcessoMovimentoKafkaListener;
import br.jus.tjro.gabinete.listener.kafka.consumers.ThumbnailKafkaListener;
import br.jus.tjro.gabinete.model.gab.ProcessoRequestPage;
import br.jus.tjro.gabinete.model.gab.ResponsePage;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoDocumento;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoDocumentoRepository;
import br.jus.tjro.gabinete.service.remoto.StorageRemotoService;
import br.jus.tjro.gabinete.service.remoto.ProcessoDocumentoRemotoService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import jakarta.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static br.jus.tjro.gabinete.listener.kafka.consumers.BuscaProcessoDocumentoKafkaListener.TOPIC_NAME;

@Service
public class ProcessoDocumentoService {

    private final Logger looger = LoggerFactory.getLogger(ProcessoDocumentoService.class);
    private final KafkaProducerService kafkaProducerService;
    private final ProcessoDocumentoRepository repository;
    private final StorageService storageService;
    private final ProcessoDocumentoRemotoService processoDocumentoRemotoService;

    @Autowired
    public ProcessoDocumentoService(KafkaProducerService kafkaProducerService,
                                    ProcessoDocumentoRemotoService processoDocumentoRemotoService,
                                    ProcessoDocumentoRepository repository,
                                    StorageService storageService){
        this.kafkaProducerService = kafkaProducerService;
        this.processoDocumentoRemotoService = processoDocumentoRemotoService;
        this.repository = repository;
        this.storageService = storageService;
    }

    public ProcessoDocumento findOne(Long id) {
        ProcessoDocumento processoDocumento = repository.findById(id).orElseThrow(NullPointerException::new);// ToDo tratar a falta desse recurso
        return processoDocumento;
    }

    public List<ProcessoDocumento> findAll(List<Long> idsDocumentos) {
        return Lists.newArrayList(repository.findAllById(idsDocumentos));
    }

    @PostAuthorize("validaProcessoAcessado(#processo)")
    public Page<ProcessoDocumento> getDocumentosPaginadaProcesso(Processo processo, Pageable pageable)
        throws Exception {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "dataJuntada","idDocumentoSistemaLegado"));
        Page<ProcessoDocumento> documentos = repository.findByProcesso(processo, pageable);

        if (documentos == null || documentos.getTotalElements() < 1) {
            this.kafkaProducerService.send(TOPIC_NAME, new ProcessoRequestPage(new Processo(processo.getId(), processo.getSistema(), processo.getIdProcessoSistemaLegado()), 0));
        }
        return documentos;
    }

    @Transactional
    public ProcessoDocumento save(ProcessoDocumento processoDocumento) throws Exception {
        return repository.save(processoDocumento);
    }

    public byte[] getDocumentoBinario(ProcessoDocumento documento) throws Exception {
        if (documento.getHash() == null) {
            identificaTipo(documento);
            return repository.save(documento).getAnexo();
        }else {
            try {
                return getDocumentoPdf(documento.getHash());
            } catch (Exception e) {
                looger.error(e.getMessage(),e);
                identificaTipo(documento);
                documento = repository.save(documento);

                return getDocumentoPdf(documento.getHash());
            }
        }
    }

    private byte[] getDocumentoPdf(String hash) throws Exception {
        return storageService.loadBytes(hash);
    }

    public byte[] getDocumentoPdf(String hash, Long id) throws Exception {
        try {
            return getDocumentoPdf(hash);
        } catch (Exception e) {
            looger.error(e.getMessage(),e);
            return getDocumentoBinario(
                repository.findById(id).orElseThrow(NullPointerException::new) // ToDo tratar a falta desse recurso
            );
        }
    }

    public ProcessoDocumento identificaTipo(ProcessoDocumento documento) {
        if (documento.getDocumentoHtml() != null) {
            documento.setExtensao("text/html");
        } else if (documento.getExtensao() != null && documento.getExtensao().toUpperCase().contains("PDF")) {
            documento.setExtensao("application/pdf");
        }
        if (documento.getNomeUserInclusao() == null)
            documento.setNomeUserInclusao("Não informado no PJE");
        return documento;
    }


    public List<ProcessoDocumento> getDocumentosProcesso(Processo processo) throws Exception {
        List<ProcessoDocumento> documentos = repository.findByProcesso(processo);
        if (documentos == null || documentos.size() < 1) {
            importaOuAtualizaDocumentosProcesso(processo,0);
            documentos = repository.findByProcesso(processo);
        }
        return documentos;
    }

    public List<ProcessoDocumento> getDocumentosProcessoNaoExcluidos(Processo processo) throws Exception {
        return this.getDocumentosProcesso(processo).stream()
            .filter(processoDocumento -> processoDocumento.getAtivo())
            .collect(Collectors.toList());
    }

    public List<ProcessoDocumento> importaOuAtualizaDocumentosProcesso(Processo processo, int pagina) throws ProcessoDocumentoServiceException {
        ResponsePage<ProcessoDocumento> documentosRemotosPage = null;
        try {
            documentosRemotosPage = processoDocumentoRemotoService.getDocumentosProcesso(processo, pagina);
        } catch (HttpServerErrorException h ){
            throw new ProcessoDocumentoServiceException("Não foi possivel buscar os documentos na fonte dados remoto: "
                + processo.getNumeroProcesso() + " ID: " + processo.getId()+". O servidor diz: "+h.getResponseBodyAsString(),h);
        } catch (Exception e) {
            throw new ProcessoDocumentoServiceException("Não foi possivel buscar os documentos na fonte dados remoto: "
                + processo.getNumeroProcesso() + " ID: " + processo.getId(),e);
        }

        int quantidadeDocumentosLocais = repository.countByProcesso(processo).intValue();

        /*if (verificaIntegridadeDocumentosLocaisEhRemoto(documentosRemotosPage, quantidadeDocumentosLocais))
            throw new ProcessoDocumentoServiceException("Há inconsistências nas quantidades de arquivos do processo: "
                + processo.getNumeroProcesso() + " ID: " + processo.getId());*/

        List<ProcessoDocumento> documentosImportados = new ArrayList<>();

        ProcessoDocumento processoDocumento;
        for (ProcessoDocumento doc : documentosRemotosPage.getContent()) {
            try {
                processoDocumento = repository.findFirstByProcessoAndIdDocumentoSistemaLegado(processo, doc.getIdDocumentoSistemaLegado());
                if(processoDocumento == null) {
                   processoDocumento = new ProcessoDocumento();
                }
                processoDocumento.setIdDocumentoSistemaLegado(String.valueOf(doc.getId()));
                processoDocumento.setProcesso(processo);
                processoDocumento.setDescricao(doc.getDescricao());
                processoDocumento.setDocumentoHtml(doc.getDocumentoHtml());
                processoDocumento.setDataJuntada(doc.getDataJuntada());
                processoDocumento.setNomeUserInclusao(doc.getNomeUserInclusao());
                processoDocumento.setEhSigiloso(doc.getEhSigiloso());
                processoDocumento.setExtensao(doc.getExtensao());
                processoDocumento.setTipoDocumento(doc.getTipoDocumento());
                processoDocumento.setIdUsuarioExclusao(doc.getIdUsuarioExclusao());
                processoDocumento.setMotivoExclusao(doc.getMotivoExclusao());
                processoDocumento.setAtivo(doc.getAtivo());
                processoDocumento.setNrOrdem(doc.getNrOrdem());
                processoDocumento.setHash(doc.getHash());
                documentosImportados.add(identificaTipo(processoDocumento));
            }catch(NullPointerException nullEx) {
                looger.error("Uma das props do doc está nula! Id doc Legado: " + doc.getId(), nullEx);
            } catch (Exception e) {
                looger.error("Erro ao salvar documentos no storage. Causa: "+e.getMessage(),e);
            }
        }
        Iterable<ProcessoDocumento> documentosSalvos = repository.saveAll(documentosImportados);
        List<Long> listaIdsSalvos = StreamSupport.stream(documentosSalvos.spliterator(), false)
            .map(ProcessoDocumento::getId).collect(Collectors.toList());
        kafkaProducerService.send(ThumbnailKafkaListener.TOPIC_NAME,listaIdsSalvos);
        if(!documentosRemotosPage.isLast())
            return this.importaOuAtualizaDocumentosProcesso(processo, pagina+1);
        return documentosImportados;
    }

    private List<ProcessoDocumento> verificaQuaisDocumentosDevemSerImportados(ResponsePage<ProcessoDocumento> documentosRemotos,
                                                                              List<ProcessoDocumento> documentosLocais) {
        Set<String> idsLocais = documentosLocais.stream().map((ProcessoDocumento::getIdDocumentoSistemaLegado))
            .collect(Collectors.toSet());
        // remove da lista de documentos remotos os já existentes localmente
        List<ProcessoDocumento> documentosParaImportar = documentosRemotos.getContent().stream()
            .filter(docR -> !idsLocais.contains(docR.getId().toString()))
            .collect(Collectors.toList());
        return documentosParaImportar;
    }

    private boolean verificaIntegridadeDocumentosLocaisEhRemoto(ResponsePage<ProcessoDocumento> documentosRemotos,
                                                                int quantidadeLocais) throws ProcessoDocumentoServiceException {
        if (documentosRemotos.getTotalElements() < quantidadeLocais)
            throw new ProcessoDocumentoServiceException("Há inconsistências nas quantidades de arquivos do processo");
        return false;
    }

    public ProcessoDocumento getDocumentoProcessoAndIdLegadoEhImportaOuAtualiza(String idDocumentoLegado, Processo processo) throws ProcessoDocumentoServiceException {
        ProcessoDocumento processoDocumento = repository.findFirstByProcessoAndIdDocumentoSistemaLegado(processo,idDocumentoLegado);
        if (processoDocumento == null) {
            //modificar para importar documento especifico
            importaOuAtualizaDocumentosProcesso(processo,0);
            processoDocumento = repository.findFirstByProcessoAndIdDocumentoSistemaLegado(processo,idDocumentoLegado);
        }
        if (processoDocumento == null)
            throw new ProcessoDocumentoServiceException("Não foi possível encontrar o documento do processo. " + "Id documento legado "
                + idDocumentoLegado + " Id Processo " + processo.getId());

        return processoDocumento;
    }

}
