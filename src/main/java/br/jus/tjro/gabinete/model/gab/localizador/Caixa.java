package br.jus.tjro.gabinete.model.gab.localizador;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity
@Table(name = "caixa")
@SequenceGenerator(name = Caixa.SEQUENCE_NAME, sequenceName = Caixa.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class Caixa {

    public static final String SEQUENCE_NAME = "SEQUENCIA_CAIXA";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nome", length = 64)
    private String nome;

    @OneToMany(mappedBy = "caixa")
    @JsonIgnore
    @JsonManagedReference
    private List<Processo> processo;

    @Transient
    @JsonInclude(Include.NON_EMPTY)
    private Long tamanho;

    public Caixa() {
    }

    public Caixa(Integer id) {
        this.id = id;
    }

    public Caixa(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Caixa(Integer id, String nome, Long tamanho) {
        this.id = id;
        this.nome = nome;
        this.tamanho = tamanho;
    }

    public Caixa(Integer id, String nome, Long tamanho, TarefaEnum tarefa) {
        this.id = id;
        this.nome = nome;
        this.tamanho = tamanho;
    }

    public Caixa(String nome) {
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Processo> getProcesso() {
        return processo;
    }

    public void setProcesso(List<Processo> processo) {
        this.processo = processo;
    }

    public Long getTamanho() {
        return tamanho;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
    }

    @Transient
    @JsonIgnore
    public static List<LocalizadorAgrupador> getLocalizadoresAgrupador(List<Caixa> caixas){
        return caixas.stream().map(t -> new LocalizadorAgrupador(t)).collect(toList());
    }

}
