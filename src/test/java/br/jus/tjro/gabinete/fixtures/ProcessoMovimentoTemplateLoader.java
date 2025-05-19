package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoMovimento;

import java.text.SimpleDateFormat;


public class ProcessoMovimentoTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(ProcessoMovimento.class).addTemplate("valido", new Rule() {{
            add("movimento", random(Long.class, "1111111", "9999999"));
            add("data_atualizacao", randomDate(
                "1900-01-01", "2017-12-31", new SimpleDateFormat("yyyy-MM-dd")));
            add("descricao", random(
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ"));
            add("in_visibilidade_externa", random(false, true));
            add("in_ativo", random(false, true));
            add("id_orgao_julgador", random(Integer.class, "1111111", "9999999"));
            add("id_orgao_julgador_colegiado", random(Integer.class, "1111111", "9999999"));
            add("nome_usuario", random(
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ"));
            add("cpf_usuario", random(
                "AAAAAAAAAAAAA", "ZZZZZZZZZZZZZ"));
        }});
    }
}
