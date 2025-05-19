package br.jus.tjro.gabinete.controller.handler;

import br.jus.tjro.gabinete.config.AdminsEnvironment;
import br.jus.tjro.gabinete.repository.remoto.ModeloDocumentoRepository;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.security.CustomMethodSecurityExpressionRoot;
import br.jus.tjro.gabinete.service.remoto.VisibilidadeRemotoService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

import java.util.List;

public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private final AuthenticationUsuarioService authService;
    private final VisibilidadeRemotoService visiblidadeService;
    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
    private final ModeloDocumentoRepository modeloDocRepository;
    private final boolean segredoDisable;
    private final AdminsEnvironment adminsEnvironment;
    private final List<String> competenciaSegredoForce;
    private final List<String> orgaojulgadorSegredoForce;
    private final List<String> classeSegredoForce;


    public CustomMethodSecurityExpressionHandler(AuthenticationUsuarioService authService,
                                                 VisibilidadeRemotoService visibilidadeRemotoService,
                                                 boolean segredoDisable, AdminsEnvironment adminsEnvironment,
                                                 ModeloDocumentoRepository modeloDocRepository,
                                                 List<String> orgaojulgadorSegredoForce,
                                                 List<String> competenciaSegredoForce,
                                                 List<String> classeSegredoForce){
        this.authService = authService;
        this.visiblidadeService = visibilidadeRemotoService;
        this.segredoDisable = segredoDisable;
        this.adminsEnvironment = adminsEnvironment;
        this.modeloDocRepository = modeloDocRepository;
        this.orgaojulgadorSegredoForce = orgaojulgadorSegredoForce;
        this.competenciaSegredoForce = competenciaSegredoForce;
        this.classeSegredoForce = classeSegredoForce;
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
        Authentication authentication, MethodInvocation invocation) {
        CustomMethodSecurityExpressionRoot root =
            new CustomMethodSecurityExpressionRoot(authentication, visiblidadeService,
                segredoDisable, authService, modeloDocRepository, adminsEnvironment,
                orgaojulgadorSegredoForce,
                competenciaSegredoForce,
                classeSegredoForce);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        return root;
    }
}
