package br.com.elastech.ms_auth.service;

import br.com.elastech.ms_auth.dto.response.TokenResponse;
import br.com.elastech.ms_auth.security.AuthUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import java.time.Instant;
import java.util.UUID;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public TokenResponse gerarToken(Authentication authentication) {
        //Recuperar o usuário autenticado
        AuthUserDetails usuario = (AuthUserDetails) authentication.getPrincipal();

        //Definir datas
        Instant agora = Instant.now();
        Instant expiracao = agora.plusSeconds(900);

        //Criar o JTI
        String jti = UUID.randomUUID().toString();

        //Converter as roles
        List<String> roles =
                usuario.getAuthorities()
                        .stream()
                        .map(authority -> authority.getAuthority())
                        .toList();

        //Criar as claims
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("ms-auth")
                .subject(usuario.getId().toString())
                .claim("preferred_username", usuario.getUsername())
                .claim("roles", roles)
                .audience(List.of("ms-usuarios", "ms-publicacoes"))
                .issuedAt(agora)
                .expiresAt(expiracao)
                .id(jti)
                .build();

        //Criar o Header
        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256)
                .build();

        //Assinar o token
        String token = jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();

        return new TokenResponse(
                token,
                "Bearer",
                900L
        );
    }
}