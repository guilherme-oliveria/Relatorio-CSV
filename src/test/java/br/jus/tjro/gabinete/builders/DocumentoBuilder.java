package br.jus.tjro.gabinete.builders;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;

public class DocumentoBuilder {

    private Minuta documento;

    private DocumentoBuilder() {
    }

    public static DocumentoBuilder umDocumento(Processo p) {
        DocumentoBuilder builder = new DocumentoBuilder();
        builder.documento = new Minuta();
        builder.documento.setMinutaHtml("conteudo");
        builder.documento.setProcesso(p);
        return builder;
    }

    public DocumentoBuilder setProcesso(Processo p) {
        documento.setProcesso(p);
        return this;
    }

    public Minuta build() {
        return documento;
    }
}
