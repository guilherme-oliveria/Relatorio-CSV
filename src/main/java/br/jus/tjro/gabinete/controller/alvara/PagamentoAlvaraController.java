package br.jus.tjro.gabinete.controller.alvara;

import br.jus.tjro.gabinete.model.gab.alvara.Alvara;
import br.jus.tjro.gabinete.model.gab.alvara.PagamentoAlvara;
import br.jus.tjro.gabinete.repository.gab.alvara.AlvaraRepository;
import br.jus.tjro.gabinete.repository.gab.alvara.PagamentoAlvaraRepository;
import br.jus.tjro.gabinete.service.alvara.AlvaraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import java.util.Optional;

@RestController
@RequestMapping("pagamento")
public class PagamentoAlvaraController {

    @Autowired
    private PagamentoAlvaraRepository pagamentoAlvaraRepository;

    @Autowired
    private AlvaraService alvaraService;

    @Autowired
    private AlvaraRepository alvaraRepository;

    @Transactional
    @DeleteMapping("/{idPagamentoAlvara}")
    public void excluir(@PathVariable("idPagamentoAlvara") Long idPagamentoAlvara, Authentication user) throws Exception {
        PagamentoAlvara pagamentoAlvara = pagamentoAlvaraRepository.findById(idPagamentoAlvara).orElseThrow(()-> new IllegalArgumentException("Erro ao recuperar ordem de pagamento alvara."));
        pagamentoAlvara.deletar();
        pagamentoAlvaraRepository.save(pagamentoAlvara);
        alvaraService.toggleTagAlvara(pagamentoAlvara.getAlvara());
    }
}
