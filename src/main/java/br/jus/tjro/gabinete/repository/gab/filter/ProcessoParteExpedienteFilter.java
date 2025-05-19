package br.jus.tjro.gabinete.repository.gab.filter;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class ProcessoParteExpedienteFilter {

    private Long idLegado;

    private Boolean fechado;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCriacao;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCiencia;

    private String pessoaCiencia;

    private String pessoaParte;

    private String tipoDocumento;

    public Long getIdLegado() {
        return idLegado;
    }

    public void setIdLegado(Long idLegado) {
        this.idLegado = idLegado;
    }

    public Boolean getFechado() {
        return fechado;
    }

    public void setFechado(Boolean fechado) {
        this.fechado = fechado;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataCiencia() {
        return dataCiencia;
    }

    public void setDataCiencia(LocalDateTime dataCiencia) {
        this.dataCiencia = dataCiencia;
    }

    public String getPessoaCiencia() {
        return pessoaCiencia;
    }

    public void setPessoaCiencia(String pessoaCiencia) {
        this.pessoaCiencia = pessoaCiencia;
    }

    public String getPessoaParte() {
        return pessoaParte;
    }

    public void setPessoaParte(String pessoaParte) {
        this.pessoaParte = pessoaParte;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}
