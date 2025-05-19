package br.jus.tjro.gabinete.model.gab.transiente;

import br.jus.tjro.gabinete.model.gab.minuta.Minuta;

import java.util.Date;


@Deprecated
public class MovimentoProcesso {

    private Long id;
    private String nome_usuario;
    private String cpf_usuario;
    private Date data;
    private String descricao;
    private Boolean in_visibilidade_externa;
    private Boolean in_ativo;
    private int id_orgao_julgador;
    private int id_orgao_julgador_colegiado;

    public MovimentoProcesso() { }

    public MovimentoProcesso(Long id) {
        this.id = id;
    }

    private Minuta documento;

    private int idProcesso;

    private String cdEvento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome_usuario() {
        return nome_usuario;
    }

    public void setNome_usuario(String nome_usuario) {
        this.nome_usuario = nome_usuario;
    }

    public String getCpf_usuario() {
        return cpf_usuario;
    }

    public void setCpf_usuario(String cpf_usuario) {
        this.cpf_usuario = cpf_usuario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getIn_visibilidade_externa() {
        return in_visibilidade_externa;
    }

    public void setIn_visibilidade_externa(Boolean in_visibilidade_externa) {
        this.in_visibilidade_externa = in_visibilidade_externa;
    }

    public Boolean getIn_ativo() {
        return in_ativo;
    }

    public void setIn_ativo(Boolean in_ativo) {
        this.in_ativo = in_ativo;
    }

    public int getId_orgao_julgador() {
        return id_orgao_julgador;
    }

    public void setId_orgao_julgador(int id_orgao_julgador) {
        this.id_orgao_julgador = id_orgao_julgador;
    }

    public int getId_orgao_julgador_colegiado() {
        return id_orgao_julgador_colegiado;
    }

    public void setId_orgao_julgador_colegiado(int id_orgao_julgador_colegiado) {
        this.id_orgao_julgador_colegiado = id_orgao_julgador_colegiado;
    }

    public int getIdProcesso() {
        return idProcesso;
    }

    public void setIdProcesso(int idProcesso) {
        this.idProcesso = idProcesso;
    }

    public Long getCdEvento() {
        if (cdEvento != null) {
            return Long.valueOf(cdEvento);
        }
        return null;
    }

    public void setCdEvento(String cdEvento) {
        this.cdEvento = cdEvento;
    }

    public Minuta getDocumento() {
        return documento;
    }

    public void setDocumento(Minuta documento) {
        this.documento = documento;
    }
}
