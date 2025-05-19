package br.jus.tjro.gabinete.service.local.localizador;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.localizador.*;
import br.jus.tjro.gabinete.repository.gab.localizador.FiltroRepository;
import br.jus.tjro.gabinete.repository.gab.localizador.LocalizadorOrgaoJulgadorRepository;
import br.jus.tjro.gabinete.repository.gab.localizador.LocalizadorUsuarioRepository;
import br.jus.tjro.gabinete.util.RegraWrapperComparator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

//import com.google.gson.Gson;

@Service
public class FiltroService {

    @Autowired
    FiltroRepository filtroRepository;
    @Autowired
    LocalizadorOrgaoJulgadorRepository orgaoJulgadorRepository;
    @Autowired
    LocalizadorUsuarioRepository usuarioRepository;


    public Filtro salvar(FiltroWrapper filtro, Usuario usuario) throws Exception {
        String parametro = processaRegraParaParametro(filtro.getRegras());
        Long filtroId = Long.valueOf(-1);
        if (filtro.getId() != null) {
            filtroId = filtro.getId();
        }
        List<Filtro> filtroSalvos = filtroRepository.findByParametroUsuario(parametro, usuario.getId(), filtroId, filtro.getOrgaoJulgador());
        if (filtroSalvos != null && filtroSalvos.size() > 0) {
            throw new Exception("Já existe um filtro cadastrado com a mesma regra (" + filtroSalvos.get(0).getNome() + ")");
        } else {
            List<Filtro> filt = filtroRepository.findByNomeAndUsuarioAndOrgaosJulgadores(filtro.getNome(), usuario.getId(), filtro.getOrgaoJulgador(), filtroId);
            if (filt != null && filt.size() > 0) {
                throw new Exception("Já existe um filtro cadastrado com o mesmo nome (" + filt.get(0).getNome() + ")");
            }
            Filtro ft = new Filtro();
            if (filtro.getId() != null) {
                ft = filtroRepository.findById(filtro.getId()).orElseThrow(NullPointerException::new); // ToDo tratar a falta desse recurso
            }
            ft.setNome(filtro.getNome());
            ArrayList<LocalizadorOrgaoJulgador> localizadorOrgao = new ArrayList<>();
            Optional<LocalizadorOrgaoJulgador> loj = orgaoJulgadorRepository.findById(filtro.getOrgaoJulgador());
            final LocalizadorOrgaoJulgador localizadorOrgaoJulgador = loj.orElse(orgaoJulgadorRepository.save(new LocalizadorOrgaoJulgador(filtro.getOrgaoJulgador())));
            localizadorOrgao.add(localizadorOrgaoJulgador);
            ft.setOrgaosJulgadores(localizadorOrgao);
            ArrayList<LocalizadorUsuario> localizadorUsuarios = new ArrayList<>();
            Optional<LocalizadorUsuario> lou = usuarioRepository.findById(usuario.getId());
            final LocalizadorUsuario localizadorUsuario = lou.orElse(usuarioRepository.save(new LocalizadorUsuario(usuario)));
            localizadorUsuarios.add(localizadorUsuario);
            ft.setUsuarios(localizadorUsuarios);
            ft.setParametro(parametro);
            return filtroRepository.save(ft);
        }
    }

    private String processaRegraParaParametro(List<RegraWrapper> regras) throws IOException {

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
        List<String> filtrosBooleanos = Arrays.asList("possuiLiminar", "prioridade", "segredoJustica", "justicaGratuita");
        List<String> filtroTPU = Arrays.asList("assuntoPrincipal", "idClasseJudicial", "movimentos");
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

    public List<Filtro> findByUsuarioIdAndOrgaoJulgador(String usuario, String orgaojulgador) {
        //Desativacao de funcionalidade de salvar filtro;
        return new ArrayList<>();
    }

    public boolean excluir(Long id, String orgaoJulgador, Usuario usuario) throws Exception {
        Filtro ft = filtroRepository.findById(id).orElseThrow(NullPointerException::new); // ToDo tratar a falta desse recurso
        if (ft.getUsuarios().stream().filter(x -> x.getId().equals(usuario.getId())).count() > 0) {
            if (ft.getOrgaosJulgadores().stream().filter(x -> x.getId().equals(orgaoJulgador)).count() > 0) {
                if (ft.getUsuarios().size() > 1 || ft.getOrgaosJulgadores().size() > 1) {
                    ArrayList<LocalizadorUsuario> listaUsuario = (ArrayList<LocalizadorUsuario>) ft.getUsuarios();
                    listaUsuario.remove(usuario.getId());
                    ft.setUsuarios(listaUsuario);
                    filtroRepository.save(ft);
                } else {
                    filtroRepository.deleteById(id);
                }
            }
        }

        return true;
    }

    public Filtro findById(Long id) {
        return filtroRepository.findById(id).orElseThrow(NullPointerException::new); // ToDo tratar a falta desse recurso
    }

}

