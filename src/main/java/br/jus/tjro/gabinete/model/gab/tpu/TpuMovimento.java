package br.jus.tjro.gabinete.model.gab.tpu;

import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimento;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimentoComplemento;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TpuMovimento extends Tpu {

    private List<TpuComplemento> complementos = new ArrayList<>();

    public TpuMovimento() {
        resourceName = "movimentos";
    }

    public TpuMovimento(Long codigo) {
        super(codigo);
        this.complementos = complementos;
    }

    public TpuMovimento(Long codigo, String descricao, String glossario, Long codigoPai, String situacao, Boolean temFilhos) {
        super(codigo, descricao, glossario, codigoPai, situacao, temFilhos);
        this.resourceName = "movimentos";
    }

    public List<TpuComplemento> getComplementos() {
        return complementos;
    }

    public void setComplementos(List<TpuComplemento> complementos) {
        this.complementos = complementos;
    }

    @Override
    public Long getCodigo() {
        return super.getCodigo();
    }

    @Override
    public Long getCodigoPai() {
        return super.getCodigoPai();
    }

    @Override
    public String getDescricao() {
        return super.getDescricao();
    }

    @Override
    public String getGlossario() {
        return super.getGlossario();
    }

    public MinutaMovimento getMinutaMovimento(Minuta minuta) {
        MinutaMovimento minutaMovimento = new MinutaMovimento(minuta, getCodigo());
        List<MinutaMovimentoComplemento> minutaComplementos = getComplementos().stream().map(p -> p.getMinutaComplemento(minutaMovimento)).collect(toList());
        minutaMovimento.setMinutaMovimentoComplementos(minutaComplementos);
        return minutaMovimento;
    }
}
