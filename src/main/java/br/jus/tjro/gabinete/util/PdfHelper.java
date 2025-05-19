package br.jus.tjro.gabinete.util;

import br.jus.tjro.gabinete.util.pdf.AccessibilityTagWorkerFactory;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PdfHelper {
    
    private static final Logger LOOGER = LoggerFactory.getLogger(PdfHelper.class);


    public static byte[] htmlToPdf(String html, String url) throws IOException {
        PdfDocument pdfDoc = null;
        try {
            html = removeTagsEhStyleInvalidos(html);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            WriterProperties writerProperties = new WriterProperties();
            writerProperties.addXmpMetadata();
            PdfWriter pdfWriter = new PdfWriter(outputStream, writerProperties);

            pdfDoc = new PdfDocument(pdfWriter);
            pdfDoc.getCatalog().setLang(new PdfString("pt-BR"));
            pdfDoc.setTagged();
            pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));

            ConverterProperties props = new ConverterProperties();
            DefaultTagWorkerFactory tagWorkerFactory = new AccessibilityTagWorkerFactory();
            props.setTagWorkerFactory(tagWorkerFactory);
            props.setBaseUri(url);
            byte[] a = html.getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
            HtmlConverter.convertToPdf(inputStream, pdfDoc, props);
            pdfDoc.close();
            return outputStream.toByteArray();
        } finally {
            if(pdfDoc != null)
                pdfDoc.close();
        }
    }

    public static void createOutlines(Map<Integer, String> outlines, PdfDocument pdfDoc) {
        PdfOutline rootOutLine = pdfDoc.getOutlines(false);
        createOutlines(outlines, rootOutLine);
    }

    public static void createOutlines(Map<Integer, String> outlines, PdfOutline rootOutLine) {
        for (Entry<Integer, String> entry : outlines.entrySet()) {
            PdfOutline outline = rootOutLine.addOutline(entry.getValue());
            outline.addDestination(PdfExplicitDestination.createFit(entry.getKey()));
        }
    }

    public static void concatPdf(PdfDocument origem, PdfDocument destino) {
        int n = origem.getNumberOfPages();
        origem.copyPagesTo(1, n, destino);
    }

    public static void adicionaRodape(Map<Integer, String> assinatura, PdfDocument resize) {
        int n = resize.getNumberOfPages();
        PdfDictionary pageDict;
        PdfArray mediaBox;
        float urx;
        String a = "nada";
        Document doc = new Document(resize);
        for (int i = 1; i <= n; i++) {
            if (assinatura.containsKey(i)) {
                a = assinatura.get(i);
            }
            pageDict = resize.getPage(i).getPdfObject();
            mediaBox = pageDict.getAsArray(PdfName.MediaBox);
            urx = mediaBox.getAsNumber(2).floatValue();
            mediaBox.set(0, new PdfNumber(0));
            Text text = new Text(a);
            doc.add(new Paragraph(text).setFixedPosition(i, 10, -45, urx - 16).setFontSize(8));
        }
    }

    public static boolean verificaPdf(InputStream inputStream) {
        try {
            PdfDocument pdf = new PdfDocument(new PdfReader(inputStream));
            pdf.close();
        } catch (Exception e) {
            LOOGER.error("Erro ao tentar verificar se arquivo é um PDF",e);
            return false;
        }
        return true;
    }

    private static byte[] resizePdf(InputStream inputStream) throws IOException {
        float width = 8.5f * 72;
        float height = 11f * 72;
        float tolerance = 1f;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(inputStream), new PdfWriter(out));

        int n = pdfDoc.getNumberOfPages();
        PdfDictionary page;
        for (int p = 1; p <= n; p++) {
            page = pdfDoc.getPage(p).getPdfObject();
            if (page.getAsNumber(PdfName.UserUnit) == null)
                page.put(PdfName.UserUnit, new PdfNumber(2.5f));
            page.remove(PdfName.Rotate);
        }
        pdfDoc.close();

        return out.toByteArray();
    }

    public static void encapsulaPdfOld(ByteArrayInputStream pdfCorrente, PdfDocument pdfDoc, PdfCanvas canvas)
        throws IOException {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(pdfCorrente));
        PdfFormXObject page = srcDoc.getFirstPage().copyAsFormXObject(pdfDoc);
        canvas.addXObject(page, 0, 0);
        srcDoc.close();
    }

    public static String removeTagsEhStyleInvalidos(String html) {
        org.jsoup.nodes.Document document = Jsoup.parse(html);
        document.select("input, select, button, textarea").remove();
        Elements t = document.select("[style]");
        for( Element elemento : t){
            String stylePropValue = elemento.attr("style");
            stylePropValue = stylePropValue.replaceAll("line-height:[^;'\"]*%([;])?", "");
            elemento.attr("style", stylePropValue);
        }
        return document.toString();
    }
}
