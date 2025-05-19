package br.jus.tjro.gabinete.model.gab.tpu;

import br.jus.tjro.gabinete.model.gab.enums.TipoComplementoEnum;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimento;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimentoComplemento;
import br.jus.tjro.gabinete.model.gab.transiente.OpcaoComplemento;
import br.jus.tjro.gabinete.model.gab.transiente.TipoComplemento;
import br.jus.tjro.gabinete.service.local.tpu.TpuMovimentoService;
import br.jus.tjro.gabinete.util.TpuComplementoDeserializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@JsonDeserialize(using = TpuComplementoDeserializer.class)
public class TpuComplemento implements Cloneable {

    @Autowired
    TpuMovimentoService tpuMovimentoService;
    private Long codigo;
    private String descricao;
    private String observacao;
    private TipoComplemento tipoComplemento;
    private List<OpcaoComplemento> opcoes;
    private String valor;
    
    @JsonIgnore
    private final Logger looger = LoggerFactory.getLogger(TpuComplemento.class);

    public TpuComplemento() {

    }

    public TpuComplemento(Long codigo, String descricao, String observacao) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.observacao = observacao;
    }

    public TpuComplemento(Long codigo, String descricao, String observacao, String valor,TipoComplemento tipoComplemento) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.observacao = observacao;
        this.valor = valor;
        this.tipoComplemento = tipoComplemento;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public TipoComplemento getTipoComplemento() {
        return tipoComplemento;
    }

    public void setTipoComplemento(TipoComplemento tipoComplemento) {
        this.tipoComplemento = tipoComplemento;
    }

    public List<OpcaoComplemento> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<OpcaoComplemento> opcoes) {
        this.opcoes = opcoes;
    }

    public TpuComplemento findByCodigo(Long codigoComplemento) {
        return tpuMovimentoService.getComplemento(codigoComplemento);
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public TpuComplemento clonar() {
        try {
            return (TpuComplemento) this.clone();
        } catch (CloneNotSupportedException e) {
            looger.error(e.getMessage(),e);
            return null;
        }
    }

    public MinutaMovimentoComplemento getMinutaComplemento(MinutaMovimento minutaMovimento) {
        String valor = getValor();
        MinutaMovimentoComplemento minutaComplemento = null;
        if (valor != null && !valor.equals("")) {
            minutaComplemento = new MinutaMovimentoComplemento();
            minutaComplemento.setComplementoId(getCodigo());
            minutaComplemento.setValor(valor);
            minutaComplemento.setDescricao(getDescricao());
            minutaComplemento.setTipoComplementoEnum(TipoComplementoEnum.valueOf(getTipoComplemento().getNome().toUpperCase()));
            minutaComplemento.setMinutaMovimento(minutaMovimento);
        }
        return minutaComplemento;
    }
}
