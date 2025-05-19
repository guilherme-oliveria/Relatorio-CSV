package br.jus.tjro.gabinete.model.gab;

import br.jus.tjro.gabinete.config.ColegiadoEnvironment;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TipoDocumento implements Comparable<TipoDocumento>{

    private final String id;

    private final String descricao;

    private final boolean ativo;

    private final boolean minuta;

    @JsonCreator
    public TipoDocumento(@JsonProperty("id") String id,
                         @JsonProperty("descricao") String descricao,
                         @JsonProperty("ativo") Boolean ativo,
                         @JsonProperty("minuta") Boolean minuta) {
        this.id = id;
        this.descricao = descricao;
        this.ativo = ativo != null && ativo;
        this.minuta = minuta != null && minuta;
    }

    public TipoDocumento(@JsonProperty("id") String id, @JsonProperty("descricao") String descricao) {
        this(id,descricao,true,false);
    }

    public TipoDocumento(@JsonProperty("id") String id) {
        this(id,"Tipo documento sem descrição",true,false);
    }

    public Boolean isMinuta() {
        return minuta;
    }

    public Boolean isMinutaColegiado() {
        return ColegiadoEnvironment.isMinutaColegiado(this.id);
    }

    public Boolean isAnexoColegiado() {
        return ColegiadoEnvironment.isAnexoColegiado(this.id);
    }

    public Boolean isPreliminar() { return ColegiadoEnvironment.isPreliminar(this.id); }

    public Boolean isRelatorio() { return ColegiadoEnvironment.isRelatorio(this.id); }

    @Override
    public int compareTo(TipoDocumento outro) {
        return this.descricao.compareTo(outro.descricao);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        TipoDocumento that = (TipoDocumento) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descricao, ativo, minuta);
    }

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }
}
