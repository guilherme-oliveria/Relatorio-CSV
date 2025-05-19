package br.jus.tjro.gabinete.security;

import br.jus.tjro.gabinete.config.AdminsEnvironment;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.ModeloMinuta;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoTag;
import br.jus.tjro.gabinete.repository.remoto.ModeloDocumentoRepository;
import br.jus.tjro.gabinete.service.remoto.VisibilidadeRemotoService;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot
    implements MethodSecurityExpressionOperations {

    private final Usuario usuario;
    private final VisibilidadeRemotoService visibilidadeRemotoService;
    private final boolean segredoDisable;
    private final ModeloDocumentoRepository minutaService;
    private final AdminsEnvironment adminsEnvironment;
    private final List<String> competenciaSegredoForce;
    private final List<String> orgaojulgadorSegredoForce;
    private final List<String> classeSegredoForce;
    private Object filterObject;
    private Object returnObject;

    public CustomMethodSecurityExpressionRoot(Authentication authentication,
                                              VisibilidadeRemotoService visibilidadeRemotoService,
                                              boolean segredoDisable,
                                              AuthenticationUsuarioService authService,
                                              ModeloDocumentoRepository minutaService,
                                              AdminsEnvironment adminsEnvironment,
                                              List<String> orgaojulgadorSegredoForce,
                                              List<String> competenciaSegredoForce,
                                              List<String> classeSegredoForce) {
        super(authentication);
        this.minutaService = minutaService;
        this.adminsEnvironment = adminsEnvironment;
        this.usuario = authService.getUsuario(authentication);
        this.visibilidadeRemotoService = visibilidadeRemotoService;
        this.segredoDisable = segredoDisable;
        this.competenciaSegredoForce = competenciaSegredoForce;
        this.orgaojulgadorSegredoForce = orgaojulgadorSegredoForce;
        this.classeSegredoForce = classeSegredoForce;
    }

    public CustomMethodSecurityExpressionRoot(Authentication authentication,
                                              VisibilidadeRemotoService visibilidadeRemotoService,
                                              AuthenticationUsuarioService authService,
                                              ModeloDocumentoRepository minutaService,
                                              AdminsEnvironment adminsEnvironment) {
        this(authentication, visibilidadeRemotoService, false, authService, minutaService, adminsEnvironment,
            new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;

    }

    @Override
    public Object getThis() {
        return this;
    }

    private Usuario getUsuario() {
        return usuario;
    }

    private boolean isUsuarioTemOrgaoJulgador(String orgaoJulgador) {
        return isUsuarioTemOrgaoJulgador(getUsuario(), orgaoJulgador);
    }

    public static boolean isUsuarioTemOrgaoJulgador(Usuario usuario, String orgaoJulgador) {
        Stream<String> result = usuario.getOrgaosJulgadores().stream().filter(p -> p.equals(orgaoJulgador));
        return result.count() > 0;
    }

    public boolean validaMinutaRecebidaAcessada(Object returnObject){
        return true;
    }

    public boolean validaTagsProcessoAcessado(Object returnObject) {
        try{
            List<ProcessoTag> tagsProcesso = (List<ProcessoTag>) returnObject;
            if (tagsProcesso.size() > 0) {
                return validaProcessoAcessado(tagsProcesso.iterator().next().getProcesso());
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean validaProcessosAcessados(Object returnObject) {
        List<Processo> processos = (List<Processo>) returnObject;
        var possuiProcessosNaoAutorizados = processos.stream().filter(processo -> !validaProcessoAcessado(processo)).collect(Collectors.toList()).size() > 0;
        return possuiProcessosNaoAutorizados;
    }

    public boolean podeGerenciarTags() {
        return adminsEnvironment.isAdmin(usuario.getCpf());
    }

    public boolean validaProcessoAcessado(Object returnObject) {
        if(returnObject instanceof Optional){
            Optional<Processo> optinal = (Optional<Processo>) returnObject;
            if(optinal.isEmpty())
                return true;
            returnObject = optinal.get();
        }
        if (verificaScheduled() || returnObject == null)
            return true;
        Processo processo = (Processo) returnObject;
        return isUsuarioTemOrgaoJulgador(processo.getOrgaoJulgadorObj().getId()) && verificaSeAcessaProcessoSigiloso(processo);
    }

    public boolean validaMinutaAcessado(Object returnObject) throws Exception {
        if (verificaScheduled())
            return true;
        Minuta documento = (Minuta) returnObject;
        if (documento.getProcesso() == null)
            throw new Exception("Erro ao validar minuta, processo esta nulo");
        return validaProcessoAcessado(documento.getProcesso());

    }

    public boolean verificaSeAcessaProcessoSigiloso(Processo processo) {
        if(nivelSigiloAtivo(processo))
            return (!processo.isSegredoJustica() || processo.verificaSigilo(usuario) || visibilidadeRemotoService.temVisibilidade(processo,usuario));
        else
            return true;

    }

    private boolean nivelSigiloAtivo(Processo processo){
        return !segredoDisable ||
            competenciaSegredoForce.contains(processo.getCompetencia()) ||
            orgaojulgadorSegredoForce.contains(processo.getOrgaoJulgador()) ||
            classeSegredoForce.contains(processo.getTpuClasse().getCodigo().toString());
    }

    public boolean canEditOrDeleteModeloDocumento(Object objeto){
        if(returnObject instanceof ModeloMinuta){
            return ((ModeloMinuta) objeto).isCanEditOrDelete(usuario);
        }else {
            return true;
        }
    }

    public boolean canEditOrDeleteModeloDocumento(String id) {
        try {
            Optional<ModeloMinuta> minuta = minutaService.findById(id, usuario);
            return minuta.map(m -> m.isCanEditOrDelete(usuario)).orElse(false);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean verificaScheduled() {
        return this.hasAnyAuthority("SISTEMA");
    }

}
