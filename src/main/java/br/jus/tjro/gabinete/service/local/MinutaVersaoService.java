package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaVersao;
import br.jus.tjro.gabinete.repository.gab.minuta.MinutasVersoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MinutaVersaoService {

    @Autowired
    private MinutasVersoesRepository minutasVersoesRepository;

    private MinutaVersao save(MinutaVersao minutaVersao) throws Exception {
        return minutasVersoesRepository.save(minutaVersao);
    }

    public MinutaVersao findOne(Long id) throws Exception {
        MinutaVersao minutaVersao = minutasVersoesRepository.findById(id).orElseThrow(NullPointerException::new); // ToDo tratar a falta desse recurso
        return minutaVersao;
    }

    public List<MinutaVersao> getVersaoByIdMinuta(Minuta minuta) {
        return minutasVersoesRepository.findByIdMinuta(minuta.getId());
    }

    public MinutaVersao findOneCompara(Minuta minuta, Integer versao) throws Exception {
        MinutaVersao minutaVersao = minutasVersoesRepository.findByIdMinutaVersao(minuta.getId(),
            versao);
        return minutaVersao;
    }

    public Integer minutaVersaoComparaEhSalva(Minuta minuta, Usuario usuario) throws Exception {
        if (minuta.getMinutaHtml() == null)
            throw new Exception("O HTML não pode ser nulo para salvar uma versão");

        List<MinutaVersao> versoes = getVersaoByIdMinuta(minuta);

        if (versoes == null || versoes.size() < 1) {
            return salvaVersao(minuta,usuario);

        } else {
            Integer numVersaoAtual = versoes.get(0).getVersao();
            String conteudoVersaoAtual = minuta.getMinutaHtml();
            String ultimaVersao = versoes.get(0).getMinutaHtml();

            if (!conteudoVersaoAtual.equals(ultimaVersao))
                return salvaVersao(minuta, numVersaoAtual + 1,usuario);
            return numVersaoAtual;
        }
    }

    private Integer salvaVersao(Minuta minuta, Integer versao, Usuario usuario) throws Exception {
        return save(new MinutaVersao(minuta, versao, usuario)).getVersao();
    }

    private Integer salvaVersao(Minuta minuta,Usuario usuario) throws Exception {
        return salvaVersao(minuta, 1,usuario);
    }
}
