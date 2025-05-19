package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.model.gab.alvara.Alvara;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimento;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.TipoAnexo;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;

public class MinutaTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(Minuta.class).addTemplate("validParaoAssinatura", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("minutaHtml", random("Minuta antes de renderizar"));
            add("minutaHtmlRenderizado", random("Minuta renderizado"));
            add("hashP7s", null);
            add("processo", one(Processo.class, "validAssinar"));
            add("minutaMovimentos",  has(1).of(MinutaMovimento.class, "valid"));
        }});

        Fixture.of(Minuta.class).addTemplate("validComAlvara", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("minutaHtml", random("Minuta antes de renderizar"));
            add("minutaHtmlRenderizado", random("Minuta renderizado"));
            add("hashP7s", null);
            add("processo", one(Processo.class, "validAssinar"));
            add("minutaMovimentos",  has(1).of(MinutaMovimento.class, "valid"));
            add("alvara",  one(Alvara.class, "valid"));
        }});

        Fixture.of(Minuta.class).addTemplate("validParaoAssinaturaColegiado", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("minutaHtml", random("Minuta antes de renderizar"));
            add("minutaHtmlRenderizado", random("Minuta renderizado"));
            add("hashP7s", null);
            add("processo", one(Processo.class, "validAssinar"));
            add("minutaMovimentos",  has(1).of(MinutaMovimento.class, "valid"));
            add("tipoDocumento",  one(TipoDocumento.class, "colegiado"));
        }});

        Fixture.of(Minuta.class).addTemplate("validParaoAssinaturaNaoColegiado", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("minutaHtml", random("Minuta antes de renderizar"));
            add("minutaHtmlRenderizado", random("Minuta renderizado"));
            add("hashP7s", null);
            add("processo", one(Processo.class, "validAssinar"));
            add("minutaMovimentos",  has(1).of(MinutaMovimento.class, "valid"));
            add("tipoDocumento",  one(TipoDocumento.class, "valido"));
        }});


        Fixture.of(Minuta.class).addTemplate("validParaoAssinaturaEhUmAnexo", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("minutaHtml", random("Minuta antes de renderizar"));
            add("minutaHtmlRenderizado", random("Minuta renderizado"));
            add("hashP7s", null);
            add("processo", one(Processo.class, "validAssinar"));
            add("anexos", has(1).of(MinutaAnexo.class, "validSemAssinatura"));
            add("minutaMovimentos",  has(1).of(MinutaMovimento.class, "valid"));
        }});

        Fixture.of(Minuta.class).addTemplate("validMinutarSemAssinatura", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("minutaHtml", random("Minuta antes de renderizar"));
            add("minutaHtmlRenderizado", random("Minuta renderizado"));
            add("hashP7s", null);
            add("processo", one(Processo.class, "validMinutar"));
            add("minutaMovimentos",  has(1).of(MinutaMovimento.class, "valid"));
        }});

        Fixture.of(Minuta.class).addTemplate("validMinutarSemAssinatura", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("minutaHtml", random("Minuta antes de renderizar"));
            add("minutaHtmlRenderizado", random("Minuta renderizado"));
            add("hashP7s", null);
            add("processo", one(Processo.class, "validMinutar"));
            add("minutaMovimentos",  has(1).of(MinutaMovimento.class, "valid"));
        }});

        Fixture.of(Minuta.class).addTemplate("validMinutarComAssinatura", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("minutaHtml", random("Minuta antes de renderizar"));
            add("minutaHtmlRenderizado", random("Minuta renderizado"));
            add("hashP7s", random("assinaturaHash"));
            add("processo", one(Processo.class, "validMinutar"));
            add("minutaMovimentos",  has(1).of(MinutaMovimento.class, "valid"));
        }});

        Fixture.of(Minuta.class).addTemplate("validMinutarSemAssinaturaComAnexoSemAssinatura", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("minutaHtml", random("Minuta antes de renderizar"));
            add("minutaHtmlRenderizado", random("Minuta renderizado"));
            add("hashP7s", null);
            add("processo", one(Processo.class, "validMinutar"));
            add("anexos", has(1).of(MinutaAnexo.class, "validSemAssinatura"));
            add("minutaMovimentos",  has(1).of(MinutaMovimento.class, "valid"));
        }});

        Fixture.of(Minuta.class).addTemplate("validMinutarComAssinaturaComAnexoSemAssinatura", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("minutaHtml", random("Minuta antes de renderizar"));
            add("minutaHtmlRenderizado", random("Minuta renderizado"));
            add("hashP7s", "assinatura");
            add("processo", one(Processo.class, "validMinutar"));
            add("anexos", has(1).of(MinutaAnexo.class, "validSemAssinatura"));
            add("minutaMovimentos",  has(1).of(MinutaMovimento.class, "valid"));
        }});

        Fixture.of(Minuta.class).addTemplate("validMinutarComAssinaturaComAnexoComAssinatura", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("minutaHtml", "Minuta antes de renderizar");
            add("minutaHtmlRenderizado", "Minuta renderizado");
            add("hashP7s", "assinatura");
            add("processo", one(Processo.class, "validMinutar"));
            add("anexos", has(1).of(MinutaAnexo.class, "validComAssinatura"));
            add("minutaMovimentos",  has(1).of(MinutaMovimento.class, "valid"));
        }});

        Fixture.of(Minuta.class).addTemplate("validMinutarAnexoComEhSemAssinar", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("minutaHtml", "Minuta antes de renderizar");
            add("minutaHtmlRenderizado", "Minuta renderizado");
            add("hashP7s", "assinatura");
            add("processo", one(Processo.class, "validMinutar"));
            add("minutaMovimentos",  has(1).of(MinutaMovimento.class, "valid"));
            add("anexos", has(4).of(MinutaAnexo.class,
                "validComAssinatura",
                "validComAssinatura",
                "validComAssinatura",
                "validSemAssinatura"));
        }});

        Fixture.of(Minuta.class).addTemplate("validMinutarComAssinaturaComAnexosComAssinatura", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("minutaHtml", "Minuta antes de renderizar");
            add("minutaHtmlRenderizado", "Minuta renderizado");
            add("hashP7s", "assinatura");
            add("minutaMovimentos",  has(1).of(MinutaMovimento.class, "valid"));
            add("processo", one(Processo.class, "validMinutar"));
            add("anexos", has(4).of(MinutaAnexo.class,
                "validComAssinatura",
                "validComAssinatura",
                "validComAssinatura",
                "validComAssinatura"));
        }});

        Fixture.of(Minuta.class).addTemplate("invalidNaoConcluso", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("minutaHtml", "Minuta antes de renderizar");
            add("minutaHtmlRenderizado", "Minuta renderizado");
            add("hashP7s", "assinatura");
            add("processo", one(Processo.class, "validNaoConcluso"));
        }});

        Fixture.of(Minuta.class).addTemplate("validoProcessoDocumento", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("minutaHtml", "Minuta antes de renderizar");
            add("minutaHtmlRenderizado", random("Minuta renderizado"));
            add("hashP7s", null);
            add("processo", one(Processo.class, "validAssinar"));
            add("tipoAnexo", one(TipoAnexo.class, "valid"));
        }});

    }


}
