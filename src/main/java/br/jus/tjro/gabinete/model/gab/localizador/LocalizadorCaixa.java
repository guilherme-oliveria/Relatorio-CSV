package br.jus.tjro.gabinete.model.gab.localizador;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.model.gab.enums.TipoDeLocalizadorEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity
@Table(name = "LOC_CAIXA")
@SequenceGenerator(name = LocalizadorCaixa.SEQUENCE_NAME, sequenceName = LocalizadorCaixa.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class LocalizadorCaixa {

    public static final String SEQUENCE_NAME = "SEQUENCIA_LOCALIZADOR_CAIXA";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOME", length = 100)
    private String nome;

    @Transient
    private Long idCaixa;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_LOC_CAIXA_LOC_REGRA"))
    private LocalizadorRegra regra;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
        name = "LOC_CAIXA_LOC_ORGAO_JULGADOR",
        joinColumns =
        @JoinColumn(name = "LOCALIZADORCAIXA_ID")
    )
    private Set<LocalizadorOrgaoJulgador> orgaosJulgadores;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
        name = "LOC_CAIXA_LOC_USUARIO",
        joinColumns =
        @JoinColumn(name = "LOCALIZADOR_CAIXA_ID", foreignKey = @ForeignKey(name = "FK_LOC_CAIXA_LOC_USU_LOC_CAIXA"))
//        inverseJoinColumns = @JoinColumn(name = "USUARIOS_ID", foreignKey = @ForeignKey(name = "FK_LOC_CAIXA_LOC_USUARIO"))
    )
    private Set<LocalizadorUsuario> usuarios;

    @Column(name = "EXCLUSIVO")
    private Boolean exclusivo;

    @Transient
    @JsonInclude(Include.NON_EMPTY)
    private Long tamanho;

    @Column(name = "TIPO")
    @Enumerated(EnumType.STRING)
    private TipoDeLocalizadorEnum tipo;

    @Transient
    public List<Regra> regras = new ArrayList<>();

    public LocalizadorCaixa() {
    }

    public LocalizadorCaixa(Long id) {
        this.id = id;
    }

    public LocalizadorCaixa(Long id, String nome, Long tamanho, TipoDeLocalizadorEnum tipo) {
        this.id = id;
        this.nome = nome;
        this.tamanho = tamanho;
        this.tipo = tipo;

    }

    public TipoDeLocalizadorEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoDeLocalizadorEnum tipo) {
        this.tipo = tipo;
    }

    public Set<LocalizadorOrgaoJulgador> getOrgaosJulgadores() {
        return orgaosJulgadores;
    }

    public void setOrgaosJulgadores(Set<LocalizadorOrgaoJulgador> orgaosJulgadores) {
        this.orgaosJulgadores = orgaosJulgadores;
    }

    public Set<LocalizadorUsuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<LocalizadorUsuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Boolean isExclusivo() {
        return exclusivo;
    }

    public void setExclusivo(Boolean exclusivo) {
        this.exclusivo = exclusivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getTamanho() {
        return tamanho;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
    }

    public LocalizadorRegra getRegra() {
        return regra;
    }

    public void setRegra(LocalizadorRegra regra) {
        this.regra = regra;
    }

    public Long getIdCaixa() {
        return idCaixa;
    }

    public void setIdCaixa(Long idCaixa) {
        this.idCaixa = idCaixa;
    }

    public void criaRegras() {
        if(this.regra != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            try {
                regras = mapper.readValue(this.regra.getParametro(), new TypeReference<List<Regra>>() {});
                if (regras.stream().noneMatch(r -> "tarefaEnum".equals(r.getParametro()) && r.getOperador().equals("=")))
                    TarefaEnum.getIntermediariasENaoConclusas().forEach(t -> regras.add(new Regra("tarefaEnum", "!=", t.toString())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Transient
    public Long getManifestacao(){
        if(regras.size() == 0)
            criaRegras();
        var regrasManifestacao = regras.stream().filter(it -> it.getParametro().equals("caixa.id") && it.getParametro().equals("=")).collect(toList());
        if(regrasManifestacao.size() >= 1 && regrasManifestacao.get(0).getValores().size() == 1)
            return Long.valueOf(regrasManifestacao.get(0).getValores().stream().findFirst().get().toString());
        return -1l;
    }


    @Transient
    public boolean isManifestacao(long idCaixa) {
        if(regras.size() == 0)
            criaRegras();
        var regrasManifestacao = regras.stream()
            .filter(it -> it.getParametro().equals("caixa.id") && it.getOperador().equals("="))
            .collect(toList());
        if(regrasManifestacao.size() == 1 && regrasManifestacao.get(0).getValores().size() == 1)
            return Long.valueOf(regrasManifestacao.get(0).getValores().get(0).toString()).equals(idCaixa);
        return regrasManifestacao.size() != 1 && idCaixa == -1l;
    }
}
