package com.biblioteca.auth;

import java.time.Instant;

import com.biblioteca.usuario.Usuario;
import com.biblioteca.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final long expirationSeconds;

    public AuthService(
            UsuarioRepository repository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtEncoder jwtEncoder,
            @Value("${app.jwt.expiration-seconds}") long expirationSeconds) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.expirationSeconds = expirationSeconds;
    }

    public AuthResponse cadastrar(CadastroRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new DataIntegrityViolationException("E-mail já cadastrado");
        }

        Usuario usuario = new Usuario(
                null,
                request.nome(),
                request.email(),
                passwordEncoder.encode(request.senha()),
                request.perfil());
        Usuario salvo = repository.save(usuario);
        return respostaComToken(salvo);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha()));
        Usuario usuario = repository.findByEmail(authentication.getName()).orElseThrow();
        return respostaComToken(usuario);
    }

    private AuthResponse respostaComToken(Usuario usuario) {
        Instant agora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("biblioteca-api")
                .issuedAt(agora)
                .expiresAt(agora.plusSeconds(expirationSeconds))
                .subject(usuario.getEmail())
                .claim("scope", "ROLE_" + usuario.getPerfil().name())
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new AuthResponse(
                usuario.getId(), 
                usuario.getNome(),
                usuario.getEmail(), 
                usuario.getPerfil(), token);
    }
}
