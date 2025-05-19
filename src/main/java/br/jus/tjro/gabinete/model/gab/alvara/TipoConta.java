package br.jus.tjro.gabinete.model.gab.alvara;

import java.util.*;

public enum TipoConta {
    CORRENTE_PESSOA_FISICA("001", "Corrente Pessoa Física"),
    CORRENTE_PESSOA_JURIDICA("003", "Corrente Pessoa Jurídica"),
    POUPANCA_PESSOA_FISICA("013", "Poupança Pessoa Física"),
    POUPANCA_PESSOA_JURIDICA("022", "Poupança Pessoa Jurídica"),
    JUDICIAL_ESTADUAL("040.1", "Judicial Estadual"),
    JUDICIAL_FEDERAL("040.2", "Judicial Federal"),
    JUDICIAL_TRABALHISTA("040.3", "Judicial Trabalhista"),
    JURIDICA_ORGAO_PUBLICO("006", "Jurídica Órgão Público");

    private String codigo;
    private final String descricao;

    private static final TreeMap<String, String> LISTA_COM_CODIGO = new TreeMap<>();

    TipoConta(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static List<TipoConta> getLista() {
        return Arrays.asList(CORRENTE_PESSOA_FISICA,
            CORRENTE_PESSOA_JURIDICA, POUPANCA_PESSOA_FISICA, POUPANCA_PESSOA_JURIDICA, JUDICIAL_ESTADUAL,
            JUDICIAL_FEDERAL, JUDICIAL_TRABALHISTA, JURIDICA_ORGAO_PUBLICO);
    }

    public static TreeMap<String, String> getListaComCodigo() {
        for (TipoConta e : getLista()) {
            LISTA_COM_CODIGO.put(e.codigo, e.descricao);
        }
        return LISTA_COM_CODIGO;
    }

    public static TipoConta getTIpoContaPeloCodigo(String codigo) {
        for (TipoConta e : values()) {
            if (e.codigo.equals(codigo))
                return e;
        }
        return null;
    }

    public String toString() {
        return descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }


}
