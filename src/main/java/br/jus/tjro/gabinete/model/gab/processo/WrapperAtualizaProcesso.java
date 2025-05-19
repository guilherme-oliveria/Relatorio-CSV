package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.model.gab.Usuario;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WrapperAtualizaProcesso {

    private final Long idProcesso;
    private Usuario usuario;

    @JsonCreator
    public WrapperAtualizaProcesso(@JsonProperty("idProcesso") Long idProcesso,
                                   @JsonProperty("usuario") Usuario usuario) {
        this.idProcesso = idProcesso;
        if(idProcesso == null)
            throw new IllegalArgumentException("Id processo não pode ser nulo");
        if(usuario == null)
            throw new IllegalArgumentException("Usuario não pode ser nulo");
        this.usuario = usuario;
    }


    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public Long getIdProcesso() {
        return idProcesso;
    }
}
