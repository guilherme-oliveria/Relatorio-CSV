package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.dto.AvisoDTO;
import br.jus.tjro.gabinete.model.gab.Aviso;
import br.jus.tjro.gabinete.model.gab.Mensagem;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.repository.gab.AvisoRepository;
import br.jus.tjro.gabinete.repository.gab.MensagemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class AvisoService {

    @Autowired
    AvisoRepository avisoRepository;

    public Aviso salvar(AvisoDTO avisoDTO, Usuario user) throws IOException {
        Aviso aviso = montaObjeto(avisoDTO);
        aviso.setCpfUsuario(user.getCpf());
        aviso.setDtCadastro(new Date());
        aviso.setNomeUsuario(user.getNome());
        return avisoRepository.save(aviso);
    }

    private Aviso montaObjeto(AvisoDTO avisoDTO) {
        Aviso aviso = new Aviso();
        aviso.setId(avisoDTO.getId());
        aviso.setDtCadastro(avisoDTO.getDtCadastro());
        aviso.setDtIniVisibilidade(avisoDTO.getDtIniVisibilidade());
        aviso.setDtFimVisibilidade(avisoDTO.getDtFimVisibilidade());
        aviso.setCpfUsuario(avisoDTO.getCpfUsuario());
        aviso.setNomeUsuario(avisoDTO.getNomeUsuario());
        aviso.setTitulo(avisoDTO.getTitulo());
        aviso.setMensagem(avisoDTO.getMensagem());
        aviso.setInAtivo(avisoDTO.getInAtivo());
        return aviso;
    }

    public boolean excluir(Long id) {
        avisoRepository.deleteById(id);
        return true;
    }

    public List<Aviso> getTodosAvisosAtivos() {
        return avisoRepository.findAtivos();
    }
}

