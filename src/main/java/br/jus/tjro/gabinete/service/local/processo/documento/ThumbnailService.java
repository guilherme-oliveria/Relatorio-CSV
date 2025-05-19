package br.jus.tjro.gabinete.service.local.processo.documento;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.interfaces.StorageService;
import br.jus.tjro.gabinete.listener.kafka.consumers.ThumbnailKafkaListener;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoDocumento;
import br.jus.tjro.gabinete.service.remoto.Doc2ImageService;

import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class ThumbnailService {
    
    @Autowired
    private Doc2ImageService doc2ImageService;

    @Autowired
    private ProcessoDocumentoService processoDocumentoService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public byte[] getThumbnail(String hash, long idDocumento) throws ServicoRemotoException {
        try{
            return storageService.loadBytes(hash);
        }catch (Exception e){
            publicaMsg(idDocumento);
            throw new ServicoRemotoException("Erro ao recuperar miniaturas",e);
        }
    }

    public byte[] getThumbnail(long idDocumento) throws ServicoRemotoException {
        try{
            ProcessoDocumento doc = processoDocumentoService.findOne(idDocumento);
            return storageService.loadBytes(doc.getHashThumbnail());
        }catch (Exception e){
            publicaMsg(idDocumento);
            throw new ServicoRemotoException("Erro ao recuperar miniaturas",e);
        }
    }

    private void publicaMsg(long idDocumento){
        var lista = List.of(idDocumento);
        kafkaProducerService.send(ThumbnailKafkaListener.TOPIC_NAME,lista);
    }

    @Transactional
    public byte[] criaEhSalvaThumbnail(Long idDocumento) throws Exception {
        ProcessoDocumento doc = processoDocumentoService.findOne(idDocumento);
        return criaEhSalvaThumbnail(doc);
    }

    @Transactional
    public byte[] criaEhSalvaThumbnail(ProcessoDocumento doc) throws Exception {
        byte[] pdf;
        byte[] imagem;
        if (doc.isHtml()) {
            imagem = doc2ImageService.html2Image(doc.getDocumentoHtml());
        } else {
            pdf = processoDocumentoService.getDocumentoBinario(doc);
            if(pdf == null)
                return null;
            StringBuilder extensao = new StringBuilder();
            byte[] thumbnail = doc2ImageService.binario2Image(pdf, extensao);
            doc.setExtensao(extensao.toString());
            imagem = thumbnail;
        }
        String hash = storageService.store(imagem);
        doc.setHashThumbnail(hash);
        processoDocumentoService.save(doc);
        return imagem;
    }

}
