package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.endereco.Estado;
import br.jus.tjro.gabinete.repository.gab.endereco.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class EstadoService {

    private final EstadoRepository estadoRepository;

    @Autowired
    public EstadoService(EstadoRepository estadoRepository){
        this.estadoRepository = estadoRepository;
    }


    private Estado getByUfEstado(Estado estado) {
        return estadoRepository.findByUf(estado.getUf());
    }

    @Transactional
    Estado save(Estado estado) {
        return estadoRepository.save(estado);
    }

    public Estado busca(Estado estado) {
        if (estado != null) {
            Estado temEstadoLocal = getByUfEstado(estado);
            if (temEstadoLocal == null) {
                return save(estado);
            }
            return temEstadoLocal;
        }
        return null;
    }
}
