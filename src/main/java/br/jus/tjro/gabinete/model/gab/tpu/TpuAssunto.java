package br.jus.tjro.gabinete.model.gab.tpu;

public class TpuAssunto extends Tpu {

    public TpuAssunto() {
        this.resourceName = "assuntos";
    }

    public TpuAssunto(Long codigo) {
        super(codigo);
        resourceName = "assuntos";
    }

    public TpuAssunto(Long codigo, String descricao, String glossario, Long codigoPai, String situacao, Boolean temFilhos) {
        super(codigo, descricao, glossario, codigoPai, situacao, temFilhos);
        this.resourceName = "assuntos";
    }
}
