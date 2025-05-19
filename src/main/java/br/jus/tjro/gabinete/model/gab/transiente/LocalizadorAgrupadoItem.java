package br.jus.tjro.gabinete.model.gab.transiente;


import br.jus.tjro.gabinete.model.gab.enums.TipoLocalizadorAgrupadorEnum;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorAgrupador;

public class LocalizadorAgrupadoItem {

    private Long id;
    private Long tamanho;
    private String nome;
    private Long idCaixa;
    private TipoLocalizadorAgrupadorEnum tipoLocalizadoresAgrupadorEnum;

    public LocalizadorAgrupadoItem() {
        this.tipoLocalizadoresAgrupadorEnum = TipoLocalizadorAgrupadorEnum.Localizador;
    }

    public LocalizadorAgrupadoItem(Long idCaixa, Long tamanho, String nome, Long id) {
        this.id = id;
        this.tamanho = tamanho;
        this.idCaixa = idCaixa;
        this.nome = nome;
        this.tipoLocalizadoresAgrupadorEnum = TipoLocalizadorAgrupadorEnum.Localizador;

    }

    public LocalizadorAgrupadoItem(LocalizadorAgrupador localizadorAgrupador) {
        id = localizadorAgrupador.getId();
        tamanho = localizadorAgrupador.getTamanho();
        this.idCaixa = localizadorAgrupador.getManifestacao();
        this.nome = localizadorAgrupador.getNome();
        this.tipoLocalizadoresAgrupadorEnum = TipoLocalizadorAgrupadorEnum.Localizador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTamanho() {
        return tamanho;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getIdCaixa() {
        return idCaixa;
    }

    public void setIdCaixa(Long idCaixa) {
        this.idCaixa = idCaixa;
    }


    public TipoLocalizadorAgrupadorEnum getTipoLocalizadoresAgrupadorEnum() {
        return tipoLocalizadoresAgrupadorEnum;
    }

    public void setTipoLocalizadoresAgrupadorEnum(TipoLocalizadorAgrupadorEnum TipoLocalizadoresAgrupadorEnum) {
        this.tipoLocalizadoresAgrupadorEnum = TipoLocalizadoresAgrupadorEnum;
    }
}
