package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimento;
import br.jus.tjro.gabinete.repository.gab.minuta.MinutaMovimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MinutaMovimentoService {

    @Autowired
    private MinutaMovimentoRepository minutaMovimentoRepository;

    public List<MinutaMovimento> findByMinuta(Minuta minuta) {
        return minutaMovimentoRepository.findByMinuta(minuta);
    }

}
