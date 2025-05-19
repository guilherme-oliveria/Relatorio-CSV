package br.jus.tjro.gabinete.model.gab;

import br.jus.tjro.gabinete.dto.PapelDTO;
import br.jus.tjro.gabinete.model.RecursosDoPapel;
import br.jus.tjro.gabinete.model.gab.segredo.recurso.AdministradorRecurso;
import br.jus.tjro.gabinete.model.gab.segredo.recurso.AssessorRecurso;
import br.jus.tjro.gabinete.model.gab.segredo.recurso.MagistradoRecurso;
import br.jus.tjro.gabinete.model.gab.segredo.recurso.Recursos;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Papel {

    private final String papel;
    private final List<OrgaoJulgador> orgaoJulgador;
    @JsonIgnore
    private final Recursos recursos;

    @JsonCreator
    public Papel(@JsonProperty("papel") String papel, @JsonProperty("orgaoJulgador") List<OrgaoJulgador> orgaoJulgador) {
        this.papel = papel;
        this.orgaoJulgador = orgaoJulgador;
        this.recursos = RecursosDoPapel.get(papel);
    }

    public Boolean contemOrgaoJulgador(String idOrgaoJulgador) {
        List<OrgaoJulgador> orgaoJulgadores = orgaoJulgador.stream().filter(oj -> oj.getId().equals(idOrgaoJulgador)).collect(Collectors.toList());
        return orgaoJulgadores.size() > 0;
    }

    public PapelDTO converteToDTO() {
        return new PapelDTO(this, orgaoJulgador);
    }

    public String getPapel() {
        return papel;
    }

    public List<OrgaoJulgador> getOrgaoJulgador() {
        return orgaoJulgador;
    }

    public Recursos getRecursos() {
        return recursos;
    }

    //Essa função é paleativa, o ideal seria o front end do gabinete verificar os recursos do perfil
    @Deprecated
    public String getRecursosName() {
        if(this.recursos instanceof AdministradorRecurso) {
            return "administrador";
        }else if(this.recursos instanceof MagistradoRecurso) {
            return "magistrado";
        } else if(this.recursos instanceof AssessorRecurso) {
            return "assessor";
        }else{
            return "semrecurso";
        }
    }
}
