package br.jus.tjro.gabinete.controller.minuta;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.listener.model.processo.listener.LocalizadorListener;
import br.jus.tjro.gabinete.model.gab.ParametrosEnvioArquivosParaAssinaturaTjOffice;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.enums.TipoDocumentoAssinatura;
import br.jus.tjro.gabinete.model.gab.minuta.PedidoInclusaoPauta;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.scheduled.devolve.origem.DevolveOrigem;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.assinaturadigital.RetornoAssinaturaService;
import br.jus.tjro.gabinete.service.local.AssinaturaService;
import br.jus.tjro.gabinete.service.local.PedidoInclusaoPautaService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.jus.tjro.gabinete.Tarefas.core.TarefaEnum.ParaIntegracaoPauta;

@Controller
@RequestMapping("assinatura")
public class AssinaturaController {

    @Autowired
    private AssinaturaService assinaturaService;
    @Autowired
    private RetornoAssinaturaService retornoAssinaturaService;

    @Autowired
    private AuthenticationUsuarioService auth;
    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private PedidoInclusaoPautaService pedidoInclusaoPautaService;

    private final ProcessosRepository processosRepository;
    private final LocalizadorListener localizadorListener;
    private final Logger looger = LoggerFactory.getLogger(AssinaturaController.class);

    @Autowired
    public AssinaturaController(ProcessosRepository processosRepository, LocalizadorListener localizadorListener) {
        this.processosRepository = processosRepository;
        this.localizadorListener = localizadorListener;
    }


    @PostMapping("/")
    @PreAuthorize("hasAuthority('MAGISTRADO')")
    public ResponseEntity<String> assinarDocumentos(@RequestParam("arquivo") MultipartFile file,
                                                    @RequestParam("id") Long id,
                                                    @RequestParam("tipoDocumento") TipoDocumentoAssinatura tipoDocumento,
                                                    Authentication usuario) throws Exception {
        Processo processo = retornoAssinaturaService.assinaturaDetached(id, file, tipoDocumento, auth.getUsuario(usuario));
        if(processo.getTarefa().isEnviaMsgDevolveOrigem())
            kafkaProducerService.send(DevolveOrigem.TOPIC_DEVOLVE_ORIGEM,processo);
        return ResponseEntity.ok().body("ok");
    }

    @GetMapping("/arquivos-assinatura/{idProceso}")
    @PreAuthorize("hasAuthority('MAGISTRADO')")
    public ResponseEntity<List<ParametrosEnvioArquivosParaAssinaturaTjOffice>> processoParaAssinatura(
        @PathVariable("idProceso") Long idProcesso) throws Exception {
        Processo processo = processosRepository.findById(idProcesso)
            .orElseThrow(() -> new NotFoundException("Processo não encontrado"));
        List<ParametrosEnvioArquivosParaAssinaturaTjOffice> arquivos;
        arquivos = assinaturaService.parametrosBuilder(processo);
        return ResponseEntity.ok().body(arquivos);
    }

    @GetMapping("/remover/{id}")
    @PreAuthorize("hasAuthority('MAGISTRADO')")
    public ResponseEntity<Boolean> removerAssinatura(@PathVariable Long id,Authentication usuario) throws Exception {
        return ResponseEntity.ok().body(retornoAssinaturaService.removerAssinatura(id,auth.getUsuario(usuario)));
    }

    @GetMapping("/stream/{idProcesso}/pedido-inclusao-pauta")
    @Transactional
    public ResponseEntity<Resource> recuperarBinarioParaAssinaturaDocumentoHtml(
        @PathVariable("idProcesso") Long idProcesso,
        Authentication auth) throws Exception {
        Usuario usuario = this.auth.getUsuario(auth);
        Processo processo = processosRepository.findById(idProcesso)
            .orElseThrow(() -> new NotFoundException("Não foi possível localizar o processo"));
        PedidoInclusaoPauta pauta = pedidoInclusaoPautaService.render(processo.minutaEmElaboracao().get().getPedidoInclusaoPauta().get(), usuario, true);
        InputStreamResource resource = new InputStreamResource(pauta.getHtmlRenderizadoInputStream());
        return ResponseEntity.ok().body(resource);
    }

    @PostMapping("/arquivos-assinatura")
    @PreAuthorize("hasAuthority('MAGISTRADO')")
    public ResponseEntity<List<ParametrosEnvioArquivosParaAssinaturaTjOffice>> processosParaAssinatura(
        @RequestBody List<Long> idsProcesso) throws Exception {
        Iterable<Processo> processos = processosRepository.findAllById(idsProcesso);
        List<ParametrosEnvioArquivosParaAssinaturaTjOffice> arquivos;
        arquivos = assinaturaService.parametrosBuilder(processos);
        return ResponseEntity.ok().body(arquivos);
    }

