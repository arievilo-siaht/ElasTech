package br.com.elastech.ms_auth.controller;

import br.com.elastech.ms_auth.dto.request.LoginRequest;
import br.com.elastech.ms_auth.dto.response.TokenResponse;
import br.com.elastech.ms_auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    private ResponseEntity<TokenResponse> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        return ResponseEntity
                .ok(authService.login(loginRequest));
    }
}