package br.jus.tjro.gabinete.infrastructure.converter;

import br.jus.tjro.gabinete.model.gab.proxy.OrgaoJulgadorProxyHandler;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.service.local.OrgaoJulgadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;

import static java.util.Optional.ofNullable;

@Component
public class OrgaoJulgadorConverter implements AttributeConverter<OrgaoJulgador, String> {

    public static OrgaoJulgadorService service;

    @Override
    public String convertToDatabaseColumn(OrgaoJulgador orgaoJulgador) {
        return ofNullable(orgaoJulgador).map(OrgaoJulgador::getId).orElse(null);
    }

    @Override
    public OrgaoJulgador convertToEntityAttribute(String dbId) {
        if(dbId == null)
            return null;
        return new OrgaoJulgadorProxyHandler(service, dbId).criaProxy();
    }

    @Autowired
    public void init(OrgaoJulgadorService service) {
        OrgaoJulgadorConverter.service = service;
    }
}
