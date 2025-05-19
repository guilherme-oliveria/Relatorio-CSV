package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.minuta.Anotacao;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.repository.gab.minuta.AnotacaoRepository;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static br.jus.tjro.gabinete.model.gab.minuta.Anotacao.ordenaPorDataCriacaoMaisRecentePrimeiro;

@Service
public class AnotacaoService {

    private final AuthenticationUsuarioService auth;

    private final AnotacaoRepository anotacaoRepository;

    @Autowired
    public AnotacaoService(AnotacaoRepository anotacaoRepository, AuthenticationUsuarioService auth){
        this.auth = auth;
        this.anotacaoRepository = anotacaoRepository;

    }

    public Anotacao salvaAnotacaoProcesso(Anotacao anotacao, Authentication user) {
        Usuario userAuth = this.auth.getUsuario(user);
        anotacao.setCpfUsuario(userAuth.getCpf());
        anotacao.setUsuarioNome(userAuth.getNome());
        anotacao.setDataCadastro(new Date());
        return anotacaoRepository.save(anotacao);
    }

    public void excluiAnotacaoProcesso(Long anotacaoId) {
        anotacaoRepository.deleteById(anotacaoId);
    }

    public List<Anotacao> obtemListaAnotacoesPrivadasDoUsuario(Long idProcesso, String cpfUsuario) {
        return anotacaoRepository.obtemListaAnotacoesPrivadasDoUsuario(idProcesso, cpfUsuario);
    }

    public List<Anotacao> obtemListaAnotacoesPublicasDoProcesso(Long idProcesso) {
        return anotacaoRepository.obtemListaAnotacoesPublicasDoProcesso(idProcesso);
    }

    public List<Anotacao> obtemListaDeAnotacoes(Long idProcesso, String cpf) {
        List<Anotacao> anotacoesPrivadas = this.obtemListaAnotacoesPrivadasDoUsuario(idProcesso, cpf);
        List<Anotacao> anotacoesPublicas = this.obtemListaAnotacoesPublicasDoProcesso(idProcesso);

        ArrayList<Anotacao> anotacoes = new ArrayList<>();
        anotacoes.addAll(anotacoesPrivadas);
        anotacoes.addAll(anotacoesPublicas);
        return ordenaPorDataCriacaoMaisRecentePrimeiro(anotacoes);
    }
}

