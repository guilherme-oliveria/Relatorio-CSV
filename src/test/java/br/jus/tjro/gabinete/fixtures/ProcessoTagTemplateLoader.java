package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoAssunto;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoTag;
import br.jus.tjro.gabinete.model.gab.tag.Tag;
import br.jus.tjro.gabinete.model.gab.tpu.TpuClasse;

import java.text.SimpleDateFormat;

public class ProcessoTagTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(Tag.class).addTemplate("valid", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("orgaoJulgador", 666l);
            add("CorHexadecimal", "#666");
            add("tag", random("endereço"));
        }});

        Fixture.of(ProcessoTag.class).addTemplate("valid", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("tag", one(Tag.class, "valid"));
        }});
    }
}
