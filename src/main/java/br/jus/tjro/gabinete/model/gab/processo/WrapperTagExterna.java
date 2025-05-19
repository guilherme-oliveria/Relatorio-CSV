package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WrapperTagExterna {

    private final Long processoLegadoId;
    private final String numeroProcesso;
    private final FonteDadosEnum fonteDados;
    private final String tag;

    @JsonCreator
    public WrapperTagExterna(@JsonProperty("processo_id") Long processoLegadoId,
                             @JsonProperty("numero_processo") String numeroProcesso,
                             @JsonProperty("instancia") String instancia,
                             @JsonProperty("tag") String tag) {
        this.processoLegadoId = processoLegadoId;
        this.numeroProcesso = numeroProcesso;
        this.fonteDados = instancia.equals("PRIMEIRO_GRAU") ? FonteDadosEnum.PJEPG : FonteDadosEnum.PJESG;
        this.tag = tag;
    }

    public Long getProcessoLegadoId() {
        return processoLegadoId;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public FonteDadosEnum getFonteDados() {
        return fonteDados;
    }

    public String getTag() {
        return tag;
    }
}
