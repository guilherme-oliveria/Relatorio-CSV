package br.jus.tjro.gabinete.dto;

import java.util.Date;

public class UsuarioMobileDTO {

    private Integer idUsuarioMobile;

    private Long usuario;

    private String codigoPareamento;

    private Date dataCadastro;

    private String plataforma;

    private Boolean ativo;

    private Boolean pareamentoRealizado;

    private String versaoPlataforma;

    private String nomeDispositivo;

    public Integer getIdUsuarioMobile() {
        return idUsuarioMobile;
    }

    public void setIdUsuarioMobile(Integer idUsuarioMobile) {
        this.idUsuarioMobile = idUsuarioMobile;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public String getCodigoPareamento() {
        return codigoPareamento;
    }

    public void setCodigoPareamento(String codigoPareamento) {
        this.codigoPareamento = codigoPareamento;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getPareamentoRealizado() {
        return pareamentoRealizado;
    }

    public void setPareamentoRealizado(Boolean pareamentoRealizado) {
        this.pareamentoRealizado = pareamentoRealizado;
    }

    public String getVersaoPlataforma() {
        return versaoPlataforma;
    }

    public void setVersaoPlataforma(String versaoPlataforma) {
        this.versaoPlataforma = versaoPlataforma;
    }

    public String getNomeDispositivo() {
        return nomeDispositivo;
    }

    public void setNomeDispositivo(String nomeDispositivo) {
        this.nomeDispositivo = nomeDispositivo;
    }
}
