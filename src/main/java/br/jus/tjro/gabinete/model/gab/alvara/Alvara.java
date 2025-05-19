package br.jus.tjro.gabinete.model.gab.alvara;

import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Table(name = "alvara")
@SequenceGenerator(name = Alvara.SEQUENCE_NAME, sequenceName = Alvara.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
@Where(clause = Alvara.NomeColunaAtivo + "= true")
public class Alvara implements Serializable {

    public static final String SEQUENCE_NAME = "SEQ_ALVARA";
    public static final String NomeColunaAtivo = "ativo";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ID_MINUTA", foreignKey = @ForeignKey(name = "FK_MINUTA"))
    @JsonManagedReference
    @JsonIgnore
    private Minuta minuta;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_alvara")
    @JsonManagedReference
    private List<PagamentoAlvara> pagamentoAlvara;

    private String requisitante;

    @Column(name = "cpf_requisitante")
    private String cpfRequisitante;

    private boolean ativo;

    @Transient
    private Long processoId;

    public Alvara(){

    }

    public Alvara(Minuta minuta, List<PagamentoAlvara> pagamentoAlvara, String requisitante, String cpfRequisitante) {
        this(minuta, LocalDateTime.now(), pagamentoAlvara, requisitante, cpfRequisitante, true, null);

    }

    @JsonCreator
    public Alvara(@JsonProperty("minuta") Minuta minuta, @JsonProperty("dataCadastro") LocalDateTime dataCadastro,
                  @JsonProperty("pagamentoAlvara") List<PagamentoAlvara> pagamentoAlvara, @JsonProperty("requisitante") String requisitante,
                  @JsonProperty("cpfRequisitante") String cpfRequisitante, @JsonProperty("ativo") boolean ativo, @JsonProperty("processoId") Long processoId) {
        minuta.setAlvara(this);
        this.minuta = minuta;
        this.dataCadastro = dataCadastro == null ? LocalDateTime.now() : dataCadastro;
        this.pagamentoAlvara = pagamentoAlvara;
        this.requisitante = requisitante;
        this.cpfRequisitante = cpfRequisitante;
        this.ativo = ativo;
        this.processoId = processoId;
    }

    public boolean foiIntegrado() {
        return pagamentoAlvara.stream().filter(PagamentoAlvara::isEnviado).collect(toList()).size() == pagamentoAlvara.size();
    }

    public void deletar(){
        this.ativo=false;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Minuta getMinuta() {
        return minuta;
    }

    public void setMinuta(Minuta minuta) {
        this.minuta = minuta;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public List<PagamentoAlvara> getPagamentoAlvara() {
        return pagamentoAlvara;
    }

    public void setPagamentoAlvara(List<PagamentoAlvara> pagamentoAlvara) {
        this.pagamentoAlvara = pagamentoAlvara;
    }

    public String getRequisitante() {
        return requisitante;
    }

    public void setRequisitante(String requisitante) {
        this.requisitante = requisitante;
    }

    public String getCpfRequisitante() {
        return cpfRequisitante;
    }

    public void setCpfRequisitante(String cpfRequisitante) {
        this.cpfRequisitante = cpfRequisitante;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Long getProcessoId() {
        return processoId;
    }

    public void setProcessoId(Long processoId) {
        this.processoId = processoId;
    }
}
