package br.jus.tjro.gabinete.infrastructure.converter;


import br.jus.tjro.gabinete.model.gab.proxy.TpuAssuntoProxyHandler;
import br.jus.tjro.gabinete.model.gab.proxy.TpuClasseProxyHandler;
import br.jus.tjro.gabinete.model.gab.tpu.TpuAssunto;
import br.jus.tjro.gabinete.model.gab.tpu.TpuClasse;
import br.jus.tjro.gabinete.service.local.tpu.TpuAssuntoService;
import br.jus.tjro.gabinete.service.local.tpu.TpuClasseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;

@Component
public class TpuAssuntoConverter implements AttributeConverter<TpuAssunto, Long> {

    public static TpuAssuntoService service;

    @Override
    public Long convertToDatabaseColumn(TpuAssunto tpuClasse) {
        if(tpuClasse == null)
            return  null;
        return tpuClasse.getCodigo();
    }

    @Override
    public TpuAssunto convertToEntityAttribute(Long id) {
        return new TpuAssuntoProxyHandler(service,id).criaProxy();
    }

    @Autowired
    public void init(TpuAssuntoService service) {
        TpuAssuntoConverter.service = service;
    }
}
