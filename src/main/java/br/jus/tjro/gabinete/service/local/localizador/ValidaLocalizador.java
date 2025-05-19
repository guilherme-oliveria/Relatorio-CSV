package br.jus.tjro.gabinete.service.local.localizador;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorWrapper;
import br.jus.tjro.gabinete.repository.gab.localizador.caixa.LocalizadorCaixaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidaLocalizador {

    private final LocalizadorCaixaRepository localizadorCaixaRepository;

    @Autowired
    public ValidaLocalizador(LocalizadorCaixaRepository localizadorCaixaRepository) {
        this.localizadorCaixaRepository = localizadorCaixaRepository;
    }

    public void validaLocalizadorAntesDeSalvar(String parametro, LocalizadorWrapper localizador, Usuario usuario)
        throws Exception {
        if (!localizador.validar())
            throw new Exception("Localizador não é válido");
    }
}
