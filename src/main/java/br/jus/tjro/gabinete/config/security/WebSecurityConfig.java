package br.jus.tjro.gabinete.config.security;

import br.jus.tjro.gabinete.config.cors.CorsCustomizerConfigurer;
import br.jus.tjro.gabinete.config.security.manager.*;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.repository.webjud.OrgaosJulgadoresRepository;
import br.jus.tjro.gabinete.service.remoto.VisibilidadeRemotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final Boolean mockUser;
    private final JwtDecoder decode;
    private final String modoAutenticacao;
    private final ProcessosRepository processosRepository;

    private final KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter;

    @Autowired
    private CorsCustomizerConfigurer corsCustomizerConfigurer;

    @Autowired
    WebSecurityConfig(@Value("${sol.key:secret}") String secret,
                      @Value("${modo.autenticacao:sol}") String modoAutenticacao,
                      @Value("${mock.user:false}") Boolean mockUser,
                      @Value("${modo.autenticacao:jwt}") String modoAuth,
                      ProcessosRepository processosRepository,
                      OrgaosJulgadoresRepository orgaosJulgadoresRepository, VisibilidadeRemotoService visibilidadeRemotoService, KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter,
                      @Value("${keycloak.url-jwt:''}") String jwtIssEmissor, PublicKeyConfig publicKeyConfig){
        this.mockUser = mockUser;
        this.modoAutenticacao = modoAutenticacao;
        this.keycloakJwtAuthenticationConverter = keycloakJwtAuthenticationConverter;
        this.decode = new JwtDecoder(secret, modoAuth,orgaosJulgadoresRepository,visibilidadeRemotoService,jwtIssEmissor,publicKeyConfig.publicKey());
        this.processosRepository = processosRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        var httpContinue = http
            .authorizeHttpRequests()
            .requestMatchers(HttpMethod.GET,
                "/login","/usuario/login-keycloak/*", "/date/isSync/*", "/swagger-resources*",
                "/pg/img/*", "/pje/img/*", "/v2/api-docs*", "/v2/api-docs*",
                "/version*", "/telegram/", "/kafka*", "/kafka/envia/*",
                "/verifica-integracao/*", "/documentos/porocesso/*/id-legado/*",
                "/processos/id-legado/*/fonte-dados/*", "/devolve-origem*",
                "/devolve-origem/*", "/devolve-origem/reenvia/*",
                "/orgao-julgador/usuario-limpa-cache/*", "/busca-concluso/*",
                "/actuator/*", "/pedido-inclusao-pauta*",
                "/swagger-resources/configuration/*"
                ).permitAll()
            .requestMatchers(HttpMethod.POST, "/devolve-origem*","/").permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .anonymous().disable();
         return configureAutentication(httpContinue)
             .cors(corsCustomizerConfigurer).csrf().disable().build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder()
            .username("tjmg")
            .password("tjmg")
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    private HttpSecurity configureAutentication(HttpSecurity http) throws Exception {
        if(modoAutenticacao.equals("dev")){
            return http.oauth2ResourceServer()
                .jwt()
                .authenticationManager(new DevAuthenticateManager(processosRepository)).and().and();
        }else if(modoAutenticacao.equals("keycloak")){
            return http.oauth2ResourceServer().bearerTokenResolver(new OfficeBearerTokenResolver())
                .jwt().jwtAuthenticationConverter(keycloakJwtAuthenticationConverter).authenticationManager(new SolAuthenticateManager(decode)).and().and();
        }
        else {
            return http.oauth2ResourceServer()
                .bearerTokenResolver(new OfficeBearerTokenResolver())
                .jwt()
                .authenticationManager(new SolAuthenticateManager(decode)).and().and();
        }
    }
}
