package br.jus.tjro.gabinete.infrastructure.converter;


import br.jus.tjro.gabinete.model.gab.proxy.TpuClasseProxyHandler;
import br.jus.tjro.gabinete.model.gab.tpu.TpuClasse;
import br.jus.tjro.gabinete.service.local.tpu.TpuClasseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;

@Component
public class TpuClasseConverter implements AttributeConverter<TpuClasse, Long> {

    public static TpuClasseService service;

    @Override
    public Long convertToDatabaseColumn(TpuClasse tpuClasse) {
        if(tpuClasse == null)
            return  null;
        return tpuClasse.getCodigo();
    }

    @Override
    public TpuClasse convertToEntityAttribute(Long id) {
        return new TpuClasseProxyHandler(service,id).criaProxy();
    }

    @Autowired
    public void init(TpuClasseService service) {
        TpuClasseConverter.service = service;
    }
}
