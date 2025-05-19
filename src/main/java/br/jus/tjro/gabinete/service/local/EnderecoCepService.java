package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.endereco.EnderecoCep;
import br.jus.tjro.gabinete.model.gab.transiente.CepTransiente;
import br.jus.tjro.gabinete.repository.gab.endereco.EnderecoCepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class EnderecoCepService {

    @Autowired
    private EnderecoCepRepository enderecoCepRepository;

    @Autowired
    private EnderecoMunicipioService enderecoMunicipioService;

    private EnderecoCep getByNumeroDoCep(CepTransiente cepTransiente) {
        return enderecoCepRepository.findByNumero(cepTransiente.getNumero());
    }

    @Transactional
    EnderecoCep save(CepTransiente cepTransiente) {
        EnderecoCep enderecoCep = new EnderecoCep(
            cepTransiente.getNumero(), cepTransiente.getLogradouro(),
            cepTransiente.getBairro(), cepTransiente.getComplemento(),
            enderecoMunicipioService.busca(cepTransiente.getMunicipio())
        );
        return enderecoCepRepository.save(enderecoCep);
    }

    public EnderecoCep busca(CepTransiente cepTransiente) {
        if (cepTransiente != null) {
            EnderecoCep temCepLocal = getByNumeroDoCep(cepTransiente);

            if (temCepLocal == null)
                return save(cepTransiente);

            return temCepLocal;
        }
        return null;
    }
}
