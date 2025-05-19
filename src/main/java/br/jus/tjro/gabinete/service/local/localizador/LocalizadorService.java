package br.jus.tjro.gabinete.service.local.localizador;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.enums.TipoDeLocalizadorEnum;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorCaixa;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorOrgaoJulgador;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorRegra;
import br.jus.tjro.gabinete.model.gab.localizador.RegraWrapper;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.transiente.LocalizadorAgrupadoItem;
import br.jus.tjro.gabinete.repository.gab.localizador.caixa.LocalizadorCaixaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class LocalizadorService {

    private final LocalizadorUsuarioService localizadorUsuarioService;
    private final LocalizadorWrapperService localizadorWrapperService;
    private final LocalizadorCaixaRepository localizadorRepository;
    private final LocalizadorOrgaoJulgadorService localizadorOrgaoJulgadorService;


    @Autowired
    public LocalizadorService(LocalizadorUsuarioService localizadorUsuarioService,
                              LocalizadorWrapperService localizadorWrapperService,
                              LocalizadorOrgaoJulgadorService localizadorOrgaoJulgadorService,
                              LocalizadorCaixaRepository localizadorRepository){
        this.localizadorUsuarioService = localizadorUsuarioService;
        this.localizadorWrapperService = localizadorWrapperService;
        this.localizadorRepository = localizadorRepository;
        this.localizadorOrgaoJulgadorService = localizadorOrgaoJulgadorService;
    }

    public LocalizadorCaixa salvaLocalizador(LocalizadorCaixa localizadorCaixa) {
        localizadorOrgaoJulgadorService.verificaSeExisteLocOrgaoJulgadorEhSalva(localizadorCaixa.getOrgaosJulgadores());
        localizadorUsuarioService.verificaSeExisteLocUsuarioEhSalva(localizadorCaixa.getUsuarios());
        return localizadorRepository.save(localizadorCaixa);
    }

    public List<LocalizadorCaixa> findByUsuarioIdAndOrgaoJulgador(Usuario usuario, String orgaojulgador) {
        return localizadorRepository.findByUsuario(usuario.getId(), orgaojulgador);
    }

    public List<LocalizadorCaixa> findByUsuario(Usuario usuario, String orgaojulgador) {
        return localizadorRepository.findByUsuario(usuario.getId(), orgaojulgador);
    }

    public void excluir(Long id, String orgaoJulgador, Usuario usuario) throws Exception {
        LocalizadorCaixa lc = localizadorRepository.findById(id).orElseThrow(NullPointerException::new); // ToDo tratar a falta desse recurso
        if (lc.getTipo().equals(TipoDeLocalizadorEnum.Sistema)) {
            throw new Exception("Não é possivel excluir um localizador de sistema");
        }else
            localizadorRepository.deleteById(id);
    }

    public LocalizadorCaixa findById(Long id) {
        return localizadorRepository.findById(id).orElseThrow(NullPointerException::new); // ToDo tratar a falta desse recurso
    }

    private List<LocalizadorAgrupadoItem> converteObjectParaLocalizadorAgrupadoItem(List<Object[]> objects)
        throws IllegalAccessException, NoSuchFieldException {

        List<LocalizadorAgrupadoItem> lista = new ArrayList<>();
        for (Object[] o : objects) {
            LocalizadorAgrupadoItem item = new LocalizadorAgrupadoItem(Long.parseLong(o[0].toString()),
                Long.parseLong(o[1].toString()), (String) o[2], Long.parseLong(o[3].toString()));
            lista.add(item);
        }
        return lista;
    }

    public void verificaSeExisteLocalizadorCaixaPorNomeContendoEOrgaoJulgadorSeNaoCria(String nome,
                                                                                       Processo processo,
                                                                                       Long idTagRegra)
        throws IOException {
        Optional<LocalizadorOrgaoJulgador> loj = localizadorOrgaoJulgadorService.findByOrgaoJulgadorId(
            processo.getOrgaoJulgador());
        LocalizadorOrgaoJulgador lojSalvo;
        if (loj.isEmpty()) {
            Set<LocalizadorOrgaoJulgador> localizadorOrgao = new HashSet();
            LocalizadorOrgaoJulgador lojASalvar = new LocalizadorOrgaoJulgador(processo.getOrgaoJulgador());
            localizadorOrgao.add(lojASalvar);
            localizadorOrgaoJulgadorService.verificaSeExisteLocOrgaoJulgadorEhSalva(localizadorOrgao);
            lojSalvo = lojASalvar;
        } else lojSalvo = loj.get();
        LocalizadorCaixa localizadorCaixa =
            localizadorRepository.findTop1ByNomeContainingIgnoreCaseAndOrgaosJulgadoresContaining(
                nome, lojSalvo);
        if(idTagRegra != null) {
            if (localizadorCaixa == null) {
                localizadorCaixa = new LocalizadorCaixa();
                Set<LocalizadorOrgaoJulgador> localizadorOrgao = new HashSet();
                localizadorOrgao.add(lojSalvo);
                localizadorCaixa.setOrgaosJulgadores(localizadorOrgao);
                localizadorCaixa.setNome(nome);
                localizadorCaixa.setTipo(TipoDeLocalizadorEnum.OrgaoJulgador);
                localizadorCaixa.setExclusivo(false);
                localizadorCaixa.setUsuarios(new HashSet<>());
            }
            RegraWrapper regraWrapperCodex = new RegraWrapper("tags.tag.id", "=", idTagRegra.toString());
            List<RegraWrapper> regrasWrapper = new ArrayList<>();

            boolean adicionaRegraCodex = true;
            LocalizadorRegra localizadorRegra = localizadorCaixa.getRegra();
            if(localizadorRegra != null) {
                if (localizadorRegra.getParametro().contains(regraWrapperCodex.getTipo())
                    && localizadorRegra.getParametro().equals(regraWrapperCodex.getOperador())
                    && localizadorRegra.getParametro().contains(regraWrapperCodex.getValor()))
                    adicionaRegraCodex = false;
            } else localizadorRegra = new LocalizadorRegra();
            if(adicionaRegraCodex) {
                regrasWrapper.add(regraWrapperCodex);
                /* COMO PEGAR AS REGRAS JÁ EXISTENTES PRA PODER TRANSFORMAR EM PARÂMETROS ADICIONANDO A REGRA DO CODEX?*/
                localizadorRegra.setParametro(localizadorWrapperService.processaRegraParaParametro(regrasWrapper));
                localizadorCaixa.setRegra(localizadorRegra);
                this.salvaLocalizador(localizadorCaixa);
            }
        }
    }
}


