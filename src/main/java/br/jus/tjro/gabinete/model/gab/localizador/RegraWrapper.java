package br.jus.tjro.gabinete.model.gab.localizador;


import java.util.Arrays;
import java.util.List;

public class RegraWrapper implements Comparable<RegraWrapper> {

    private String tipo;
    private String operador;
    private String valor;
    private TpuWrapper tpu;

    public RegraWrapper() {}

    public RegraWrapper(String tipo, String operador, String valor) {
        this.tipo = tipo;
        this.operador = operador;
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public TpuWrapper getTpu() {
        return tpu;
    }

    public void setTpu(TpuWrapper tpu) {
        this.tpu = tpu;
    }

    public boolean validar() {
        List<String> filtros = Arrays.asList(
            "possuiLiminar",
            "prioridade",
            "segredoJustica",
            "justicaGratuita",
            "assuntoPrincipal",
            "idClasseJudicial",
            "movimentos",
            "numeroProcesso",
            "valorCausa",
            "caixa.id",
            "tags.tag.id",
            "tarefaEnum",
            "processoPartes.pessoa.nome",
            "dataEntrada",
            "prioridades.id",
            "processoAssuntos.idAssunto");
        if (!filtros.contains(tipo)) {
            return false;
        }
        if (operador == null || operador.isEmpty()) {
            return false;
        }
        return valor != null && !valor.isEmpty();
    }


    @Override
    public int compareTo(RegraWrapper regra) {
        return this.tipo.compareTo(regra.tipo);
    }
}


