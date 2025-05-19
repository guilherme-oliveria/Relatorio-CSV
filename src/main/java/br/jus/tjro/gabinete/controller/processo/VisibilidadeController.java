package br.jus.tjro.gabinete.controller.processo;


import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.model.gab.processo.Visibilidade;
import br.jus.tjro.gabinete.model.gab.transiente.Partes;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.ProcessoParteService;
import br.jus.tjro.gabinete.service.local.ProcessoService;
import br.jus.tjro.gabinete.service.remoto.VisibilidadeRemotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/visibilidade")
public class VisibilidadeController {

    private final ProcessoService processoService;
    private final VisibilidadeRemotoService visibilidadeRemotoService;
    private final AuthenticationUsuarioService authService;

    @Autowired
    public VisibilidadeController(ProcessoService processosRepository,
                                  AuthenticationUsuarioService authService,
                                  VisibilidadeRemotoService visibilidadeRemotoService){
        this.processoService = processosRepository;
        this.visibilidadeRemotoService = visibilidadeRemotoService;
        this.authService = authService;
    }

    @GetMapping("/{idProcesso}")
    public ResponseEntity<List<Visibilidade>> getByProcesso(@PathVariable("idProcesso") Long idProcesso, Authentication usuario) throws Exception {
        Usuario usuarioObj = authService.getUsuario(usuario);
        Optional<Processo> processo = processoService.findById(idProcesso);
        return ResponseEntity.ok().body(visibilidadeRemotoService.getByProcesso(processo.get(), usuarioObj));
    }

    @PreAuthorize("hasAuthority('MAGISTRADO')")
    @PostMapping("/{idProcesso}")
    public void save(@PathVariable("idProcesso") Long idProcesso, @RequestBody List<Long> idsPessoasLegado) {
        Optional<Processo> processo = processoService.findById(idProcesso);
        visibilidadeRemotoService.save(processo.get(),idsPessoasLegado);
    }

    @PreAuthorize("hasAuthority('MAGISTRADO')")
    @PostMapping("/partes/{idProcesso}")
    public void savePartes(@PathVariable("idProcesso") Long idProcesso) {
        Optional<Processo> processo = processoService.findById(idProcesso);
        visibilidadeRemotoService.savePartes(processo.get());
    }

    @PreAuthorize("hasAuthority('MAGISTRADO')")
    @PostMapping("/orgao/{idProcesso}")
    public void saveOrgao(@PathVariable("idProcesso") Long idProcesso) {
        Optional<Processo> processo = processoService.findById(idProcesso);
        visibilidadeRemotoService.saveOrgao(processo.get());
    }

    @PreAuthorize("hasAuthority('MAGISTRADO')")
    @PostMapping("/orgao-colegiado/{idProcesso}")
    public void saveOrgaoColegiado(@PathVariable("idProcesso") Long idProcesso) {
        Optional<Processo> processo = processoService.findById(idProcesso);
        visibilidadeRemotoService.saveOrgaoColegiado(processo.get());
    }

    @DeleteMapping("/{idProcesso}/{idVisibilidadeLegado}")
    @PreAuthorize("hasAuthority('MAGISTRADO')")
    public void delete(@PathVariable("idProcesso") Long idProcesso, @PathVariable("idVisibilidadeLegado") Long idVisibilidade) throws Exception {
        Optional<Processo> processo = processoService.findById(idProcesso);
        visibilidadeRemotoService.delete(processo.get(),idVisibilidade);
    }
}
