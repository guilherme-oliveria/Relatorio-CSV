package br.jus.tjro.gabinete.builders;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;

import java.time.LocalDateTime;

public class ProcessoBuilder {

    private Processo processo;

    private ProcessoBuilder() {
    }

    public static ProcessoBuilder umProcesso() throws Exception {
        ProcessoBuilder builder = new ProcessoBuilder();
        builder.processo = new Processo();
        builder.processo.setDataEntrada(LocalDateTime.now());
        builder.processo.setIdProcessoSistemaLegado(999l);
        builder.processo.setJusticaGratuita(false);
        builder.processo.setNumeroProcesso("7000422-19.2014.8.22.0601");
        builder.processo.setPossuiLiminar(false);
        builder.processo.setPrioridade(false);
        builder.processo.removeSegredoJustica();
        builder.processo.setSistema(FonteDadosEnum.PJEPG);
        builder.processo.setAssuntoPrincipal("");
        builder.processo.setTarefa(TarefaEnum.Minutar);
        return builder;
    }

    public ProcessoBuilder numeroProcesso(String numero) {
        processo.setNumeroProcesso(numero);
        return this;
    }

    public ProcessoBuilder caixa(Caixa caixa) {
        processo.setCaixa(caixa);
        return this;
    }

    public ProcessoBuilder orgaoJulgador(String orgaoJulgador) {
        processo.setOrgaoJulgadorObj(new OrgaoJulgador(orgaoJulgador));
        return this;
    }

    public ProcessoBuilder dataEntrada(LocalDateTime data) {
        processo.setDataEntrada(data);
        return this;
    }

    public ProcessoBuilder idLegado(Long idLegado) {
        processo.setIdProcessoSistemaLegado(idLegado);
        return this;
    }

    public ProcessoBuilder tarefa(TarefaEnum tarefa) throws Exception {
        processo.setTarefa(tarefa);
        return this;
    }

    public Processo agora() {
        return processo;
    }

}
