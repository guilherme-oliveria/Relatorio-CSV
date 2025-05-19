package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimento;
import br.jus.tjro.gabinete.model.gab.minuta.PedidoInclusaoPauta;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.TipoAnexo;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;

import java.util.Date;

public class PedidoInclusaoPautaTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(PedidoInclusaoPauta.class).addTemplate("validParaAssinaturaColegiado", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("minuta", one(Minuta.class, "validParaoAssinaturaColegiado"));
            add("htmlRenderizado", random("PedidoInclusaoPauta renderizado"));
            add("hashP7s", null);
            add("dataAssinatura", null);
        }});

        Fixture.of(PedidoInclusaoPauta.class).addTemplate("validParaAssinaturaNaoColegiado", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("minuta", one(Minuta.class, "validParaoAssinaturaNaoColegiado"));
            add("htmlRenderizado", random("PedidoInclusaoPauta renderizado"));
            add("hashP7s", null);
            add("dataAssinatura", null);
        }});

    }


}
