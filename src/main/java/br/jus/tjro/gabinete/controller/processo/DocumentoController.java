package br.jus.tjro.gabinete.controller.processo;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoDocumento;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoDocumentoRepository;
import br.jus.tjro.gabinete.service.local.ProcessoService;
import br.jus.tjro.gabinete.service.local.processo.documento.InteiroTeorService;
import br.jus.tjro.gabinete.service.local.processo.documento.ProcessoDocumentoService;
import br.jus.tjro.gabinete.service.local.processo.documento.ThumbnailService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("documentos")
public class DocumentoController {

    @Autowired
    private ProcessoService processoService;

    @Autowired
    private ProcessoDocumentoService processoDocumentoService;

    @Autowired
    private ProcessoDocumentoRepository repository;

    @Autowired
    private InteiroTeorService inteiroTeorService;

    @Autowired
    private ThumbnailService thumbnail;

    @GetMapping("/paginada/processo/{idProcesso}")
    @Transactional(readOnly = true)
    public ResponseEntity<Page<ProcessoDocumento>> documentoPaginadaProcesso(Pageable pageable,
                                                                             @PathVariable("idProcesso") Long idProcesso) throws Exception {
        Processo processo = processoService.findOne(idProcesso);
        Page<ProcessoDocumento> documentosPaginadaProcesso = processoDocumentoService
            .getDocumentosPaginadaProcesso(processo, pageable);

        return documentosPaginadaProcesso.getSize() > 1 ? ResponseEntity.ok(documentosPaginadaProcesso)
            : ResponseEntity.noContent().build();
    }

    @GetMapping("/processo/{idProcesso}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<ProcessoDocumento>> documentosProcesso(@PathVariable("idProcesso") Long idProcesso) throws Exception {
        return ResponseEntity.ok(repository.findByProcesso(new Processo(idProcesso)));
    }

    @GetMapping("/meta-dados/{idDocumento}")
    @Transactional(readOnly = true)
    public ResponseEntity<ProcessoDocumento> metaDados(@PathVariable("idDocumento") Long idDocumento) {
        return ResponseEntity.ok().body(repository.findById(idDocumento).orElseThrow(()->new NullPointerException("O documento não existe")));
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public void documento(HttpServletResponse response, @PathVariable("id") Long id) throws Exception {
        ProcessoDocumento doc = repository.findById(id).orElseThrow(()->new NullPointerException("O documento não existe"));
        if (doc.getDocumentoHtml() != null) {
            response.setContentType("text/html");
            response.getWriter().println(doc.getDocumentoHtml());
            response.flushBuffer();
        } else {
            response.setContentType(doc.getExtensao());
            responsePdf(response, processoDocumentoService.getDocumentoBinario(doc));
        }
    }

    @GetMapping("{id}/hash/{hash}")
    public void documentoHash(HttpServletResponse response, @PathVariable("hash") String hash, @PathVariable("id") Long id) throws Exception {
        byte[] doc = processoDocumentoService.getDocumentoPdf(hash, id);
        responsePdf(response, doc);
    }

    @GetMapping("/intero-teor/{idProcesso}")
    public ResponseEntity<Resource> interoTeor(@PathVariable("idProcesso") long idProcesso) throws Exception {
        Processo processo = processoService.findOne(idProcesso);
        String nomeArquivo = processo.getNumeroProcesso() + " - Inteiro Teor" + ".pdf";
        ByteArrayResource resource = new ByteArrayResource(inteiroTeorService.gerarInteroTeor(processo));
        return ResponseEntity.ok().header("arquivo", nomeArquivo).contentType(MediaType.APPLICATION_PDF).body(resource);
    }

    @PostMapping("/mesclar")
    public ResponseEntity<Resource> mesclarDocumentos(@RequestBody List<Long> idsDocumentos) throws Exception {
        ByteArrayResource resource = new ByteArrayResource(inteiroTeorService.mesclarDocumentos(idsDocumentos));
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(resource);
    }

    @GetMapping("/thumbnail/{idProcessoDocumento}")
    @Transactional(readOnly = true)
    public ResponseEntity<Resource> recuperarThumbnail(@PathVariable("idProcessoDocumento") Long idDocumento) throws ServicoRemotoException {
        byte[] imagem = thumbnail.getThumbnail(idDocumento);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
            .body(new InputStreamResource(new ByteArrayInputStream(imagem)));
    }

    @GetMapping("/thumbnail/{id}/hash/{hash}")
    @Transactional(readOnly = true)
    public ResponseEntity<Resource> recuperarThumbnailHash(@PathVariable("id") Long id, @PathVariable("hash") String hash) throws ServicoRemotoException {
        byte[] imagem = thumbnail.getThumbnail(hash, id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
            .body(new InputStreamResource(new ByteArrayInputStream(imagem)));
    }

    private void responsePdf(HttpServletResponse response, byte[] data) throws IOException {
        response.setContentType("application/pdf");
        InputStream is = new ByteArrayInputStream(data);
        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
    }

    @GetMapping("/processo/{idProcesso}/{idsDocumentos}")
    @Transactional(readOnly = true)
    public List<String> getIdsDocumentosByIdProcessoEIdsDocumentos(@PathVariable("idProcesso") Long idProcesso,
                                                                   @PathVariable("idsDocumentos") String idsDocumentos) throws Exception {
        Processo processo = processoService.findOne(idProcesso);

        List<String> listaIdsDocumentos = List.of(idsDocumentos.split(","));
        List<ProcessoDocumento> procDocumentos = this.repository.findByProcessoAndIdDocumentoSistemaLegadoIn(processo, listaIdsDocumentos);
        List<String> listaRetornoIds = new ArrayList<String>();

        for (ProcessoDocumento procdoc: procDocumentos) {
            listaRetornoIds.add(procdoc.getIdDocumentoSistemaLegado());
        }

        return listaRetornoIds;
    }

}
