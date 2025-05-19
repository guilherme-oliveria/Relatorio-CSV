package br.jus.tjro.gabinete.model.webjud;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgaoJulgador {

    private final String id;

    private String descricao;

    @JsonManagedReference
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OrgaoEndereco> enderecos = new ArrayList<>();

    private String sigla;
    private String idLegado;
    private String sistema;

    private List<OrgaoJulgador> orgaosJulgadoresRevisores = new ArrayList<>();

    public OrgaoJulgador(String id) {
        this(id,id+" - Serviço de lotações esta indisponível",new ArrayList<>(),"",new ArrayList<>());
    }

    @JsonCreator
    public OrgaoJulgador(@JsonProperty("id") String id,
                         @JsonProperty("descricao") String descricao,
                         @JsonProperty("enderecos") List<OrgaoEndereco> enderecos,
                         @JsonProperty("sigla") String sigla,
                         @JsonProperty("orgaosJulgadoresRevisores") List<OrgaoJulgador> orgaosJulgadoresRevisores) {
        if(("PJEPG-".equals(id) || "PJESG-".equals(id)))
            throw new RuntimeException("O id do orgao julgador não pode ser PJEPG-");
        else if(!id.contains("-"))
            throw new RuntimeException("O id do orgao julgador deve ter o formato {sistema}-{id}");
        this.id = id;
        this.descricao = descricao;
        this.enderecos = enderecos;
        this.sigla = sigla;
        this.orgaosJulgadoresRevisores = orgaosJulgadoresRevisores;
    }

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public List<OrgaoEndereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<OrgaoEndereco> enderecos) {
        this.enderecos = enderecos;
    }

    public String getIdLegado() {
        return idLegado;
    }

    public void setIdLegado(String idLegado) {
        this.idLegado = idLegado;
    }

    public String getSistema() {
        return sistema;
    }

    public void setSistema(String sistema) {
        this.sistema = sistema;
    }

    public List<OrgaoJulgador> getOrgaosJulgadoresRevisores() {
        return orgaosJulgadoresRevisores;
    }

    public void setOrgaosJulgadoresRevisores(List<OrgaoJulgador> orgaosJulgadoresRevisores) {
        this.orgaosJulgadoresRevisores = orgaosJulgadoresRevisores;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        OrgaoJulgador that = (OrgaoJulgador) o;
        return Objects.equals(getId(), that.getId());
    }
}
