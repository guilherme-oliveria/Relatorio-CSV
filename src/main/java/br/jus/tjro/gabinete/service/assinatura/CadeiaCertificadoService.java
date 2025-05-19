package br.jus.tjro.gabinete.service.assinatura;

import br.jus.tjro.gabinete.model.gab.minuta.CadeiaCertificado;
import br.jus.tjro.gabinete.model.gab.tag.Tag;
import br.jus.tjro.gabinete.repository.gab.assinatura.CadeiaCertificadoRepository;
import org.springframework.stereotype.Service;

@Service
public class CadeiaCertificadoService {

    private CadeiaCertificadoRepository cadeiaCertificadoRepository;

    public CadeiaCertificadoService(CadeiaCertificadoRepository cadeiaCertificadoRepository){
        this.cadeiaCertificadoRepository = cadeiaCertificadoRepository;
    }

    public Boolean CadeiaCertificadoJaExiste(String hashCadeia) {
        CadeiaCertificado cadeiaCertificado = cadeiaCertificadoRepository.findByHashCadeiaCertificado(hashCadeia).orElse(null);
        if (cadeiaCertificado != null ) {
            return true;
        } else {
            return false;
        }
    }

    public CadeiaCertificado verificarSeExisteESalvar(String hash, String cadeiaCertificado) {
        var cadeiaCert = cadeiaCertificadoRepository.findByHashCadeiaCertificado(hash);
        return cadeiaCert.orElseGet(() -> save(new CadeiaCertificado(hash,cadeiaCertificado)));
    }

    public CadeiaCertificado save(CadeiaCertificado cadeiaCertificado)  {
        return cadeiaCertificadoRepository.save(cadeiaCertificado);
    }
}
