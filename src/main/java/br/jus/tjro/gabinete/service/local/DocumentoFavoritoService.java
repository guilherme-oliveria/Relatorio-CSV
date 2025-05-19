package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.processo.DocumentoFavorito;
import br.jus.tjro.gabinete.repository.gab.processo.DocumentoFavoritoRepository;
import br.jus.tjro.gabinete.service.local.processo.documento.ProcessoDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentoFavoritoService {

    @Autowired
    private DocumentoFavoritoRepository documentoFavoritoRepository;

    @Autowired
    private ProcessoDocumentoService processoDocumentoService;

    @Transactional
    public DocumentoFavorito salvar(DocumentoFavorito documentoFavorito, Long idDocumento) {

        Optional<DocumentoFavorito> docuRecuperado = documentoFavorito.getId() != null ? documentoFavoritoRepository.findById(documentoFavorito.getId()) : Optional.empty();

        if (docuRecuperado.isPresent()) {
            docuRecuperado.get().setComentario(documentoFavorito.getComentario());
            return documentoFavoritoRepository.save(docuRecuperado.get());
        } else {
            documentoFavorito.setDocumento(processoDocumentoService.findOne(idDocumento));
            return documentoFavoritoRepository.save(documentoFavorito);
        }
    }

    public List<DocumentoFavorito> buscaDocumetosFavoritoPorProcesso(Long idProcesso) {
        List<DocumentoFavorito> documentosFavoritados = documentoFavoritoRepository.findByIdProcessoOrderByDocumentoDataJuntadaDesc(idProcesso);
        return documentosFavoritados;
    }

    public DocumentoFavorito buscaDocumetosFavoritoPorProcesso(Long idProcesso, Long idDocumento) {
        DocumentoFavorito documento = documentoFavoritoRepository.findByIdProcessoAndIdDocumento(idProcesso, idDocumento);
        return documento;
    }

    @Transactional
    public void desfavoritarDocumento(Long idDocumentoFavorito) {
        Optional<DocumentoFavorito> documento = documentoFavoritoRepository
            .findById(idDocumentoFavorito);

        documento.ifPresent(documentoFavorito -> documentoFavoritoRepository.delete(documentoFavorito));
    }

    @Transactional
    public DocumentoFavorito salvarComentarioFavorito(Long idFavoritoBanco, String comentario) {
        DocumentoFavorito documentoFavorito = documentoFavoritoRepository.findById(idFavoritoBanco).orElseThrow(NullPointerException::new); //ToDo tratar quando não existir o recurso
        documentoFavorito.setComentario(comentario);
        return documentoFavoritoRepository.save(documentoFavorito);
    }

}
