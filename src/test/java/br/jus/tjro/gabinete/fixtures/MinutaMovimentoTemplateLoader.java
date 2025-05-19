package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.TipoAnexo;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimento;
import br.jus.tjro.gabinete.model.gab.processo.Processo;

public class MinutaMovimentoTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(MinutaMovimento.class).addTemplate("valid", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
        }});
            }


}
