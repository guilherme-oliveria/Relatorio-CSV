package br.jus.tjro.gabinete.model.gab;

import br.jus.tjro.gabinete.dto.PapelDTO;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.segredo.recurso.AssessorRecurso;
import br.jus.tjro.gabinete.model.gab.segredo.recurso.MagistradoRecurso;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuario {

    private final String id;

    private final String nome;

    private final List<String> permissoes;

    private final String token;

    private Map<String, List<Papel>> papeis = new HashMap();

    public Usuario(@JsonProperty("nome") String nome,
                   @JsonProperty("id") String id,
                   @JsonProperty("permissoes") List<String> permissoes,
                   @JsonProperty("token") String token) {
        this.nome = nome;
        this.id = id;
        this.permissoes = permissoes;
        this.token = setToken(token);
    }

    public String getNome() {
        return nome;
    }

    public String getId() {
        return id;
    }

    @JsonIgnore
    public List<String> getOrgaosJulgadores() {
        List<String> orgaosJulgadores = new ArrayList<>();
        if (this.papeis != null) {
            this.papeis.forEach((s, listPapeis) -> listPapeis.forEach(papel -> papel.getOrgaoJulgador().forEach(orgaoJulgador -> {
                if (!orgaosJulgadores.contains(orgaoJulgador.getId())) {
                    orgaosJulgadores.add(orgaoJulgador.getId());
                }
            })));
        }
        return orgaosJulgadores;
    }

    @JsonIgnore
    public List<OrgaoJulgador> getOrgaosJulgadoresCompleto() {
        List<OrgaoJulgador> orgaosJulgadoresCompleto = new ArrayList<>();
        if (this.papeis != null) {
            this.papeis.forEach((s, listPapeis) -> listPapeis.forEach(papel -> papel.getOrgaoJulgador().forEach(orgaoJulgador -> {
                if (!orgaosJulgadoresCompleto.contains(orgaoJulgador)) {
                    orgaosJulgadoresCompleto.add(orgaoJulgador);
                }
            })));
        }
        return orgaosJulgadoresCompleto;
    }

    public List<String> getPermissoes() {
        return this.permissoes;
    }

    @JsonIgnore
    public String getCpf() {
        return id;
    }

    @JsonIgnore
    public String getToken() {
        return token;
    }

    public List<Papel> getPapeisBySistema(String sistema) {
        if(!this.papeis.containsKey(sistema))
            return new ArrayList();
        else
            return this.papeis.get(sistema);
    }

    public void setPapeis(Map<String, List<Papel>> papeis) {
        this.papeis = papeis;
    }

    private String setToken(String token) {
        if (token != null) {
            token = token.trim();
            if (!token.startsWith("Bearer "))
                return "Bearer " + token;
        }
        return token;
    }

    public Boolean isServidor(String sistema) {
        return papeis != null && !papeis.isEmpty() && papeis.containsKey(sistema);
    }

    public boolean isMagistrado(OrgaoJulgador oj) {
        return getPapeisByOrgaoJulgador(oj).stream().anyMatch(it -> it.getRecursos() instanceof MagistradoRecurso);
    }

    public boolean isAssessor(OrgaoJulgador oj) {
        return getPapeisByOrgaoJulgador(oj).stream().anyMatch(it -> it.getRecursos() instanceof AssessorRecurso);
    }

    public boolean lePorcesso(Processo processo){
        return getPapeisByOrgaoJulgador(processo.getOrgaoJulgadorObj()).stream().anyMatch(it -> it.getRecursos().isLeProcesso(processo));
    }
    public List<Papel> getPapeisByOrgaoJulgador(OrgaoJulgador oj){
        return getPapeisBySistema(oj.getSistema()).stream().filter(it -> it.getOrgaoJulgador().contains(oj)).toList();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((permissoes == null) ? 0 : permissoes.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Usuario other = (Usuario) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        if (permissoes == null) {
            return other.permissoes == null;
        } else return permissoes.size() == other.permissoes.size();
    }

    public Collection<? extends GrantedAuthority> authority() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        this.getPermissoes().forEach(i -> authorities.add(new SimpleGrantedAuthority(i.toUpperCase())));
        return authorities;
    }

    public Map<String, List<Papel>> getPapeis() {
         return this.papeis;
    }

    @JsonIgnore
    public Map<String, List<PapelDTO>> getAllPapeisDto(){
        Map<String, List<PapelDTO>> mapPapeis = new HashMap<String, List<PapelDTO>>();
        papeis.forEach((s, papels) -> {
            mapPapeis.put(s,papels.stream().map(Papel::converteToDTO).toList());
        });
        return mapPapeis;
    }
}