    @PostMapping("/assina-recusa-arquivos")
    @PreAuthorize("hasAuthority('MAGISTRADO')")
    public ResponseEntity<List<ParametrosEnvioArquivosParaAssinaturaTjOffice>> recusaAssinaturaDocumentos(
        @RequestBody AssinarResucarPayload payload) {
        payload.getIdsRecusar()
            .map(processosRepository::findAllById)
            .ifPresent(processos -> {
                processos.forEach(Processo::recusar);
                Iterable<Processo> retorno = processosRepository.saveAll(processos);
                localizadorListener.send(retorno);
            });
        var arquivos = payload.getIdsAssinar()
            .map(processosRepository::findAllById).stream()
            .flatMap(processos -> assinaturaService.parametrosBuilder(processos).stream())
            .collect(Collectors.toList());
        return ResponseEntity.ok().body(arquivos);
    }

    static class AssinarResucarPayload {
        private List<Long> processosAssinar = new ArrayList<>();
        private List<Long> processosRecusar = new ArrayList<>();

        private Optional<List<Long>> getIdsAssinar() {
            return empty(processosAssinar);
        }

        private Optional<List<Long>> getIdsRecusar() {
            return empty(processosRecusar);
        }

        private Optional<List<Long>> empty(List<Long> list) {
            if(list == null || list.isEmpty()) return Optional.empty();
            return Optional.of(list);
        }

        public void setProcessosAssinar(List<Long> processosAssinar) {
            this.processosAssinar = processosAssinar;
        }

        public void setProcessosRecusar(List<Long> processosRecusar) {
            this.processosRecusar = processosRecusar;
        }
    }

    @GetMapping("/assinatura-com-erro")
    public ResponseEntity<Boolean> assinaturaComErro(@RequestHeader("oj") String idOJ) {
        Boolean ahErro = assinaturaService.assinaturaComErro(idOJ);
        return ResponseEntity.ok().body(ahErro);
    }

    @GetMapping("/hora")
    public ResponseEntity<Long> getHora() {
        long horaAtual = System.currentTimeMillis();
        return ResponseEntity.ok(horaAtual);
    }


    @GetMapping("/arquivos-assinatura-mobile/{idProceso}")
    @PreAuthorize("hasAuthority('MAGISTRADO')")
    public ResponseEntity<Boolean> processoParaAssinaturaMobile(@PathVariable("idProceso") Long idProcesso, Authentication auth) throws Exception {
        Usuario usuario = this.auth.getUsuario(auth);
        Processo processo = processosRepository.findById(idProcesso)
            .orElseThrow(() -> new NotFoundException("Processo não encontrado"));
        Processo processoAtt = assinaturaService.assinaturaDetached(processo,usuario);
        if(processoAtt.getTarefa().isEnviaMsgDevolveOrigem())
            kafkaProducerService.send(DevolveOrigem.TOPIC_DEVOLVE_ORIGEM,processoAtt);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/arquivos-assinatura-mobile")
    @PreAuthorize("hasAuthority('MAGISTRADO')")
    public ResponseEntity<Boolean> processosParaAssinaturaMobile(@RequestBody List<Long> idsProcesso, Authentication auth) throws Exception {
        Iterable<Processo> processos = processosRepository.findAllById(idsProcesso);
        Usuario usuario = this.auth.getUsuario(auth);
        Boolean assinou = assinaturaService.processosAssinarMobile(processos,usuario);
        return ResponseEntity.ok().body(assinou);
    }


    @PostMapping("/assina-recusa-arquivos-mobile")
    @PreAuthorize("hasAuthority('MAGISTRADO')")
    public ResponseEntity<Boolean> recusaAssinaturaDocumentosMobile(
        @RequestBody AssinarResucarPayload payload, Authentication auth) {
        Usuario usuario = this.auth.getUsuario(auth);
        payload.getIdsRecusar()
            .map(processosRepository::findAllById)
            .ifPresent(processos -> {
                processos.forEach(Processo::recusar);
                Iterable<Processo> retorno = processosRepository.saveAll(processos);
                localizadorListener.send(retorno);
            });
        var assinou = payload.getIdsAssinar()
            .map(processosRepository::findAllById)
            .stream()
            .allMatch(processos -> assinaturaService.processosAssinarMobile(processos, usuario));

        return ResponseEntity.ok().body(assinou);
    }

}

