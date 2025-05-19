package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.builders.service.BuilderProcessoService;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.transiente.ProcessoConcluso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
public class ProcessoServiceTest {

    private ProcessoService processoService;

    @BeforeEach
    public void setUp() {
        BuilderProcessoService builder = new BuilderProcessoService();
        processoService = builder.get();
    }

    @Test
    public void montaProcessoInformandoTarefaMinutar() throws Exception {
        Processo processo = processoService.montaProcesso(getProcessoConcluso(),FonteDadosEnum.PJEPG,getProcesso(),TarefaEnum.Minutar,null);
        assertEquals(TarefaEnum.Minutar,processo.getTarefa());
    }

    @Test
    public void montaProcessoInformandoTarefaAssinar() throws Exception {
        Processo processo = processoService.montaProcesso(getProcessoConcluso(),FonteDadosEnum.PJEPG,getProcesso(),TarefaEnum.Assinar,null);
        assertEquals(TarefaEnum.Assinar,processo.getTarefa());
    }


    @Test
    public void montaProcessoInformandoTarefaPoremJaExisteEstadoDeveManter() throws Exception {
        Processo processo = processoService.montaProcesso(getProcessoConcluso(),FonteDadosEnum.PJEPG,getProcessoComEstado(),TarefaEnum.Assinar,null);
        assertEquals(TarefaEnum.ParaIntegracao,processo.getTarefa());
    }

    @Test
    public void montaProcessoTestaOrgaoJulgador() throws Exception {
        Processo processo = processoService.montaProcesso(getProcessoConcluso(),FonteDadosEnum.PJEPG,getProcesso(),TarefaEnum.Minutar,null);
        assertEquals(TarefaEnum.Minutar,processo.getTarefa());
        assertEquals(FonteDadosEnum.PJEPG+"-333", processo.getOrgaoJulgador());
    }

    private Processo getProcesso() {
        return new Processo();
    }


    private Processo getProcessoComEstado() throws Exception {
        Processo p = new Processo();
        p.setTarefa(TarefaEnum.ParaIntegracao);
        return p;
    }

    private ProcessoConcluso getProcessoConcluso() {
        return new ProcessoConcluso(1l,"666",
            "6",
            true,true,true,
            "333","333666",new Date(),666.333, "",
            false,0,null,null);
    }
}
