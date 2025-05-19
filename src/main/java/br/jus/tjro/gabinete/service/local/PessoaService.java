package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.repository.gab.pessoa.PessoaRepository;

import br.jus.tjro.gabinete.service.importacao.ObtemItensParaAdicionarAtualizarOuExcluir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class PessoaService {

    
    private final Logger looger = LoggerFactory.getLogger(PessoaService.class);

    private final PessoaRepository pessoaRepository;

    @Autowired
    public PessoaService(PessoaRepository pessoaRepository){
        this.pessoaRepository = pessoaRepository;
    }

    public Pessoa buscaPorIdLegado(Long idPessoaLegado, FonteDadosEnum sistema) {
        return pessoaRepository.findByIdPessoaLegadoAndSistema(idPessoaLegado,sistema).orElse(null);
    }
}
