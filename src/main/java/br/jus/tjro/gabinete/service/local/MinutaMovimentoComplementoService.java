package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimento;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimentoComplemento;
import br.jus.tjro.gabinete.repository.gab.minuta.MinutaMovimentoComplementoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MinutaMovimentoComplementoService {

    @Autowired
    private MinutaMovimentoComplementoRepository complementoRepository;

    public List<MinutaMovimentoComplemento> findByMinutaMovimento(MinutaMovimento minutaMovimento) {
        return complementoRepository.findByMinutaMovimentoId(minutaMovimento.getId());
    }

}
