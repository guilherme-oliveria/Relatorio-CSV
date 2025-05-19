package br.jus.tjro.gabinete.util;

import org.apache.commons.text.StringEscapeUtils;

public class HtmlUtils {

    public static final String scapeHtmlEnhanced(String html){
        html = StringEscapeUtils.unescapeHtml4(html);
        html = StringEscapeUtils.escapeHtml4(html);
        html = html.replaceAll("&lt;", "<");
        html = html.replaceAll("&gt;", ">");
        html = html.replaceAll("&quot;", "\"");
        html = html.replaceAll("&iquest;", "");
        html = html.replaceAll("\\P{Print}", "");
        return html;
    }

    public static final String escapeHTML(String s) {

        StringBuffer sb = new StringBuffer();

        int n = s.length();

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            switch (c) {

            case '”':
                sb.append("&quote;");
                break;

            case '“':
                sb.append("&quote;");
                break;

            // A
            case 'á':
                sb.append("&aacute;");
                break;
            case 'Á':
                sb.append("&Aacute;");
                break;
            case 'ã':
                sb.append("&atilde;");
                break;
            case 'Ã':
                sb.append("&Atilde;");
                break;
            case 'à':
                sb.append("&agrave;");
                break;
            case 'À':
                sb.append("&Agrave;");
                break;
            case 'â':
                sb.append("&acirc;");
                break;
            case 'Â':
                sb.append("&Acirc;");
                break;
            case 'ä':
                sb.append("&auml;");
                break;
            case 'Ä':
                sb.append("&Auml;");
                break;
            case 'å':
                sb.append("&aring;");
                break;
            case 'Å':
                sb.append("&Aring;");
                break;

            // E
            case 'é':
                sb.append("&eacute;");
                break;
            case 'É':
                sb.append("&Eacute;");
                break;
            case 'ẽ':
                sb.append("&etilde;");
                break;
            case 'Ẽ':
                sb.append("&Etilde;");
                break;
            case 'è':
                sb.append("&egrave;");
                break;
            case 'È':
                sb.append("&Egrave;");
                break;
            case 'ê':
                sb.append("&ecirc;");
                break;
            case 'Ê':
                sb.append("&Ecirc;");
                break;
            case 'ë':
                sb.append("&euml;");
                break;
            case 'Ë':
                sb.append("&Euml;");
                break;

            // case 'å':
            // sb.append("&ering;");
            // break;
            // case 'Å':
            // sb.append("&Ering;");
            // break;

            // I
            case 'í':
                sb.append("&iacute;");
                break;
            case 'Í':
                sb.append("&Iacute;");
                break;
            case 'ĩ':
                sb.append("&itilde;");
                break;
            case 'Ĩ':
                sb.append("&Itilde;");
                break;
            case 'ì':
                sb.append("&igrave;");
                break;
            case 'Ì':
                sb.append("&Igrave;");
                break;
            case 'î':
                sb.append("&icirc;");
                break;
            case 'Î':
                sb.append("&Icirc;");
                break;
            case 'ï':
                sb.append("&iuml;");
                break;
            case 'Ï':
                sb.append("&Iuml;");
                break;

            // case 'å':
            // sb.append("&iring;");
            // break;
            // case 'Å':
            // sb.append("&Iring;");
            // break;

            // O
            case 'ó':
                sb.append("&oacute;");
                break;
            case 'Ó':
                sb.append("&Oacute;");
                break;
            case 'õ':
                sb.append("&otilde;");
                break;
            case 'Õ':
                sb.append("&Otilde;");
                break;
            case 'ò':
                sb.append("&ograve;");
                break;
            case 'Ò':
                sb.append("&Ograve;");
                break;
            case 'ô':
                sb.append("&ocirc;");
                break;
            case 'Ô':
                sb.append("&Ocirc;");
                break;
            case 'ö':
                sb.append("&ouml;");
                break;
            case 'Ö':
                sb.append("&Ouml;");
                break;

            // case 'å':
            // sb.append("&oring;");
            // break;
            // case 'Å':
            // sb.append("&Oring;");
            // break;

            // U
            case 'ú':
                sb.append("&uacute;");
                break;
            case 'Ú':
                sb.append("&Uacute;");
                break;
            case 'ũ':
                sb.append("&utilde;");
                break;
            case 'Ũ':
                sb.append("&Utilde;");
                break;
            case 'ù':
                sb.append("&ugrave;");
                break;
            case 'Ù':
                sb.append("&Ugrave;");
                break;
            case 'û':
                sb.append("&ucirc;");
                break;
            case 'Û':
                sb.append("&Ucirc;");
                break;
            case 'ü':
                sb.append("&uuml;");
                break;
            case 'Ü':
                sb.append("&Uuml;");
                break;
            case '§':
                sb.append("&sect;");
                break;

            case 'ñ':
                sb.append("&ntilde;");
                break;

            case 'Ñ':
                sb.append("&Ntilde;");
                break;

            case '%':
                sb.append("&percnt;");
                break;

            // case 'å':
            // sb.append("&oring;");
            // break;
            // case 'Å':
            // sb.append("&Oring;");
            // break;

            case 'º':
                sb.append("&ordm;");
                break;
            case 'ª':
                sb.append("&ordf;");
                break;

            case 'æ':
                sb.append("&aelig;");
                break;
            case 'Æ':
                sb.append("&AElig;");
                break;
            case 'ç':
                sb.append("&ccedil;");
                break;
            case 'Ç':
                sb.append("&Ccedil;");
                break;

            case 'ø':
                sb.append("&oslash;");
                break;
            case 'Ø':
                sb.append("&Oslash;");
                break;
            case 'ß':
                sb.append("&szlig;");
                break;

            case '®':
                sb.append("&reg;");
                break;
            case '©':
                sb.append("&copy;");
                break;
            case '€':
                sb.append("&euro;");
                break;
            // Substitui no break white space por espaco normal
            case ' ':
                sb.append(" ");
                break;

            default:
                sb.append(c);
                break;
            }
        }
        return sb.toString();
    }
}
