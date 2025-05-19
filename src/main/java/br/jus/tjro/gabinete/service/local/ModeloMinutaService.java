package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.ModeloMinuta;
import br.jus.tjro.gabinete.model.gab.transiente.ModeloMinutaCabecalhoRodape;
import br.jus.tjro.gabinete.repository.remoto.ModeloDocumentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class ModeloMinutaService {

    @Autowired
    private ModeloDocumentoRepository modeloMinutaRepository;

    @Autowired
    private OrgaoJulgadorService orgaoJulgadorService;

    @Value("${tribunal:''}")
    private String tribunal;

    private final Logger looger = LoggerFactory.getLogger(ModeloMinutaService.class);

    private Boolean pertenceAoOrgaoJulgador(ModeloMinuta modelo, Usuario usuarioLogado) {
        boolean result = usuarioLogado.getOrgaosJulgadores().contains(modelo.getIdOrgaoJulgador());
        return result;
    }

    public ModeloMinutaCabecalhoRodape getCabecalhoERodape() {
        ModeloMinutaCabecalhoRodape cabecalhoRodape = new ModeloMinutaCabecalhoRodape();
        String cabeca = "";
        String localHtml= "/cabecalho"+tribunal+".html";

        try (InputStream inputStream = getClass().getResourceAsStream(localHtml);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            cabeca = reader.lines()
                .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            cabeca = "Não foi possivel ler o arquivo de cabeçalho";
            looger.error(cabeca);
        }
        cabecalhoRodape.setCabecalho(cabeca);
        cabecalhoRodape.setRodape("");
        return cabecalhoRodape;
    }

    public ModeloMinuta copiarParaUsuario(String modeloId, Long orgaoJulgadorId, Usuario usuario) throws Exception {
        ModeloMinuta novoModelo = this.modeloMinutaRepository
            .findById(modeloId,usuario)
            .map(ModeloMinuta::clonar)
            .orElseThrow(NullPointerException::new); // ToDo tratar a falta desse recurso
        novoModelo.setId(null);
        novoModelo.setAtualizacao(new Date());
        novoModelo.setCpfAtualizacao(usuario.getId());
        novoModelo.meu = true;
        return novoModelo;
    }
}
