package br.jus.tjro.gabinete.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlStringUtil {

    public static String conveteUrlRelativeAbsolute(String html, String URL) {
        Document doc = Jsoup.parse(html, URL);

        Elements select = doc.select("a");
        for (Element e : select) {
            // baseUri will be used by absUrl
            String absUrl = e.absUrl("href");
            e.attr("href", absUrl);
        }

        // now we process the imgs
        select = doc.select("img");
        for (Element e : select) {
            if (!(e.toString().indexOf("base64") > 1))
                e.attr("src", e.absUrl("src"));
            else
                e.attr("src", "");
        }

        return doc.toString();
    }

    /**
     * Remove imagens inconsistentes da String passada por parmetro.
     *
     * @param html String do HTML.
     * @return html String do HTML sem imagens inconsistentes.
     * @throws PdfException
     */
    public static String verificaImagensHtml(String html) {
//		html = html.replaceAll("src=\"(?<=\\bsrc=\")http[^\"]*\"", "");
//		html = html.replaceAll("src=\"(?<=\\bsrc=\")http[^\"]*\"", "");
//		html = html.replaceAll("src=\"(?<=\\bsrc=\")[^\"]*\"", "");
        html = html.replaceAll("<img .*src=\"(?!data:image).*\" />", "");

//        html = html.replaceAll("text-indent:(?:(.*?);)", "");
//		Pattern patternImg = Pattern.compile("src=\"(?<=\\bsrc=\")http[^\"]*\"", Pattern.CASE_INSENSITIVE);
//		Matcher matcherImg = patternImg.matcher(html);
//		List<String> srcsSani = new ArrayList<String>();
//		matcherImg.replaceAll("");
        return html;
    }

}
