package br.jus.tjro.gabinete.repository;

import br.jus.tjro.gabinete.model.gab.enums.TipoPrazoEnum;
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoExpediente;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParteExpediente;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.repository.gab.pessoa.PessoaRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoParteRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoExpedienteRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoParteExpedienteRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(value = { SpringExtension.class })
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureEmbeddedDatabase
public class ProcessoExpedienteRepositoryTest {

    @Autowired
    private ProcessoExpedienteRepository processoExpedienteRepository;

    @Autowired
    private ProcessoParteExpedienteRepository processoParteExpedienteRepository;

    @Autowired
    private ProcessosRepository processosRepository;

    @Autowired
    private ProcessoParteRepository processoParteRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    private ArrayList<Long> idsProcessos = new ArrayList<>();

    @Test
    public void salvaProcessoExpediente() {
        ProcessoExpediente expediente = new ProcessoExpediente();
        expediente.setIdLegado(666l);
        expediente.setProcesso(getProcesso());
        idsProcessos.add(expediente.getProcesso().getId());
        expediente.setDtCriacao(new Date());
        expediente.setInTemporario(false);
        ProcessoExpediente retorno = processoExpedienteRepository.save(expediente);
        assertNotNull(retorno.getId());
        List<ProcessoExpediente> lista = Lists.newArrayList(processoExpedienteRepository.findAll());
        assertTrue(lista.size() > 0);
    }

    @AfterEach
    public void tearDownAfter() throws Exception {
        processoParteExpedienteRepository.deleteAll();
        processoExpedienteRepository.deleteAll();
//        processosRepository.deleteAll();
    }

    @AfterAll
    public void cleanProcessos() throws Exception{
        processosRepository.deleteAllById(idsProcessos);
    }

    @Test
    public void salvaProcessoParteExpediente() {
        ProcessoExpediente expediente = new ProcessoExpediente();
        Processo processo = getProcesso();
        expediente.setIdLegado(666l);
        expediente.setProcesso(processo);
        idsProcessos.add(expediente.getProcesso().getId());

        expediente.setDtCriacao(new Date());
        expediente.setInTemporario(false);
        expediente = processoExpedienteRepository.save(expediente);

        assertNotNull(expediente.getId());

        ProcessoParteExpediente parteExpediente = new ProcessoParteExpediente();
        parteExpediente.setProcessoExpediente(expediente);
        parteExpediente.setIdLegado(666l);
        parteExpediente.setPessoaParte(getPessoa());
        parteExpediente.setPendenteManifestacao(false);
        parteExpediente.setTipoPrazo(TipoPrazoEnum.D);
        parteExpediente.setFechado(false);
        parteExpediente.setIntimacaoPessoal(false);

        ProcessoParteExpediente retorno = processoParteExpedienteRepository.save(parteExpediente);
        
        assertNotNull(retorno.getId());
        List<ProcessoParteExpediente> lista = Lists.newArrayList(processoParteExpedienteRepository.findAll());
        assertTrue(lista.size() > 0);
    }

    @Test
    public void testaRelacionamentoEntreExpedienteEhParteExpediente() {

        ProcessoExpediente expediente = new ProcessoExpediente();
        expediente.setProcesso(getProcesso());
        idsProcessos.add(expediente.getProcesso().getId());

        expediente.setIdLegado(666l);
        expediente.setDtCriacao(new Date());
        expediente.setInTemporario(false);
        expediente = processoExpedienteRepository.save(expediente);

        assertNotNull(expediente.getId());

        ProcessoParteExpediente parteExpediente = new ProcessoParteExpediente();
        parteExpediente.setProcessoExpediente(expediente);
        parteExpediente.setIdLegado(666l);
        parteExpediente.setPessoaParte(getPessoa());
        parteExpediente.setPendenteManifestacao(false);
        parteExpediente.setTipoPrazo(TipoPrazoEnum.D);
        parteExpediente.setFechado(false);
        parteExpediente.setIntimacaoPessoal(false);

        ProcessoParteExpediente parteExpediente1 = new ProcessoParteExpediente();
        parteExpediente1.setProcessoExpediente(expediente);
        parteExpediente1.setIdLegado(666l);
        parteExpediente1.setPessoaParte(getPessoa());
        parteExpediente1.setPendenteManifestacao(false);
        parteExpediente1.setTipoPrazo(TipoPrazoEnum.D);
        parteExpediente1.setFechado(false);
        parteExpediente1.setIntimacaoPessoal(false);

        ProcessoParteExpediente r1 = processoParteExpedienteRepository.save(parteExpediente);
        ProcessoParteExpediente r2 = processoParteExpedienteRepository.save(parteExpediente1);

        assertSame(r1.getProcessoExpediente().getId(), expediente.getId());
        assertSame(r2.getProcessoExpediente().getId(), expediente.getId());

        List<ProcessoParteExpediente> lista = new ArrayList<>();
        lista.add(r1);
        lista.add(r2);
        expediente.setProcessoParteExpedientes(lista);
        assertEquals(expediente.getProcessoParteExpedientes().size(), 2);
    }

    private Pessoa getPessoa() {
        Pessoa pessoa = new Pessoa();
        return pessoaRepository.save(pessoa);
    }

    private Processo getProcesso(){
        Processo processo = new Processo();
        processo.setNumeroProcesso("numero");
        processo.setOrgaoJulgadorObj(new OrgaoJulgador("PJEPG-91"));
        return processosRepository.save(processo);
    }
}
