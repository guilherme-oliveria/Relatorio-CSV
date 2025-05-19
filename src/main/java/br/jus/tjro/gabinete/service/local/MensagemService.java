package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.Mensagem;
import br.jus.tjro.gabinete.repository.gab.MensagemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@Service
public class MensagemService {

    @Autowired
    MensagemRepository mensagemRepository;

    public boolean salvar(String titulo, String mensagem, Object objeto, Timestamp dataLimiteParaEntrega, String usuarioId, String orgaoJulgadorId, boolean excluirAposNotificacao) throws IOException {
        Mensagem msg = montaObjeto(titulo, mensagem);
        ObjectMapper mapper = new ObjectMapper();
        msg.setObjeto(mapper.writeValueAsString(objeto));
        msg.setDataLimite(dataLimiteParaEntrega);
        msg.setUsuarioId(usuarioId);
        msg.setOrgaoJulgadorId(orgaoJulgadorId);
        msg.setExcluirAposNotificacao(excluirAposNotificacao);
        mensagemRepository.save(msg);
        return true;
    }

    public boolean salvar(String titulo, String mensagem, Object objeto, Timestamp dataLimiteParaEntrega, String usuarioId, String orgaoJulgadorId) throws IOException {
        Mensagem msg = montaObjeto(titulo, mensagem);
        ObjectMapper mapper = new ObjectMapper();
        msg.setObjeto(mapper.writeValueAsString(objeto));
        msg.setDataLimite(dataLimiteParaEntrega);
        msg.setUsuarioId(usuarioId);
        msg.setOrgaoJulgadorId(orgaoJulgadorId);
        msg.setExcluirAposNotificacao(false);
        mensagemRepository.save(msg);
        return true;
    }

    public boolean salvar(String titulo, String mensagem, Object objeto, Timestamp dataLimiteParaEntrega, String usuarioId) throws IOException {
        Mensagem msg = montaObjeto(titulo, mensagem);
        ObjectMapper mapper = new ObjectMapper();
        msg.setObjeto(mapper.writeValueAsString(objeto));
        msg.setDataLimite(dataLimiteParaEntrega);
        msg.setUsuarioId(usuarioId);
        mensagemRepository.save(msg);
        return true;
    }

    public boolean salvar(String titulo, String mensagem, Object objeto, Timestamp dataLimiteParaEntrega) throws IOException {
        Mensagem msg = montaObjeto(titulo, mensagem);
        ObjectMapper mapper = new ObjectMapper();
        msg.setObjeto(mapper.writeValueAsString(objeto));
        msg.setDataCadastro(dataLimiteParaEntrega);
        mensagemRepository.save(msg);
        return true;
    }

    public boolean salvar(String titulo, String mensagem, Object objeto) throws IOException {
        Mensagem msg = montaObjeto(titulo, mensagem);
        ObjectMapper mapper = new ObjectMapper();
        msg.setObjeto(mapper.writeValueAsString(objeto));
        mensagemRepository.save(msg);
        return true;
    }

    public boolean salvar(String titulo, String mensagem) throws IOException {
        Mensagem msg = montaObjeto(titulo, mensagem);
        mensagemRepository.save(msg);
        return true;
    }

    public boolean salvar(Mensagem msg) throws IOException {
        mensagemRepository.save(msg);
        return true;
    }

    private Mensagem montaObjeto(String titulo, String mensagem) {
        Mensagem msg = new Mensagem();
        msg.setTitulo(titulo);
        msg.setMensagem(mensagem);
        msg.setExcluirAposNotificacao(false);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        msg.setDataCadastro(timestamp);
        return msg;
    }

    public boolean excluir(Long id) {
        mensagemRepository.deleteById(id);
        return true;
    }

    public boolean enviado(Long id) {
        Mensagem mensagem = mensagemRepository.findById(id).orElseThrow(NullPointerException::new); // ToDo tratar a falta desse recurso
        mensagem.setEnviado(true);
        mensagemRepository.save(mensagem);
        return true;
    }

    public boolean visualizado(Long id) {
        Mensagem mensagem = mensagemRepository.findById(id).orElseThrow(NullPointerException::new); // ToDo tratar a falta desse recurso
        mensagem.setVisualizado(true);
        mensagemRepository.save(mensagem);
        return true;
    }

    public List<Mensagem> Buscar(String usuarioId) {
        return mensagemRepository.findByUsuario(usuarioId);
    }

    public List<Mensagem> BuscarParaEnvio(String usuarioId) {
        return mensagemRepository.findByParaEnvio(usuarioId);
    }


}

