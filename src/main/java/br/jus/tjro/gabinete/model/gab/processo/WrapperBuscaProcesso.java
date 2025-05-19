package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.transiente.ProcessoConcluso;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WrapperBuscaProcesso {

    private final ProcessoConcluso processoConcluso;
    private final Processo processo;
    private Usuario usuario;

    public WrapperBuscaProcesso(ProcessoConcluso processoConcluso) {
        if(processoConcluso == null)
            throw new IllegalArgumentException("ProcessoConcluso não pode ser nulo");
        this.processoConcluso = processoConcluso;
        this.processo = null;
    }

    @JsonCreator
    public WrapperBuscaProcesso(@JsonProperty("processoConcluso") ProcessoConcluso processoConcluso,
                                @JsonProperty("usuario") Usuario usuario) {
        this.processoConcluso = processoConcluso;
        this.processo = null;
        if(processoConcluso == null)
            throw new IllegalArgumentException("ProcessoConcluso não pode ser nulo");
        if(usuario == null)
            throw new IllegalArgumentException("Usuario não pode ser nulo");
        this.usuario = usuario;
    }

    public WrapperBuscaProcesso(Processo processo) {
        this.processoConcluso = null;
        this.processo = processo;
        if(processo == null)
            throw new IllegalArgumentException("Processo não pode ser nulo");
    }

    @JsonIgnore
    public boolean isImportarSistemaLegado() {
        return processoConcluso != null;
    }

    @JsonIgnore
    public Processo getProcesso() {
        return  processo;
    }

    public ProcessoConcluso getProcessoConcluso() {
        return processoConcluso;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }
}
