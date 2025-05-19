package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.alvara.Alvara;
import br.jus.tjro.gabinete.model.gab.alvara.PagamentoAlvara;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import io.vavr.collection.List;
import java.time.LocalDateTime;

public class AlvaraTemplateLoader implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(Alvara.class).addTemplate("valid", new Rule() {{
            add("minuta", one(Minuta.class, "validMinutarSemAssinatura"));
            add("dataCadastro", LocalDateTime.now());
            add("pagamentoAlvara", has(1).of(PagamentoAlvara.class, "valid"));
            add("requisitante", name());
            add("cpfRequisitante", "666");
            add("ativo", true);
            add("processoId", random(Long.class, 111L, 99999L));
        }});
    }
}
