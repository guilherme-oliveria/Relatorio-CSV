package br.jus.tjro.gabinete.config.security.manager;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.Papel;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.repository.webjud.OrgaosJulgadoresRepository;
import br.jus.tjro.gabinete.service.remoto.VisibilidadeRemotoService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.validation.ConstraintViolationException;

import java.security.spec.EncodedKeySpec;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import java.security.Signature;
import java.security.PublicKey;
import java.util.Base64;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.security.interfaces.RSAKey;
import java.security.KeyFactory;
import java.util.stream.Collectors;

/**
 * Classe base para o gerenciamento dos JWTs, geração, validação.
 *
 * @author Pablo Filetti Moreira
 * @version %I%, %G%
 * @since 1.0
 */

public class JwtDecoder {


    private final Logger log = LoggerFactory.getLogger(JwtDecoder.class);
    private static final String JWT_USUARIO_ID = "id";
    private static final String JWT_USUARIO_PERFIS = "pem";
    private static final String JWT_USUARIO_CPF = "cpf";
    private static final String JWT_USUARIO_ORGAOS_JULGADORES = "lot";
    private final String JWT_KEY;
    private static String  PUBLIC_KEY ="";

    private final String MODOAUTH;
    private final OrgaosJulgadoresRepository orgaoJulgadorService;
    private final VisibilidadeRemotoService visibilidadeRemotoService;
    private final String JWT_EMISSOR_ISS_VERIFY;

    public JwtDecoder(String jwt_key, String modoAuth,
                      OrgaosJulgadoresRepository orgaoJulgadorService,
                      VisibilidadeRemotoService visibilidadeRemotoService,
                      String jwtEmissor,
                      String publicKey) {
        JWT_KEY = jwt_key;
        MODOAUTH = modoAuth;
        JWT_EMISSOR_ISS_VERIFY= jwtEmissor;
        this.orgaoJulgadorService = orgaoJulgadorService;
        this.visibilidadeRemotoService = visibilidadeRemotoService;
        this.PUBLIC_KEY=publicKey;
    }

    /**
     * @param token jwt já gerado anteriormente
     * @return obj usuário
     */
    public Usuario converteTokenEhValida(String token) {
        DecodedJWT jwt = JWT.decode(token);
        Usuario usuario;
        String nome,id,perfis;

        if(MODOAUTH.equals("keycloak")) {
            nome = jwt.getClaim("name").asString();
            id = jwt.getClaim("preferred_username").asString();
            perfis = jwt.getClaim("aud").asString();

            try {
                validaTokenRSA(token);
            } catch (Exception e) {
                log.error(e.getMessage());
                return null;
                //throw new IllegalArgumentException("Erro ao Validar Token", e);
            }

        } else {
            nome = jwt.getSubject();
            id = jwt.getClaim(JWT_USUARIO_CPF).asString();
            perfis = jwt.getClaim(JWT_USUARIO_PERFIS).asString();

            if (perfis == null)
                throw new RuntimeException("Erro. Não existe perfis no token");

            try {
                validaToken(token);
            } catch (Exception e) {
                log.error(e.getMessage());
                return null;
                //throw new IllegalArgumentException("Erro ao Validar Token", e);
            }
        }


        if (isTokenExpirado(jwt)) {
            log.error("Token expirado");
            return null;
        }

        //throw new ConstraintViolationException("Token expirado", Collections.emptySet());

        List<String> permissoes = fromString(perfis);
        usuario = new Usuario(nome, id, permissoes, token);
        if (usuario.getPermissoes().stream().filter(p -> p.equals("SISTEMA")).count() < 1) {
            usuario.setPapeis(orgaoJulgadorService.findAllPapelByUsuario(usuario));
            Map<String, List<Papel>> papeis = usuario.getPapeis();
            if(papeis.get("PJEPG").stream().filter(p -> p.getPapel().equals("administrador")).collect(Collectors.toList()).size() <= 0) {
                try {
                    Boolean isAdmin = visibilidadeRemotoService.getIsAdmin(usuario);
                    if (isAdmin) {
                        Papel admin = new Papel("administrador", new ArrayList<>());
                        papeis.get("PJEPG").add(admin);
                        usuario.setPapeis(papeis);
                    }
                } catch (Exception e) {
                    log.error("Falha ");
                }
            }
        }
        return usuario;
    }
    /**
     * Verifica se o token JWT é valido
     *
     * @param token já gerado anteriormente
     * @return boolean
     */
    private boolean validaToken(String token) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC512(JWT_KEY);
        JWTVerifier verifier = JWT.require(algorithm).acceptLeeway(180000).build();
        DecodedJWT jwt = verifier.verify(token);
        return true;
    }

    /**
     * Verificar criptografia assimétrica do tipo RSA, o keycloak utiliza
     * @param token já gerado anteriormente
     * @return Caso token não sejá valido o método verify lançará uma exceção indicando o motivo da falha
     * */
    public void validaTokenRSA(String token) {
        try {
            PublicKey publicKey = loadPublicKeyFromPEM(PUBLIC_KEY);
            Algorithm algorithm = Algorithm.RSA256((RSAKey) publicKey);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(JWT_EMISSOR_ISS_VERIFY).build();

            verifier.verify(token);
        } catch (JWTVerificationException exception){
            throw new JWTVerificationException(exception.getMessage(),exception);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static PublicKey loadPublicKeyFromPEM(String publicKeyPEM) throws Exception {
        try (PemReader pemReader = new PemReader(new StringReader(publicKeyPEM))) {
            PemObject pemObject = pemReader.readPemObject();
            byte[] publicKeyBytes = pemObject.getContent();

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            EncodedKeySpec keySpec2 = new X509EncodedKeySpec(publicKeyBytes);
            return  keyFactory.generatePublic(keySpec2);
        }
    }
    /**
     * Verifica se o token JWT ainda esta no prazo de validade
     *
     * @param token já gerado anteriormente
     * @return boolean returna true caso o token esteja expirado
     */
    private boolean isTokenExpirado(DecodedJWT jwt) {
        LocalDateTime dtAtual = LocalDateTime.now();
        Date dtAtualDate = Date.from(dtAtual.atZone(ZoneId.systemDefault()).toInstant());
        return dtAtualDate.after(jwt.getExpiresAt());
    }

    private List<String> fromString(String string) {
        if(string != null) {
            String[] strings = string.replace("[", "").replace("]", "").split(", ");
            if (string == null)
                return null;
            return Arrays.asList(strings);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     *  Verificar criptografia assimétrica do tipo RSA, o keycloak utiliza
     *  * @param token já gerado anteriormente
     *  * @return boolean
     * */
    public static boolean validaTokenKeycloak(String token) {
        try {
            PublicKey publicKey = loadPublicKeyFromPEM(PUBLIC_KEY);

            return verifyToken(token, publicKey);
        } catch (Exception e) {
            return false; // Token inválido ou expirado
        }
    }

    private static boolean verifyToken(String token, PublicKey publicKey) throws Exception {
        String[] tokenParts = token.split("\\.");

        if (tokenParts.length != 3) {
            return false; // O token não está no formato correto
        }

        String header = tokenParts[0];
        String payload = tokenParts[1];
        String signature = tokenParts[2];

        String signedData = header + "." + payload;
        byte[] signatureBytes = Base64.getUrlDecoder().decode(signature);

        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(publicKey);
        verifier.update(signedData.getBytes());

        return verifier.verify(signatureBytes);
    }

}
