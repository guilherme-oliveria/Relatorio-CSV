package br.jus.tjro.gabinete.service.local.localizador;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.enums.TipoDeLocalizadorEnum;
import br.jus.tjro.gabinete.model.gab.localizador.*;
import br.jus.tjro.gabinete.repository.gab.localizador.caixa.LocalizadorCaixaRepository;
import br.jus.tjro.gabinete.util.RegraWrapperComparator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class LocalizadorWrapperService {

    private final LocalizadorCaixaRepository localizadorCaixaRepository;
    private final ValidaLocalizador validaLocalizador;

    @Autowired
    public LocalizadorWrapperService(LocalizadorCaixaRepository localizadorCaixaRepository, ValidaLocalizador validaLocalizador) {
        this.localizadorCaixaRepository = localizadorCaixaRepository;
        this.validaLocalizador = validaLocalizador;
    }

    public LocalizadorCaixa wrapperForLocalizadorCaixa(LocalizadorWrapper localizadorWrapper, Usuario usuario)
        throws Exception {

        String parametro = processaRegraParaParametro(localizadorWrapper.getRegras());

        validaLocalizador.validaLocalizadorAntesDeSalvar(parametro, localizadorWrapper, usuario);

        LocalizadorCaixa lc = new LocalizadorCaixa();
        if (localizadorWrapper.getId() != null) {
            lc = localizadorCaixaRepository.findById(localizadorWrapper.getId()).orElseThrow(NullPointerException::new); //ToDo tratar quando não existir o recurso
        }
        lc.setNome(localizadorWrapper.getNome());

        wrapperForLocalizadorCaixaToOrgaoJulgador(localizadorWrapper, lc);

        wrapperForLocalizadorCaixaToUsuarios(usuario, lc);

        LocalizadorRegra lr = wrapperForLocalizadorCaixaToRegras(lc);

        lr.setParametro(parametro);
        lc.setExclusivo(localizadorWrapper.isExclusivo());
        if (localizadorWrapper.isDeOrgaoJulgador()) {
            lc.setTipo(TipoDeLocalizadorEnum.OrgaoJulgador);
        } else {
            lc.setTipo(TipoDeLocalizadorEnum.Usuario);
        }
        return lc;
    }

    private LocalizadorRegra wrapperForLocalizadorCaixaToRegras(LocalizadorCaixa lc) {
        LocalizadorRegra lr = new LocalizadorRegra();
        if (lc.getRegra() != null && lc.getRegra().getId() != null) {
            lr.setId(lc.getRegra().getId());
        }
        lc.setRegra(lr);
        return lr;
    }

    private void wrapperForLocalizadorCaixaToUsuarios(Usuario usuario, LocalizadorCaixa lc) {
        Set<LocalizadorUsuario> localizadorUsuarios = new HashSet();

        LocalizadorUsuario lou = new LocalizadorUsuario(usuario);
        localizadorUsuarios.add(lou);

        lc.setUsuarios(localizadorUsuarios);
    }

    private void wrapperForLocalizadorCaixaToOrgaoJulgador(LocalizadorWrapper localizador, LocalizadorCaixa lc) {
        Set<LocalizadorOrgaoJulgador> localizadorOrgao = new HashSet();
        LocalizadorOrgaoJulgador loj = new LocalizadorOrgaoJulgador(localizador.getOrgaoJulgador());
        localizadorOrgao.add(loj);
        lc.setOrgaosJulgadores(localizadorOrgao);
    }

    public String processaRegraParaParametro(List<RegraWrapper> regras) throws IOException {

        ArrayList<Regra> regrasRetorno = new ArrayList<Regra>();
        RegraWrapperComparator comparador = new RegraWrapperComparator();
        Collections.sort(regras, comparador);
        for (RegraWrapper regra : regras) {
            Regra r = new Regra();
            r.setOperador(regra.getOperador());
            r.setParametro(regra.getTipo());
            r.setValor(processaValor(regra));
            regrasRetorno.add(r);
        }
        return new ObjectMapper().writeValueAsString(regrasRetorno);

    }

    private Object processaValor(RegraWrapper regra) {
        List<String> filtrosBooleanos = Arrays.asList("possuiLiminar", "prioridade", "segredoJustica",
            "justicaGratuita");
        List<String> filtroTPU = Arrays.asList("processoAssuntos.idAssunto", "idClasseJudicial", "movimentos");
        if (filtrosBooleanos.contains(regra.getTipo())) {

            if (regra.getValor().equalsIgnoreCase("sim") || regra.getValor().equalsIgnoreCase("true")) {
                return true;
            }
            if (regra.getValor().equalsIgnoreCase("não") || regra.getValor().equalsIgnoreCase("false")) {
                return false;
            }
        } else if (filtroTPU.contains(regra.getTipo())) {
            return regra.getTpu().getCodigo();
        }
        return regra.getValor();
    }

}
