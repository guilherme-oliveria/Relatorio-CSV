package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.enums.TipoLocalizadorAgrupadorEnum;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorAgrupador;
import br.jus.tjro.gabinete.model.gab.transiente.LocalizadorAgrupadoItem;

public class LocalizadorAgrupadorLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(LocalizadorAgrupador.class).addTemplate("valid", new Rule() {{
            add("id", random(Long.class, range(1L, 5000L)));
            add("nome", "Descricao Nome Caixa");
            add("tipoLocalizadoresAgrupadorEnum", TipoLocalizadorAgrupadorEnum.Manifestacao);
            add("localizadoresAgrupadoItem", has(1).of(LocalizadorAgrupadoItem.class, "validPaiId1"));
            add("tamanho", random(Long.class, range(1L, 5000L)));
        }});

        Fixture.of(LocalizadorAgrupador.class).addTemplate("validManifestacaoId1", new Rule() {{
            add("id", 1l);
            add("nome", "Descricao Nome Caixa");
            add("tipoLocalizadoresAgrupadorEnum", TipoLocalizadorAgrupadorEnum.Manifestacao);
            add("localizadoresAgrupadoItem", has(1).of(LocalizadorAgrupadoItem.class, "validPaiId1"));
            add("tamanho", random(Long.class, range(1L, 5000L)));
        }});
    }

}
