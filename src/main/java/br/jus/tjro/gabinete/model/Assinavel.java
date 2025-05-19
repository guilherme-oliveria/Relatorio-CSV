package br.jus.tjro.gabinete.model;

import br.jus.tjro.gabinete.model.gab.enums.TipoDocumentoAssinatura;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public interface Assinavel {

    @JsonIgnore
    TipoDocumentoAssinatura getTipoDocumentoAssinatura();
    Long getId();
    @JsonIgnore
    default List<String> getParametrosAssinatura(){
        return List.of("tipoDocumento="+getTipoDocumentoAssinatura(),"id="+getId().toString());
    }
}
