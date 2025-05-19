package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.endereco.Endereco;
import br.jus.tjro.gabinete.model.gab.transiente.EnderecoTransiente;
import br.jus.tjro.gabinete.repository.gab.endereco.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    private final EnderecoCepService enderecoCepService;

    @Autowired
    public EnderecoService(EnderecoRepository enderecoRepository,EnderecoCepService enderecoCepService){
        this.enderecoRepository = enderecoRepository;
        this.enderecoCepService = enderecoCepService;
    }


    private Endereco findByIdEnderecoLegadoAndFonteDados(List<Long> ids, FonteDadosEnum sistema) {
        return enderecoRepository.findByIdEnderecoLegadoAndFonteDados(ids, sistema.fonte);
    }

    public Endereco saveEndereco(EnderecoTransiente enderecoTransiente, FonteDadosEnum sistema) {

        Endereco endereco = new Endereco(
            enderecoTransiente.getId(), enderecoTransiente.getLogradouro(), enderecoTransiente.getNumero(),
            enderecoTransiente.getComplemento(), enderecoTransiente.getBairro(),
            enderecoCepService.busca(enderecoTransiente.getCep())
        );
        return enderecoRepository.save(endereco);
    }

    public Endereco busca(EnderecoTransiente enderecoTransiente, FonteDadosEnum sistema) {
        if (enderecoTransiente != null) {
            List<Long> ids = new ArrayList<>();
            ids.add(enderecoTransiente.getId());
            Endereco temEnderecoLocal = findByIdEnderecoLegadoAndFonteDados(ids, sistema);

            if (temEnderecoLocal == null)
                return saveEndereco(enderecoTransiente, sistema);

            return temEnderecoLocal;
        }
        return null;
    }
}
