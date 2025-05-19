package br.jus.tjro.gabinete.controller.processo;


import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.pessoa.PessoaDocumento;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParteEndereco;
import br.jus.tjro.gabinete.model.gab.enums.TipoPolo;
import br.jus.tjro.gabinete.model.gab.transiente.Partes;
import br.jus.tjro.gabinete.service.local.PessoaDocumentoService;
import br.jus.tjro.gabinete.service.local.ProcessoParteService;
import br.jus.tjro.gabinete.service.local.ProcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("partes")
public class ProcessoParteController {

    @Autowired
    private ProcessoParteService processoParteService;

    @Autowired
    private ProcessoService processoService;

    @Autowired
    private PessoaDocumentoService pessoaDocumentoService;

    @GetMapping("/{polo}/processo/{idProcesso}")
    public ResponseEntity<?> buscaParteProcessoPorIdProcessoETipoPolo(@PathVariable("polo") TipoPolo polo, @PathVariable("idProcesso") long idProcesso) throws Exception {
        Processo processo = processoService.findOne(idProcesso);
        List<Partes> partes = partes = processoParteService.buscaParteProcessoPorIdProcessoEPolo(polo, processo.getId());
        return partes != null && !partes.isEmpty() ? ok(partes) : noContent().build();
    }

    @GetMapping("/parte/{idParte}")
    public ResponseEntity<Pessoa> buscaParteComDocumentos(@PathVariable("idParte") Long idParte) {
        ProcessoParte processoParte = processoParteService.findOne(idParte);
        Pessoa pessoa = processoParte.getPessoa();
        List<PessoaDocumento> documentos = pessoaDocumentoService.buscaDocumentosDaPessoa(pessoa);
        pessoa.setPessoaDocumentos(documentos);
        return ok(pessoa);
    }

    @GetMapping("/endereco/{idParte}")
    public ResponseEntity<Page<ProcessoParteEndereco>> buscaEnderecoProcesso(Pageable pageable, @PathVariable("idParte") Long idParte) {
        Page<ProcessoParteEndereco> processoParteEnderecos = processoParteService.buscaEnderecosParte(idParte, pageable);
        return processoParteEnderecos.getSize() > 0 ? ok(processoParteEnderecos)
            : noContent().build();
    }
}
