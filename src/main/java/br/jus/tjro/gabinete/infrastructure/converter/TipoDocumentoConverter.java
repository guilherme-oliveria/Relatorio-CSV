package br.jus.tjro.gabinete.infrastructure.converter;

import br.jus.tjro.gabinete.config.ColegiadoEnvironment;
import br.jus.tjro.gabinete.config.JuizLeigoEnvironment;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.model.gab.proxy.TipoDocumentoProxyHandler;
import br.jus.tjro.gabinete.repository.gab.TipoDocumentoRespositoryImp;
import br.jus.tjro.gabinete.service.local.TipoDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import jakarta.persistence.AttributeConverter;

import java.util.Collections;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@Component
public class TipoDocumentoConverter implements AttributeConverter<TipoDocumento, String> {

    public static TipoDocumentoService service = new TipoDocumentoService(
        new TipoDocumentoRespositoryImp(new RestTemplate(),"localhost"),
        new ColegiadoEnvironment(emptyList(),emptyList(), ""),
        new JuizLeigoEnvironment(emptyList())
        );

    @Override
    public String convertToDatabaseColumn(TipoDocumento tipoDocumento) {
        return ofNullable(tipoDocumento).map(t -> t.getId()).orElse(null);
    }

    @Override
    public TipoDocumento convertToEntityAttribute(String id) {
        if(id != null && ColegiadoEnvironment.isPreliminar(id)) {
            return new TipoDocumento(id, "Preliminar", true, false);
        }
        return new TipoDocumentoProxyHandler(service,id).criaProxy();
    }

    @Autowired
    public void init(TipoDocumentoService service, ColegiadoEnvironment colegiadoEnvironment) {
        TipoDocumentoConverter.service = service;
    }
}
