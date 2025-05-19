package br.jus.tjro.gabinete.controller;


import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.listener.model.processo.listener.LocalizadorListener;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.Version;
import br.jus.tjro.gabinete.model.gab.VersionEnvironment;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum.*;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpMethod.GET;


@RestController
public class IndexController {

    private final VersionEnvironment versionEnvironment;
    private final RestTemplate template;
    private final List<FonteDados> fontes;
    private final AuthenticationUsuarioService auth;

    private final ProcessosRepository processoRepo;
    private final KafkaProducerService producer;

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // This code protects Spring Core from a "Remote Code Execution" attack (dubbed "Spring4Shell").
        // By applying this mitigation, you prevent the "Class Loader Manipulation" attack vector from firing.
        // For more details, see this post: https://www.lunasec.io/docs/blog/spring-rce-vulnerabilities/
        String[] blackList = {"class.*","Class.*","*.class.*",".*Class.*"};
        binder.setDisallowedFields(blackList);
    }

    @Autowired
    public IndexController(VersionEnvironment versionEnvironment, List<FonteDados> fontes,
                           RestTemplate template, AuthenticationUsuarioService auth,
                           ProcessosRepository processoRepo, KafkaProducerService producer) {
        this.versionEnvironment = versionEnvironment;
        this.fontes = fontes;
        this.template = template;
        this.auth = auth;
        this.processoRepo = processoRepo;
        this.producer = producer;
    }

    @GetMapping("/version")
    public ResponseEntity<Version> getVersao() {
        return ResponseEntity.ok(versionEnvironment.getVersion());
    }

    @GetMapping("/whoim")
    public ResponseEntity<Usuario> getUsuario(Authentication usuario) {
        return ResponseEntity.ok(auth.getUsuario(usuario));
    }

    @GetMapping("/versions")
    public ResponseEntity<List<Version>> getVersoes() {
        HttpHeaders headers = new HttpHeaders() {{
            put("Content-Type", singletonList("application/json"));
        }};
        var urls = Stream.of(PJEPG.selecionaFonte(fontes), PJESG.selecionaFonte(fontes)).map(f -> f.getUrlBase() + "/versions").collect(toList());
        logger.info("E2E - Pegando versões da aplicação " + urls.stream().collect(joining(",")));

        var versionsServices = urls.stream().flatMap(url -> Arrays.stream(this.template.exchange(url, GET, new HttpEntity<>(headers), Version[].class).getBody())).collect(toList());
        var versions = new ArrayList<>(of(versionEnvironment.getVersion()));
        versions.addAll(versionsServices);
        return ResponseEntity.ok(versions);
    }
}
