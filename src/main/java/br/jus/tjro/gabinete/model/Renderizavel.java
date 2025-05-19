package br.jus.tjro.gabinete.model;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.interfaces.ModeloDocumentoService;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Renderizavel {
    @JsonIgnore
    String getTemplate();

    @JsonIgnore
    void setHtmlProcessado(String htmlProcessado);

    @JsonIgnore
    default void renderizarHtml(ModeloDocumentoService modeloDocumento, Usuario usuario, Processo processo, Boolean assinando) throws ServicoRemotoException {
        String modeloProcessado = modeloDocumento.renderizarVariavel(usuario, processo, this.getTemplate(), assinando);
        this.setHtmlProcessado(modeloProcessado);
    }

}
