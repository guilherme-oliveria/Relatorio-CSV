package br.jus.tjro.gabinete.model.gab.transiente;

import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Parte {

    private final Long id;
    private final String nome;
    private final String tipoPolo;
    private final String procuradoria;
    private final String tipoParte;
    private final Pessoa pessoa;


    @JsonCreator
    public Parte(
            @JsonProperty("id") Long id,
            @JsonProperty("nome") String nome,
            @JsonProperty("tipoPolo") String tipoPolo,
            @JsonProperty("procuradoria") String procuradoria,
            @JsonProperty("tipoParte") String tipoParte,
            @JsonProperty("pessoa") Pessoa pessoa)
    {
        this.id = id;
        this.nome = nome;
        this.tipoPolo = tipoPolo;
        this.procuradoria = procuradoria;
        this.tipoParte = tipoParte;
        this.pessoa = pessoa;
    }

    public Parte(ProcessoParte pparte) {
        id = pparte.getId();
        nome = pparte.getNome();
        tipoPolo = pparte.getTipoPolo().getLabel();
        procuradoria = pparte.getProcuradoria();
        tipoParte = pparte.getTipoParte();
        pessoa = new Pessoa();
        pessoa.setNome(pparte.getNome());
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTipoPolo() {
        return tipoPolo;
    }

    public String getProcuradoria() {
        return procuradoria;
    }

    public String getTipoParte() {
        return tipoParte;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }
}
