package br.jus.tjro.gabinete.util;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


public class StringUtils {

    private static StringUtils INSTANCE;

    private StringUtils() {
    }

    public static StringUtils getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new StringUtils();
        }

        return INSTANCE;
    }

    public static String createNameWithSafeExtension(String name, String extension) {

        name = StringUtils.getInstance().replaceAllAccents(name);

        // Remove qualquer caracter que nao esteja na lista
        name = name.replaceAll("[^\\w\\d\\-j\\ \\.]", "");

        // Retira os espacos do inicio e do final do texto
        name = name.trim();

        // Substitui espacos e pontos por underline
        name = name.replaceAll("[\\ \\.]", "_");

        return name + "." + extension;
    }

    public static String pluralTipoParte(String tipoParte, Integer count) {
        if (count == 1) return tipoParte;
        HashMap<String, String> dicionario = new HashMap<>();
        dicionario.put("ACUSADO", "ACUSADOS");
        dicionario.put("ADMINISTRADOR JUDICIAL", "JUDICIAIS");
        dicionario.put("ADMINISTRADOR PROVISÓRIO", "PROVISÓRIOS");
        dicionario.put("ADOLESCENTE", "ADOLESCENTES");
        dicionario.put("ADVOGADO", "ADVOGADOS");
        dicionario.put("AGRAVADO", "AGRAVADOS");
        dicionario.put("AGRAVANTE", "AGRAVANTES");
        dicionario.put("AMICUS CURIAE", "AMICUS CURIAE");
        dicionario.put("APELADO", "APELADOS");
        dicionario.put("APELANTE", "APELANTES");
        dicionario.put("ARGUÍDO", "ARGUÍDOS");
        dicionario.put("ARGUINTE", "ARGUINTES");
        dicionario.put("ASSISTENTE", "ASSISTENTES");
        dicionario.put("AUSENTE", "AUSENTES");
        dicionario.put("AUTOR", "AUTORES");
        dicionario.put("AUTOR DO FATO", "AUTORES DOS FATOS");
        dicionario.put("AUTORIDADE", "AUTORIDADES");
        dicionario.put("CESSIONÁRIO", "CESSIONÁRIOS");
        dicionario.put("COMUNICADO", "COMUNICADOS");
        dicionario.put("COMUNICANTE", "COMUNICANTES");
        dicionario.put("CONDENADO", "CONDENADOS");
        dicionario.put("CONSULENTE", "CONSULENTES");
        dicionario.put("CORRIGENTE", "CORRIGENTES");
        dicionario.put("CORRIGIDO", "CORRIGIDOS");
        dicionario.put("CURADOR", "CURADORES");
        dicionario.put("CUSTUS LEGIS", "CUSTUS LEGIS");
        dicionario.put("DENUNCIADO", "DENUNCIADOS");
        dicionario.put("DEPRECADO", "DEPRECADOS");
        dicionario.put("DEPRECANTE", "DEPRECANTES");
        dicionario.put("EMBARGADO", "EMBARGADOS");
        dicionario.put("EMBARGANTE", "EMBARGANTES");
        dicionario.put("ESPÓLIO", "ESPÓLIOS");
        dicionario.put("EXCEPTO", "EXCEPTOS");
        dicionario.put("EXCIPIENTE", "EXCIPIENTES");
        dicionario.put("EXECUTADO", "EXECUTADOS");
        dicionario.put("EXEQUENTE", "EXEQUENTES");
        dicionario.put("EXPROPRIADO", "EXPROPRIADOS");
        dicionario.put("EXPROPRIANTE", "EXPROPRIANTES");
        dicionario.put("FLAGRANTEADO", "FLAGRANTEADOS");
        dicionario.put("HERDEIRO", "HERDEIROS");
        dicionario.put("IMPETRADO", "IMPETRADOS");
        dicionario.put("IMPETRANTE", "IMPETRANTES");
        dicionario.put("IMPUGNADO", "IMPUGNADOS");
        dicionario.put("IMPUGNANTE", "IMPUGNANTES");
        dicionario.put("INDICIADO", "INDICIADOS");
        dicionario.put("INTERESSADO", "INTERESSADOS");
        dicionario.put("INTERPELADO", "INTERPELADOS");
        dicionario.put("INTERPELANTE", "INTERPELANTES");
        dicionario.put("INTÉRPRETE", "INTÉRPRETES");
        dicionario.put("INVENTARIADO", "INVENTARIADOS");
        dicionario.put("INVESTIGADO", "INVESTIGADOS");
        dicionario.put("JUÍZO RECORRENTE", "RECORRENTES");
        dicionario.put("JUSROGANTE", "JUSROGANTES");
        dicionario.put("LITISCONSORTE", "LITISCONSORTES");
        dicionario.put("MASSA FALIDA", "MASSAS FALIDAS");
        dicionario.put("NOTICIADO", "NOTICIADOS");
        dicionario.put("NOTICIANTE", "NOTICIANTES");
        dicionario.put("NOTIFICADO", "NOTIFICADOS");
        dicionario.put("NOTIFICANTE", "NOTIFICANTES");
        dicionario.put("NUNCIADO", "NUNCIADOS");
        dicionario.put("NUNCIANTE", "NUNCIANTES");
        dicionario.put("OPOENTE", "OPOENTES");
        dicionario.put("OPOSTO", "OPOSTOS");
        dicionario.put("ORDENADO", "ORDENADOS");
        dicionario.put("ORDENANTE", "ORDENANTES");
        dicionario.put("PACIENTE", "PACIENTES");
        dicionario.put("PACIENTE / IMPETRANTE", "PACIENTES/IMPETRANTES");
        dicionario.put("PARTE AUTORA", "AUTORAS");
        dicionario.put("PARTE RÉ", "PARTES RÉS");
        dicionario.put("PERITO", "PERITOS");
        dicionario.put("PROCESSADO", "PROCESSADOS");
        dicionario.put("PROCESSANTE", "PROCESSANTES");
        dicionario.put("PROCURADOR", "PROCURADORES");
        dicionario.put("QUERELADO", "QUERELADOS");
        dicionario.put("QUERELANTE", "QUERELANTES");
        dicionario.put("RECLAMADO", "RECLAMADOS");
        dicionario.put("RECLAMANTE", "RECLAMANTES");
        dicionario.put("RECORRENTE", "RECORRENTES");
        dicionario.put("RECORRIDO", "RECORRIDOS");
        dicionario.put("RELATANTE", "RELATANTES");
        dicionario.put("REPRESENTADO", "REPRESENTADOS");
        dicionario.put("REPRESENTANTE", "REPRESENTANTES");
        dicionario.put("REPRESENTANTE/NOTICIANTE", "REPRESENTANTES/NOTICIANTES");
        dicionario.put("REPRESENTANTE PROCESSUAL", "REPRESENTANTES PROCESSUAIS");
        dicionario.put("REQUERENTE", "REQUERENTES");
        dicionario.put("REQUERIDO", "REQUERIDOS");
        dicionario.put("REQUISITANTE", "REQUISITANTES");
        dicionario.put("RESPONSÁVEL", "RESPONSÁVEIS");
        dicionario.put("RÉU", "RÉUS");
        dicionario.put("ROGADO", "ROGADOS");
        dicionario.put("ROGANTE", "ROGANTES");
        dicionario.put("SERVIDOR", "SERVIDORES");
        dicionario.put("SINDICADO", "SINDICADOS");
        dicionario.put("SINDICANTE", "SINDICANTES");
        dicionario.put("SUSCITADO", "SUSCITADOS");
        dicionario.put("SUSCITANTE", "SUSCITANTES");
        dicionario.put("TERCEIRO INTERESSADO", "TERCEIROS INTERESSADOS");
        dicionario.put("TESTEMUNHA", "TESTEMUNHAS");
        dicionario.put("TESTE PJE", "TESTE PJE");
        dicionario.put("TUTOR", "TUTORES");
        return dicionario.getOrDefault(tipoParte, tipoParte);
    }

    /**
     * Substitui o nome em questão pelas suas iniciais
     *
     * @param source o texto a ser convertido pelas iniciais
     * @return as iniciais do texto source
     */
    public static String obtemIniciais(String source) {
        String trimmed = fullTrim(source);
        String[] tokens = trimmed.split(" ");
        StringBuilder sb = new StringBuilder(tokens.length * 3);
        boolean first = true;
        for (String t : tokens) {
            if(t.isEmpty()) continue;
            if (!first) {
                sb.append(" ");
            }
            sb.append(t.toUpperCase().charAt(0));
            sb.append(".");
            first = false;
        }
        return sb.toString();
    }

    /**
     * Substitui todos os espaços múltiplos existentes no interior de um texto
     * por um único espaço.
     *
     * @param source o texto do qual serão suprimidos os caracteres vazios
     *               duplicados
     * @return o texto source, suprimidos os caracteres vazios duplicados
     */
    public static String trimInside(String source) {
        return source.replaceAll("\\b\\s{2,}\\b", " ");
    }

    /**
     * Substitui todos os espaços vazios à direita e à esquerda de um texto
     * dado, assim como aqueles duplicados existentes em seu interior.
     *
     * @param source o texto do qual serão suprimidos os caracteres vazios
     *               supérfluos.
     * @return o texto source, suprimidos os caracteres vazios supérfluos
     */
    public static String fullTrim(String source) {
        return trimInside(trimBorders(source));
    }

    /**
     * Substitui todos os espaços vazios à direita e à esquerda de um texto
     * dado.
     *
     * @param source o texto do qual serão suprimidos os caracteres vazios à
     *               esquerda e à direita.
     * @return o texto source, suprimidos os caracteres vazios à esquerda e à
     * direita.
     */
    public static String trimBorders(String source) {
        return trimLeft(trimRight(source));
    }

    /**
     * Suprime todos os caracteres vazios à esquerda do texto de origem.
     *
     * @param source o texto do qual serão suprimidos os caracteres vazios à
     *               esquerda
     * @return o texto txt, suprimidos os caracteres vazios à esquerda
     */
    public static String trimLeft(String source) {
        return source.replaceAll("^\\s+", "");
    }

    /**
     * Suprime todos os caracteres vazios à direita do texto de origem.
     *
     * @param source o texto do qual serão suprimidos os caracteres vazios à
     *               direita
     * @return o texto source, suprimidos os caracteres vazios à direita
     */
    public static String trimRight(String source) {
        return source.replaceAll("\\s+$", "");
    }

    public static String formatarCpfCnpj(String tipo, String valor) {
        String doc = "";
        if (tipo.equals("CPF")) {
            doc = valor.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        } else if (tipo.equals("CNPJ")) {
            doc = valor.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        }
        return doc;
    }

    public boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public String firstToLowerCase(String str) {
        return !isNullOrEmpty(str) ? str.substring(0, 1).toLowerCase() + str.substring(1) : "";
    }

    public String firstToUpperCase(String str) {
        return !isNullOrEmpty(str) ? str.substring(0, 1).toUpperCase() + str.substring(1) : "";
    }

    public String join(String[] itens, String delimiter) {
        return join(Arrays.asList(itens), delimiter);
    }

    public String join(Collection<String> itens, String delimiter) {

        if (itens == null || itens.isEmpty())
            return "";

        Iterator<String> iter = itens.iterator();

        StringBuilder builder = new StringBuilder(iter.next());

        while (iter.hasNext()) {
            builder.append(delimiter).append(iter.next());
        }

        return builder.toString();
    }

    public String removeAllNonNumeric(String string) {
        if (!isNullOrEmpty(string)) {
            return string.replaceAll("[^0-9]", "");
        }
        return string;
    }

    public String stripSpecialCharacters(String string) {
        if (!isNullOrEmpty(string)) {
            return string.replaceAll("[^0-9a-zA-Z]", "");
        }
        return string;
    }

    public boolean isNullOrEmpty(char[] chars) {
        return (chars == null || chars.length == 0);
    }

    public String replaceAllAccents(String string) {
        if (!isNullOrEmpty(string)) {
            CharSequence cs = new StringBuilder(string);
            return Normalizer.normalize(cs, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        }

        return null;
    }

    public static String getString(Object lol) {
        try {
            return lol.toString();
        }catch (Exception e) {
            return "";
        }
    }

}
