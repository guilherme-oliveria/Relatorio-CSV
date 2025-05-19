package br.jus.tjro.gabinete.model.gab.alvara;


public class Requisitante {
    private Long aplicacaoId = 781L;
    private String matricula;
    private String nome;

    public Requisitante(String requisitante, String cpfRequisitante) {
        this.nome = requisitante;
        this.matricula = cpfRequisitante;
    }

    public Long getAplicacaoId() {
        return aplicacaoId;
    }

    public void setAplicacaoId(Long aplicacaoId) {
        this.aplicacaoId = aplicacaoId;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
