package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.pessoa.PessoaDocumento;


public class PessoaDocumentoTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(PessoaDocumento.class).addTemplate("valido", new Rule() {{
            add("idDocumentoLegado", random(Long.class, range(1L, 200L)));
            add("idPessoaLegado", random(Long.class, range(1L, 200L)));
            add("tipoDocumento", random("AAA", "ZZZ"));
            add("documento", random("AAAAAAAAAAAAAAAAAAA", "ZZZZZZZZZZZZZZZZZZZ"));
        }});
    }
}
