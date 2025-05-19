package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.endereco.EnderecoMunicipio;
import br.jus.tjro.gabinete.model.gab.transiente.MunicipioTransiente;
import br.jus.tjro.gabinete.repository.gab.endereco.EnderecoMunicipioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class EnderecoMunicipioService {

    @Autowired
    private EnderecoMunicipioRepository enderecoMunicipioRepository;

    @Autowired
    private EstadoService estadoService;


    private EnderecoMunicipio getByNomeMunicipioAndUf(MunicipioTransiente municipioTransiente) {
        return enderecoMunicipioRepository.findByDescricaoAndEstado_Uf(
            municipioTransiente.getDescricao(), municipioTransiente.getEstado().getUf());
    }

    @Transactional
    EnderecoMunicipio save(MunicipioTransiente municipioTransiente) {
        EnderecoMunicipio municipio = new EnderecoMunicipio(
            municipioTransiente.getDescricao(),
            estadoService.busca(municipioTransiente.getEstado())
        );
        return enderecoMunicipioRepository.save(municipio);
    }

    public EnderecoMunicipio busca(MunicipioTransiente municipioTransiente) {
        if (municipioTransiente != null) {
            EnderecoMunicipio temMunicipioLocal = getByNomeMunicipioAndUf(municipioTransiente);
            if (temMunicipioLocal == null) {
                return save(municipioTransiente);
            }
            return temMunicipioLocal;
        }
        return null;
    }
}
