package br.jus.tjro.gabinete.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HtmlUtilsTest {
    @Test
    public void removeCaractereCodificacao(){
        String entrada = "a\u0081m";
        String clean = HtmlUtils.scapeHtmlEnhanced(entrada);
        assertEquals("am",clean);
    }

    @Test
    public void removeInterregacaoInvertido(){
        String entrada = "Caractere interrogacao invertido¿¿¿";
        String clean = HtmlUtils.scapeHtmlEnhanced(entrada);
        assertEquals("Caractere interrogacao invertido",clean);
    }

    @Test
    public void substituiLtGt(){
        String entrada = "&lt;Tribunal&gt;";
        String clean = HtmlUtils.scapeHtmlEnhanced(entrada);
        assertEquals("<Tribunal>",clean);
    }

    @Test
    public void substituiEscapeHtml(){
        String entrada = "§ 2º, do Código Civil";
        String clean = HtmlUtils.escapeHTML(entrada);
        assertEquals("&sect; 2&ordm;, do C&oacute;digo Civil",clean);
    }
}
