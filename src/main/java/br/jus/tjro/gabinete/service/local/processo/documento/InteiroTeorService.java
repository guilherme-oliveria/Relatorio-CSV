package br.jus.tjro.gabinete.service.local.processo.documento;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoDocumento;
import br.jus.tjro.gabinete.service.remoto.processo.ProcessoRemotoService;
import br.jus.tjro.gabinete.util.HtmlStringUtil;
import br.jus.tjro.gabinete.util.ParametrosUtil;
import br.jus.tjro.gabinete.util.PdfHelper;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.crypto.BadPasswordException;
import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.css.Rect;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@Service
public class InteiroTeorService {

    @Autowired
    private ProcessoDocumentoService processoDocumentoService;

    @Autowired
    private ParametrosUtil parametro;

    private static final Logger logger = LoggerFactory.getLogger(InteiroTeorService.class);

    public String getDominio() {
        return parametro.getValorString("GABINETE_HOST");
    }

    public byte[] gerarInteroTeor(Processo processo) throws Exception {
        List<ProcessoDocumento> documentos = processoDocumentoService.getDocumentosProcessoNaoExcluidos(processo);
        ProcessoDocumento.ordenaPorDataJuntadaAndNrOrdem(documentos);
        return concatenarListaDocumentosComIndexEhAssinatura(documentos);
    }

    public byte[] mesclarDocumentos(List<Long> idsDocumentos) throws Exception {
        List<ProcessoDocumento> docs = processoDocumentoService.findAll(idsDocumentos);
        ProcessoDocumento.ordenaPorDataJuntadaAndIdLegado(docs);
        return concatenarListaDocumentosComIndexEhAssinatura(docs);
    }

    private byte[] concatenarListaDocumentosComIndexEhAssinatura(List<ProcessoDocumento> documentos) throws Exception {
        ByteArrayOutputStream concatenadoEhComIndex = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(concatenadoEhComIndex);
        PdfDocument pdfDoc = new PdfDocument(writer);

        Map<Integer, String> outlines = new TreeMap<Integer, String>();
        Map<Integer, String> assinatura = new TreeMap<Integer, String>();
        int masterPage = 1;
        for (ProcessoDocumento processoDoc : documentos) {
            PdfDocument pdfDocument = getDocumentoParaPdfDocument(processoDoc);
            int numPages = pdfDocument.getNumberOfPages();
            for (int pageNo = 1; pageNo <= numPages; pageNo++) {
                PdfPage page = pdfDoc.addNewPage();
                Rectangle rect = page.getPageSize();
                if(masterPage == 1) {
                    pdfDoc.setDefaultPageSize(new PageSize(new Rectangle(0, -80, rect.getWidth(), rect.getHeight() + 80)));
                }
                PdfPage docPage = pdfDocument.getPage(pageNo);
                Rectangle orig = pdfDocument.getPage(pageNo).getPageSize();
                PdfFormXObject pageCopy = docPage.copyAsFormXObject(pdfDoc);
                PdfCanvas content = new PdfCanvas(page);
                if (docPage.getPageSize().getWidth() / docPage.getPageSize().getHeight() > 1.2 ) {
                    AffineTransform rotationMatrix = AffineTransform.getRotateInstance(1.57,300,305);
                    content.concatMatrix(rotationMatrix);
                    AffineTransform transformationMatrix = AffineTransform.getScaleInstance(842 / orig.getWidth(),
                        595 / orig.getHeight());
                    content.concatMatrix(transformationMatrix);
                } else {
                    AffineTransform transformationMatrix = AffineTransform.getScaleInstance(595 / orig.getWidth(),
                        842 / orig.getHeight());
                    content.concatMatrix(transformationMatrix);
                }
                String rodape = "" +
                    "ID: " + processoDoc.getIdDocumentoSistemaLegado() + " p. " + pageNo + " de " + pdfDocument.getNumberOfPages() + " em " + processoDoc.getDataJuntadaFormatada() + " " + processoDoc.getNrOrdem() +
                    "\n" +
                    processoDoc.getDsTipoDoc() + ": " + processoDoc.getDescricao() +
                    "\n" + "Juntado por: " + processoDoc.getNomeUserInclusao() +
                    "\n" + "Assinado eletronicamente por: " + processoDoc.getNomeUserAssinatura();
                assinatura.put(masterPage, rodape);
                content.addXObject(pageCopy, 10, 0);
                masterPage++;
            }

            String marcadores = processoDoc.getDsTipoDoc() + " | ID: " + processoDoc.getIdDocumentoSistemaLegado() + " | " + processoDoc.getDataJuntadaFormatada();
            int marcadoresPage = masterPage - numPages;
            outlines.put(marcadoresPage, marcadores);
            pdfDocument.close();
        }

        PdfHelper.createOutlines(outlines, pdfDoc);
        pdfDoc.close();
        ByteArrayInputStream adicionandoAreaAssinatura = new ByteArrayInputStream(concatenadoEhComIndex.toByteArray());
        ByteArrayOutputStream inteiroTeor = new ByteArrayOutputStream();
        PdfReader reader = new PdfReader(adicionandoAreaAssinatura);
        reader.setUnethicalReading(true);
        PdfDocument resize = new PdfDocument(reader, new PdfWriter(inteiroTeor));
        PdfHelper.adicionaRodape(assinatura, resize);
        resize.close();
        return inteiroTeor.toByteArray();
    }

    private PdfDocument getDocumentoParaPdfDocument(ProcessoDocumento doc) throws Exception {
        ByteArrayInputStream docPdfAtual;
        String html;
        try {
            if (doc.isHtml()) {
                html = HtmlStringUtil.verificaImagensHtml(doc.getDocumentoHtml());
                docPdfAtual = new ByteArrayInputStream(Objects.requireNonNull(PdfHelper.htmlToPdf(html, getDominio())));
            } else {
                byte[] pdf = processoDocumentoService.getDocumentoBinario(doc);
                docPdfAtual = new ByteArrayInputStream(pdf);
            }
            PdfReader reader = new PdfReader(docPdfAtual);
            reader.setUnethicalReading(true);
            return new PdfDocument(reader);
        } catch (Throwable e){
            logger.error("Erro ao processar documento ID: " + doc.getIdDocumentoSistemaLegado() + ".</p> "+getTraceId(),e);
            html = "<p style=\"color: red;\">N&atilde;o foi poss&iacute;vel processar o documento ID: " + doc.getIdDocumentoSistemaLegado() + ".</p> "+getTraceId();
            docPdfAtual = new ByteArrayInputStream(Objects.requireNonNull(PdfHelper.htmlToPdf(html, getDominio())));
            return new PdfDocument(new PdfReader(docPdfAtual));
        }
    }

    private String getTraceId(){
        return MDC.get("X-B3-TraceId");
    }

}
