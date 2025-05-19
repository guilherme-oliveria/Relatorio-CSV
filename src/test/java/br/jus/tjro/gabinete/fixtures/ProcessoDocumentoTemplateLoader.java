package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoDocumento;

import java.text.SimpleDateFormat;


public class ProcessoDocumentoTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(ProcessoDocumento.class).addTemplate("valido", new Rule() {{
            add("id", random(Long.class, "1111111", "9999999"));
            add("idDocumentoSistemaLegado", random(Long.class, "1111111", "9999999"));
            add("documentoHtml", random(
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ"));
            add("dataJuntada", randomDate(
                "1900-01-01", "2017-12-31", new SimpleDateFormat("yyyy-MM-dd")));
            add("nomeUserInclusao", random(
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ"));
            add("ehSigiloso", random(false, true));
            add("tipoDocumento", one(TipoDocumento.class, "valido"));
            add("extensao", random("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ"));
            // Long idUsuarioExclusao, String motivoExclusao, Date dataExclusao, Boolean ativo
            add("idUsuarioExclusao",  random(Long.class, "1111111", "9999999"));
            add("motivoExclusao", random(
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ"));
            add("dataExclusao", randomDate(
                "1900-01-01", "2017-12-31", new SimpleDateFormat("yyyy-MM-dd")));
            add("ativo", random(false, true));
            add("nrOrdem",0);
        }});
    }
}
