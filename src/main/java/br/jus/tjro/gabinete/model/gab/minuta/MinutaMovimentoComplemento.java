package br.jus.tjro.gabinete.model.gab.minuta;

import br.jus.tjro.gabinete.model.gab.enums.TipoComplementoEnum;
import br.jus.tjro.gabinete.model.gab.tpu.TpuComplemento;
import br.jus.tjro.gabinete.model.gab.transiente.OpcaoComplemento;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "MINUTA_MOV_COMPLEMENTO")
@SequenceGenerator(name = MinutaMovimentoComplemento.SEQUENCE_NAME, sequenceName = MinutaMovimentoComplemento.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class MinutaMovimentoComplemento {

    public static final String SEQUENCE_NAME = "SEQ_MINUTA_MOV_COMPLEMENTO";

    public MinutaMovimentoComplemento() {
    }

    public MinutaMovimentoComplemento(TpuComplemento tpuComplemento) {
        this.complementoId = tpuComplemento.getCodigo();
        this.valor = tpuComplemento.getValor();
        this.tipoComplementoEnum = TipoComplementoEnum.valueOf(tpuComplemento.getTipoComplemento().getNome().toUpperCase());
        this.descricao = tpuComplemento.getDescricao();
        this.opcoesComplemento = tpuComplemento.getOpcoes();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(name = "complemento_id")
    private Long complementoId;

    @ManyToOne
    @JoinColumn(name = "minuta_movimento_id", foreignKey = @ForeignKey(name = "FK_MINUTA_MOV_COMPLEM_MIN_MOV"))
    @JsonBackReference
    private MinutaMovimento minutaMovimento;

    private String valor;

    @Column(name = "tipo_complemento")
    @Enumerated(EnumType.STRING)
    private TipoComplementoEnum tipoComplementoEnum;

    private String descricao;

    @Transient
    private List<OpcaoComplemento> opcoesComplemento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getComplementoId() {
        return complementoId;
    }

    public void setComplementoId(Long complementoId) {
        this.complementoId = complementoId;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public MinutaMovimento getMinutaMovimento() {
        return minutaMovimento;
    }

    public void setMinutaMovimento(MinutaMovimento minutaMovimento) {
        this.minutaMovimento = minutaMovimento;
    }

    public TipoComplementoEnum getTipoComplementoEnum() {
        return tipoComplementoEnum;
    }

    public void setTipoComplementoEnum(TipoComplementoEnum tipoComplementoEnum) {
        this.tipoComplementoEnum = tipoComplementoEnum;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<OpcaoComplemento> getOpcoesComplemento() {
        return opcoesComplemento;
    }

    public void setOpcoesComplemento(List<OpcaoComplemento> opcoesComplemento) {
        this.opcoesComplemento = opcoesComplemento;
    }

    public void setDadosTpu(TpuComplemento tpuComplemento) {
        setDescricao(tpuComplemento.getDescricao());
        setOpcoesComplemento(tpuComplemento.getOpcoes());
    }
}
