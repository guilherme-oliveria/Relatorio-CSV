package br.jus.tjro.gabinete.model.gab.enums;


@Deprecated
public enum TipoLog {

    Sched("Schedule"),
    Erro("Erro"),
    TpAne("Importação de Tipos de Anexos"),
    Proc("Importação de Processos"),
    Parte("Importação das Partes"),
    Assun("Importação dos Assuntos"),
    Docum("Importação de Documentos de Processo"),
    DocPe("Importação de Documentos de Pessoa"),
    Movim("Importação de Movimentos"),
    Ender("Importação de Endereços"),;

    public String descricao;

    TipoLog(String desc) {
        this.descricao = desc;
    }
}
