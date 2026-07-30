package br.com.elastech.ms_auth.client;

import br.com.elastech.ms_usuarios.dto.internal.UsuarioAuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class UsuarioAuthClient {

    private final RestClient restClient;

    @Value("${app.auth.usuarios-base-url}")
    private String usuariosBaseUrl;

    public UsuarioAuthClient(@Value("${app.auth.usuarios-base-url}") String usuariosBaseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(usuariosBaseUrl)
                .build();
    }

    public UsuarioAuthResponse findByUsername(String username) {

        try {
            return restClient.get()
                    .uri("/internal/auth/usuarios/{username}", username)
                    .retrieve()
                    .body(UsuarioAuthResponse.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
