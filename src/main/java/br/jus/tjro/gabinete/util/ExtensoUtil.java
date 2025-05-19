/**
 * Copyright (c) 2012 Carlos Romel Pereira da Silva <carlos.romel@gmail.com>
 * <p>
 * Classe que transcreve um valor monetário para o equivalente em extenso.
 */
package br.jus.tjro.gabinete.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class ExtensoUtil {
    private static final String CONECTOR = " e ";
    private static final String SEPARADOR = ", ";
    private static final String QUALIFICADOR = " de";

    private final static String[][] DECIMAIS = {
        {"cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos"},
        {"dez", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa"},
        {"um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove",
            "onze", "doze", "treze", "quatorze", "quinze", "dezesseis", "dezessete", "dezoito", "dezenove"}
    };

    private final static String[][] MULTIPLOS = {
        {"vigesilhão", "vigesilhões"},
        {"novedecilhão", "novedecilhões"},
        {"octodecilhão", "octodecilhões"},
        {"setedecilhão", "setedecilhões"},
        {"sexdecilhão", "sexdecilhões"},
        {"quindecilhão", "quindecilhões"},
        {"quatordecilhão", "quatordecilhões"},
        {"tredecilhão", "tredecilhões"},
        {"duodecilhão", "duodecilhões"},
        {"undecilhão", "undecilhões"},
        {"decilhão", "decilhões"},
        {"nonilhão", "nonilhões"},
        {"octilhão", "octilhões"},
        {"septilhão", "septilhões"},
        {"sextilhão", "sextilhões"},
        {"quintilhão", "quintilhões"},
        {"quatrilhão", "quatrilhões"},
        {"trilhão", "trilhões"},
        {"bilhão", "bilhões"},
        {"milhão", "milhões"},
        {"mil", "mil"},
        {"real", "reais"},
        {"centavo", "centavos"}
    };

    private static final int CASAS = MULTIPLOS.length * 3;
    private static final int CENTAVOS = MULTIPLOS.length - 1;
    private static final int MOEDA = MULTIPLOS.length - 2;
    private static final int MILHAR = MULTIPLOS.length - 3;

    private BigDecimal valor;

    public ExtensoUtil() {
    }

    public ExtensoUtil(BigDecimal valor) {
        this.valor = valor;
    }

    public ExtensoUtil(Double valor) {
        this(new BigDecimal(valor));
    }

    public ExtensoUtil(Float valor) {
        this(new BigDecimal(valor));
    }

    public ExtensoUtil(Integer valor) {
        this(new BigDecimal(valor));
    }

    public static String data(Date data, boolean comDiaDaSemana) {

        // PEGO AQUI A DATA ATUAL
        Date dataAtual = data;

        // CRIO AQUI UM FORMATADOR
        // PASSANDO UM ESTILO DE FORMATAÇÃO : DateFormat.FULL
        // E PASSANDO UM LOCAL DA DATA : new Locale("pt", "BR")
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));

        // FORMATO A DATA, O FORMATADOR ME RETORNA
        // A STRING DA DATA FORMATADA
        String dataExtenso = formatador.format(dataAtual);
        if (comDiaDaSemana)
            return dataExtenso;
        else {
            // AQUI É CASO VOCÊ QUEIRA TIRAR
            // O DIA DA SEMANA QUE APARECE NO
            // PRIMEIRO EXEMPLO
            int index = dataExtenso.indexOf(",");
            int lenght = dataExtenso.length();
            return dataExtenso.substring(++index, lenght);
        }

    }

    public static String hora(Date hora) {
//        DateFormat formato = new SimpleDateFormat("hh:mm");
//        formato.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        Integer h = hora.getHours();
        Integer m = hora.getMinutes();

        String lbHora = (h > 1 || h == 0) ? "horas" : "hora";
        String lbMinutos = (m > 1 || m == 0) ? "minutos" : "minutos";

        return h + " " + lbHora + " e " + m + " " + lbMinutos;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setValor(Double valor) {
        this.valor = new BigDecimal(valor);
    }

    public void setValor(Float valor) {
        this.valor = new BigDecimal(valor);
    }

    public void setValor(Integer valor) {
        this.valor = new BigDecimal(valor);
    }

    @Override
    public String toString() {
        final String formatado = String.format("%0" + ExtensoUtil.CASAS + ".2f", valor).replace(",", "0").replace(".", "0");
        final int centavos = Integer.parseInt(formatado.substring(formatado.length() - 3));
        boolean casoEspecialMil = false;
        int ultimaGrandeza = 0;
        String extenso = "";
        String classe;
        int proximos;
        int valorAbsoluto;
        int grau;

        for (int grandeza = 0; grandeza < MULTIPLOS.length; ++grandeza) {
            classe = formatado.substring(grandeza * 3, grandeza * 3 + 3);

            if (Integer.parseInt(classe) > 0) {
                casoEspecialMil = casoEspecialMil || Integer.parseInt(classe) == 1 && (grandeza == MILHAR);
                grau = Integer.parseInt(classe) > 1 ? 1 : 0;
                ultimaGrandeza = grandeza;

                if (!extenso.isEmpty()) {
                    extenso += (grandeza == CENTAVOS || (casoEspecialMil && grandeza == MOEDA && centavos == 0)) ? CONECTOR : SEPARADOR;
                }

                for (int valorRelativo = 0; valorRelativo < classe.length(); ++valorRelativo) {
                    valorAbsoluto = Integer.parseInt(classe.substring(valorRelativo, valorRelativo + 1));

                    if (valorAbsoluto > 0) {
                        proximos = (valorRelativo < 2) ? Integer.parseInt(classe.substring(valorRelativo + 1, 3)) : 0;
                        if ("100".equals(classe)) {
                            extenso += "cem";
                            valorRelativo += 2;
                        } else if (extenso.isEmpty() && casoEspecialMil) {
                            /*
                             * NOTA: No caso específico de 1.000,
                             *       diz-se: "mil reais", ao invés do usual
                             *       esperado: "um mil reais".
                             */
                        } else if (valorAbsoluto == 1 && valorRelativo == 1 && proximos > 0) {
                            extenso += DECIMAIS[++valorRelativo][proximos + 8];
                        } else {
                            extenso += DECIMAIS[valorRelativo][valorAbsoluto - 1] + (proximos > 0 ? CONECTOR : "");
                        }
                    }
                }

                extenso += (extenso.isEmpty() ? "" : " ") + MULTIPLOS[grandeza][grau];
            } else if (!extenso.isEmpty() && (grandeza == MOEDA)) {
                grau = ((grandeza < CENTAVOS) || Integer.parseInt(classe) > 1) ? 1 : 0;
                extenso += (ultimaGrandeza != (MILHAR) ? QUALIFICADOR : "") + " " + MULTIPLOS[grandeza][grau];
            }
        }

        return extenso;
    }

    public String toStringFormatted() {
        String extenso = toString();

        return Character.toUpperCase(extenso.charAt(0)) + extenso.substring(1) + ".";
    }
}
