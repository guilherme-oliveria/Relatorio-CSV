package br.jus.tjro.gabinete.repository;


import br.jus.tjro.gabinete.model.gab.enums.TipoPolo;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.transiente.Partes;
import br.jus.tjro.gabinete.repository.gab.pessoa.PessoaRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoParteRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(value = { SpringExtension.class })
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
public class PessoaRepositoryTeste {



    @Autowired
    public PessoaRepository pessoaRepository;

    //@Test
    public void test(){
        var pessoas = pessoaRepository.findAll();
    }
}
