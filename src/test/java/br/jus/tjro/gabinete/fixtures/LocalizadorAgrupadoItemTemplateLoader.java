package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.transiente.LocalizadorAgrupadoItem;

public class LocalizadorAgrupadoItemTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(LocalizadorAgrupadoItem.class).addTemplate("valid", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("nome", "Descricao Nome localizador agrupador item");
            add("tamanho", random(Long.class, range(1L, 5000L)));
            add("idCaixa", random(Long.class, range(1L, 200L)));
        }});

        Fixture.of(LocalizadorAgrupadoItem.class).addTemplate("validPaiId1", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("nome", "Descricao Nome localizador agrupador item");
            add("tamanho", random(Long.class, range(1L, 5000L)));
            add("idCaixa", random(Long.class, 1L));
        }});
    }

}
