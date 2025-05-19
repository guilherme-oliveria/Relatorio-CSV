package br.jus.tjro.gabinete.model.gab.localizador;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Regra {

    private final String parametro;
    private final String operador;
    private final List<Object> valores;

    public Regra(@JsonProperty("parametro") String parametro,
                 @JsonProperty("operador") String operador,
                 @JsonProperty("valor") Object valores){
        this.parametro = parametro;
        this.operador = operador;
        this.valores = new ArrayList<>();
        this.valores.add(valores);
    }

    public String getParametro() {
        return parametro;
    }

    public String getOperador() {
        return operador;
    }

    public List<Object> getValores() {
        return valores;
    }
}
