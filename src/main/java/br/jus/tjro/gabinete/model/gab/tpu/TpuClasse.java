package br.jus.tjro.gabinete.model.gab.tpu;

public class TpuClasse extends Tpu {

    public TpuClasse() {
        super();
        resourceName = "classes";
    }

    public TpuClasse(Long codigo) {
        super(codigo);
        resourceName = "classes";
    }

    public TpuClasse(Long codigo, String descricao, String glossario, Long codigoPai, String situacao, Boolean temFilhos) {
        super(codigo, descricao, glossario, codigoPai, situacao, temFilhos);
        resourceName = "classes";
    }
}
