package br.jus.tjro.gabinete.Tarefas.core;

import io.vavr.collection.Stream;

import java.util.stream.Collectors;

import static io.vavr.collection.Stream.of;

public enum TarefaEnum {

    Minutar("Minutar",false),
    Corrigir("Corrigir",false),
    Assinar("Assinar",false),
    Assinando("Assinando", true ),
    ParaIntegracao("ParaIntegracao", true,true),
    ParaIntegracaoSemManifestacao("ParaIntegracaoSemManifestacao", true),
    ParaIntegracaoComErro("ParaIntegracaoComErro", true,false),
    ParaIntegracaoAlvara("ParaIntegracaoAlvara", true,false),
    NaoConcluso("NaoConcluso",false,true),
    ParaIntegracaoPauta("ParaIntegracaoPauta",true,true),

    Revisar("Revisar",false);

    private final boolean atualizaLocalizador;
    private final boolean intermediaria;
    private final boolean enviaMsgDevolveOrigem;
    private final String tarefa;

    TarefaEnum(String tarefa, boolean intermediaria) {
        this(tarefa, intermediaria, true,false);
    }
    TarefaEnum(String tarefa, boolean intermediaria, boolean enviaMsgDevolveOrigem) {
        this(tarefa, intermediaria, true,enviaMsgDevolveOrigem);
    }

    TarefaEnum(String tarefa, boolean intermediaria, boolean atualizaLocalizador, boolean enviaMsgDevolveOrigem) {
        this.tarefa = tarefa;
        this.intermediaria = intermediaria;
        this.atualizaLocalizador = atualizaLocalizador;
        this.enviaMsgDevolveOrigem = enviaMsgDevolveOrigem;
    }

    public String getTarefa() {
        return tarefa;
    }

    public boolean isAtualizaLocalizador() {
        return atualizaLocalizador;
    }

    public boolean isEnviaMsgDevolveOrigem() {
        return enviaMsgDevolveOrigem;
    }

    public static String getValuesAsJson() {
        return of(values())
            .map(TarefaEnum::toJson)
            .collect(Collectors.joining(",", "[", "]"));
    }

    public static Stream<TarefaEnum> getIntermediariasENaoConclusas() {
        return of(values()).filter(v -> v.isIntermediaria() || NaoConcluso.equals(v));
    }

    public static Stream<TarefaEnum> getTarefasVisiveis() {
        return of(values()).filter(v -> !(v.isIntermediaria() || NaoConcluso.equals(v)));
    }

    public static TarefaEnum fromString(String text) {
        return of(values()).find(b -> b.tarefa.equalsIgnoreCase(text)).get();
    }

    public String toJson() {
        return "{\"" + this.name() + "\": \"" + this.tarefa + "\"}";
    }

    public boolean isIntermediaria() {
        return intermediaria;
    }
}
