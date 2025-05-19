package br.jus.tjro.gabinete.model.gab.localizador;

import br.jus.tjro.gabinete.model.gab.enums.TipoLocalizadorAgrupadorEnum;
import br.jus.tjro.gabinete.model.gab.transiente.LocalizadorAgrupadoItem;

import java.util.List;

public class LocalizadorAgrupador {

    private Long id;

    private String nome;

    private TipoLocalizadorAgrupadorEnum tipoLocalizadoresAgrupadorEnum;

    private Long tamanho;

    private Long manifestacao;

    public LocalizadorAgrupador() {
    }

    public LocalizadorAgrupador(TipoLocalizadorAgrupadorEnum tipoLocalizadoresAgrupadorEnum, List<LocalizadorAgrupadoItem> localizadoresAgrupadoItem,
                                Long tamanho) {
        this.tipoLocalizadoresAgrupadorEnum = tipoLocalizadoresAgrupadorEnum;
        this.tamanho = tamanho;
    }

    public LocalizadorAgrupador(Caixa caixa){
        this.nome =caixa.getNome();
        this.tamanho = caixa.getTamanho();
        this.id = caixa.getId().longValue();
        this.tipoLocalizadoresAgrupadorEnum = TipoLocalizadorAgrupadorEnum.Manifestacao;
    }


    public LocalizadorAgrupador(LocalizadorCaixa p) {
        this.id = p.getId();
        this.nome = p.getNome();
        this.tipoLocalizadoresAgrupadorEnum = TipoLocalizadorAgrupadorEnum.Localizador;
        this.tamanho = p.getTamanho();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoLocalizadorAgrupadorEnum getTipoLocalizadoresAgrupadorEnum() {
        return tipoLocalizadoresAgrupadorEnum;
    }

    public void setTipoLocalizadoresAgrupadorEnum(TipoLocalizadorAgrupadorEnum TipoLocalizadoresAgrupadorEnum) {
        this.tipoLocalizadoresAgrupadorEnum = TipoLocalizadoresAgrupadorEnum;
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

    public Long getManifestacao() {
        return manifestacao;
    }

    public void setManifestacao(Long manifestacao) {
        this.manifestacao = manifestacao;
    }
}
