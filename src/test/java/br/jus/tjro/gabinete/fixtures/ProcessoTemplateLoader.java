package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.builders.OrgaoJulgadorBuilder;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoAssunto;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoTag;
import br.jus.tjro.gabinete.model.gab.tpu.TpuClasse;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import static org.springframework.boot.context.properties.bind.Bindable.listOf;

public class ProcessoTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(TpuClasse.class).addTemplate("valid", new Rule() {{
            add("descricao", random("endereço"));
        }});

        Fixture.of(OrgaoJulgador.class).addTemplate("valid", new Rule(){{
            add("id", "PJEPG-5");
            add("descricao","4ª Vara do Windson");
            add("enderecos",new ArrayList());
            add("sigla","ABC");
            add("orgaosJulgadoresRevisores",new ArrayList());
        }});

        Fixture.of(Processo.class).addTemplate("valid", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("orgaoJulgadorObj", OrgaoJulgadorBuilder.umOrgaoJulgador());
            add("numeroProcesso", "7");
            add("assuntoPrincipal", "Assunto Gadelha");
            add("valorCausa", random(Double.class, range(776.66, 1000.90)));
            add("dataUltimaDistribuicao", randomDate("2011-04-15", "2011-11-07", new SimpleDateFormat("yyyy-MM-dd")));
            add("tpuClasse", one(TpuClasse.class, "valid"));
            add("processoPartes", has(1).of(ProcessoParte.class, "valido"));
            add("processoAssuntos", has(1).of(ProcessoAssunto.class, "valido"));
            add("caixa", one(Caixa.class, "valid"));
        }});

        Fixture.of(Processo.class).addTemplate("validCom3Tags", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("orgaoJulgadorObj", OrgaoJulgadorBuilder.umOrgaoJulgador());
            add("numeroProcesso", "666");
            add("assuntoPrincipal", "Assunto Gadelha");
            add("valorCausa", random(Double.class, range(776.66, 1000.90)));
            add("dataUltimaDistribuicao", randomDate("2011-04-15", "2011-11-07", new SimpleDateFormat("yyyy-MM-dd")));
            add("tpuClasse", one(TpuClasse.class, "valid"));
            add("processoPartes", has(1).of(ProcessoParte.class, "valido"));
            add("processoAssuntos", has(1).of(ProcessoAssunto.class, "valido"));
            add("caixa", one(Caixa.class, "valid"));
            add("tags", has(3).of(ProcessoTag.class, "valid"));
        }});

        Fixture.of(Processo.class).addTemplate("validMinutar", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("orgaoJulgadorObj", OrgaoJulgadorBuilder.umOrgaoJulgador());
            add("numeroProcesso", "7");
            add("assuntoPrincipal", "Assunto Gadelha");
            add("valorCausa", random(Double.class, range(776.66, 1000.90)));
            add("dataUltimaDistribuicao", randomDate("2011-04-15", "2011-11-07", new SimpleDateFormat("yyyy-MM-dd")));
            add("tpuClasse", one(TpuClasse.class, "valid"));
            add("tarefaEnum", TarefaEnum.Minutar);
            add("idProcessoSistemaLegado", 300l);
        }});

        Fixture.of(Processo.class).addTemplate("validAssinar", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("orgaoJulgadorObj", OrgaoJulgadorBuilder.umOrgaoJulgador());
            add("numeroProcesso", "42");
            add("assuntoPrincipal", "Assunto Gadelha");
            add("valorCausa", random(Double.class, range(776.66, 1000.90)));
            add("dataUltimaDistribuicao", randomDate("2011-04-15", "2011-11-07", new SimpleDateFormat("yyyy-MM-dd")));
            add("tpuClasse", one(TpuClasse.class, "valid"));
            add("tarefaEnum", TarefaEnum.Assinar);
        }});

        Fixture.of(Processo.class).addTemplate("validNaoConcluso", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("orgaoJulgadorObj",OrgaoJulgadorBuilder.umOrgaoJulgador());
            add("numeroProcesso", "666");
            add("assuntoPrincipal", "Assunto Windson");
            add("valorCausa", random(Double.class, range(776.66, 1000.90)));
            add("dataUltimaDistribuicao", randomDate("2011-04-15", "2011-11-07", new SimpleDateFormat("yyyy-MM-dd")));
            add("tpuClasse", one(TpuClasse.class, "valid"));
            add("tarefaEnum", TarefaEnum.NaoConcluso);
        }});

        Fixture.of(Processo.class).addTemplate("paraImportacao", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("orgaoJulgadorObj", OrgaoJulgadorBuilder.umOrgaoJulgador());
            add("numeroProcesso", "666");
            add("assuntoPrincipal", "Assunto Windson");
            add("valorCausa", random(Double.class, range(776.66, 1000.90)));
            add("dataUltimaDistribuicao", randomDate("2011-04-15", "2011-11-07", new SimpleDateFormat("yyyy-MM-dd")));
            add("tpuClasse", one(TpuClasse.class, "valid"));
            add("processoPartes", has(1).of(ProcessoParte.class, "valido"));
            add("processoAssuntos", has(1).of(ProcessoAssunto.class, "valido"));
            add("caixa", one(Caixa.class, "valid"));
        }});
    }
}
