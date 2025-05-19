package br.jus.tjro.gabinete.controller.processo;


import br.jus.tjro.gabinete.model.gab.processo.*;
import br.jus.tjro.gabinete.repository.gab.filter.ProcessoParteExpedienteFilter;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.service.local.ProcessoParteExpedienteService;
import io.minio.http.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("expedientes")
public class ProcessoExpedienteController {

    @Autowired
    ProcessoParteExpedienteService processoParteExpedienteService;

    @Autowired
    ProcessosRepository processosRepository;

    @GetMapping("/processo/{id}")
    public ResponseEntity<List<ProcessoParteExpediente>> getExpedientes(@PathVariable("id") Long idProcesso) throws Exception {
        Processo processo = processosRepository.findById(idProcesso).get();
//        processoParteExpedienteService.importaOuAtualizaExpedientesDoProcesso(processo);
        List<ProcessoParteExpediente> expedientes = processoParteExpedienteService.buscaExpedientesDoProcesso(processo);
        return ResponseEntity.ok().body(expedientes);
    }

    @GetMapping("/lista/processo/{id}")
    public Page<ProcessoParteExpediente> listaExpedientesPaginada(@PathVariable("id") Long idProcesso, ProcessoParteExpedienteFilter processoParteExpedienteFilter, Pageable pageable, @QueryParam("query") String query) throws Exception {
        Processo processo = processosRepository.findById(idProcesso).get();
        Page<ProcessoParteExpediente> expedientes = processoParteExpedienteService.listaPaginada(processo, processoParteExpedienteFilter, pageable, query);
        return expedientes;
    }

    @GetMapping("/importar/{id}")
    public boolean importarExpedientes(@PathVariable("id") Long idProcesso) throws Exception {
        Processo processo = processosRepository.findById(idProcesso).get();
        processoParteExpedienteService.importaOuAtualizaExpedientesDoProcesso(processo);
        return true;
    }
}
