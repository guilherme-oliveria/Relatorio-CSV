package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.minuta.PublicarProcessoDJE;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.repository.gab.minuta.PublicarProcessoDJERepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicarProcessoDjeService {

    @Autowired
    private PublicarProcessoDJERepository publicarProcessoDjeRepository;

    public Minuta verificaEhRemovePublicacaoDje(Minuta minuta) {
        if (minuta.getPublicacaoDje() != null) {
            PublicarProcessoDJE dje = minuta.getPublicacaoDje();
            dje.setMinuta(minuta);
            publicarProcessoDjeRepository.save(dje);
        }
        return minuta;
    }

    public PublicarProcessoDJE save(PublicarProcessoDJE publicacaoDje) {
        return publicarProcessoDjeRepository.save(publicacaoDje);
    }
}
