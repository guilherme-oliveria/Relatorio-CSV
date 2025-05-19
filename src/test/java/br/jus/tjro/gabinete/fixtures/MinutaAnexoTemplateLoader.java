package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.model.gab.processo.Processo;

public class MinutaAnexoTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(MinutaAnexo.class).addTemplate("valid", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("descricao", "Descricao Minuta Anexo");
        }});

        Fixture.of(MinutaAnexo.class).addTemplate("validSemAssinatura", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("hash", "StorageHash");
            add("hashP7s", null);
            add("descricao", "Descricao Minuta Anexo");
        }});

        Fixture.of(MinutaAnexo.class).addTemplate("validComAssinatura", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("hash", "StorageHash");
            add("hashP7s", "comAssinatura");
            add("descricao", "Descricao Minuta Anexo");
            add("tipoDocumento", new TipoDocumento("666","tipoDocumento",true,false));
            add("minutaPai",one(Minuta.class, "validMinutarComAssinatura"));
        }});
    }

}
