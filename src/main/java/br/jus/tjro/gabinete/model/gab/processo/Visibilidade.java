package br.jus.tjro.gabinete.model.gab.processo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Visibilidade {

    private Long idLegado;
    private final String nome;
    private final String cpf;
    private final Long idPessoaLegado;


    @JsonCreator
    Visibilidade(@JsonProperty("nome") String nome,
                 @JsonProperty(value = "cpf", defaultValue = "") String cpf,
                 @JsonProperty("idPessoaLegado") Long idPessoaLegado){
        this.nome = nome;
        if(cpf == null)
            this.cpf = "";
        else
            this.cpf = cpf;
        this.idPessoaLegado = idPessoaLegado;
    }

    public void setIdLegado(Long idLegado) {
        this.idLegado = idLegado;
    }

    public Long getIdLegado() {
        return idLegado;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNumerosCpf() {
        return cpf.replaceAll("\\.","").replaceAll("-","");
    }

    public Long getIdPessoaLegado() {
        return idPessoaLegado;
    }
}
