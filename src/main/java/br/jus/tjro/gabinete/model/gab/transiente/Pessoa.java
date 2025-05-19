package br.jus.tjro.gabinete.model.gab.transiente;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;

public class Pessoa {
    private final int idPessoaLegado;
    private final String nome;
    private final String cpfCnpj;

    @JsonCreator
    public Pessoa(@JsonProperty("idPessoaLegado") int idPessoaLegado,
                  @JsonProperty("nome") String nome,
                  @JsonProperty("cpfCnpj") String cpfCnpj) {
        this.idPessoaLegado = idPessoaLegado;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
    }

    public int getIdPessoaLegado() {
        return idPessoaLegado;
    }

    public String getNome() {
        return nome;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }
}
