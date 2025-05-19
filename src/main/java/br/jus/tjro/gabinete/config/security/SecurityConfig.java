package br.jus.tjro.gabinete.config.security;

import br.jus.tjro.gabinete.config.AdminsEnvironment;
import br.jus.tjro.gabinete.controller.handler.CustomMethodSecurityExpressionHandler;
import br.jus.tjro.gabinete.repository.remoto.ModeloDocumentoRepository;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.remoto.VisibilidadeRemotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import java.util.List;

@Deprecated
public class SecurityConfig extends GlobalMethodSecurityConfiguration {

    private final AuthenticationUsuarioService authService;
    private final VisibilidadeRemotoService visibilidadeRemotoService;
    private final boolean segredoDisable;
    private final ModeloDocumentoRepository modeloDocsRepository;
    private final AdminsEnvironment adminsEnvironment;
    private final List<String> orgaojulgadorSegredoForce;
    private final List<String> competenciaSegredoForce;
    private final List<String> classeSegredoForce;


    @Autowired
    public SecurityConfig(AuthenticationUsuarioService authService,
                          VisibilidadeRemotoService visibilidadeRemotoService,
                          @Value("${segredo.disable:false}") boolean segredoDisable,
                          ModeloDocumentoRepository modeloDocsRepository,
                          AdminsEnvironment adminsEnvironment,
                          @Value("${orgao.segredo.force:}") List<String> orgaojulgadorSegredoForce,
                          @Value("${competencia.segredo.force:}") List<String> competenciaSegredoForce,
                          @Value("${classe.segredo.force:") List<String> classeSegredoForce){
        this.authService = authService;
        this.visibilidadeRemotoService = visibilidadeRemotoService;
        this.segredoDisable = segredoDisable;
        this.modeloDocsRepository = modeloDocsRepository;
        this.adminsEnvironment = adminsEnvironment;
        this.orgaojulgadorSegredoForce = orgaojulgadorSegredoForce;
        this.competenciaSegredoForce = competenciaSegredoForce;
        this.classeSegredoForce = classeSegredoForce;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new CustomMethodSecurityExpressionHandler(authService, visibilidadeRemotoService, segredoDisable,
            adminsEnvironment, modeloDocsRepository,orgaojulgadorSegredoForce,competenciaSegredoForce,classeSegredoForce);
    }
}
