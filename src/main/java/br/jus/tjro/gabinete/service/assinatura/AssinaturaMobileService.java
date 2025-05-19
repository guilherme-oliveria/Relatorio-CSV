package br.jus.tjro.gabinete.service.assinatura;

import br.jus.tjro.gabinete.model.gab.minuta.Assinatura;
import br.jus.tjro.gabinete.repository.gab.assinatura.AssinaturaRepository;
import org.springframework.stereotype.Service;

@Service
public class AssinaturaMobileService {

    private AssinaturaRepository assinaturaRepository;

    private CadeiaCertificadoService cadeiaCertificadoService;

    public AssinaturaMobileService(AssinaturaRepository assinaturaRepository, CadeiaCertificadoService cadeiaCertificadoService){
        this.assinaturaRepository = assinaturaRepository;
        this.cadeiaCertificadoService = cadeiaCertificadoService;
    }

    public Assinatura save(Assinatura assinatura)  {
        return assinaturaRepository.save(assinatura);
    }
}
