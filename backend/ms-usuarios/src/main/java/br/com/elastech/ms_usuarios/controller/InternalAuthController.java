package br.com.elastech.ms_usuarios.controller;

import br.com.elastech.ms_usuarios.dto.internal.UsuarioAuthResponse;
import br.com.elastech.ms_usuarios.entities.Usuario;
import br.com.elastech.ms_usuarios.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/auth")
public class InternalAuthController {
    private final UsuarioService usuarioService;

    public InternalAuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuarios/{username}")
    public ResponseEntity<UsuarioAuthResponse> buscarUsuarioParaAutenticacao(
        @PathVariable String username
    ) {
        Usuario usuario = usuarioService.findByUsername(username);

        UsuarioAuthResponse usuarioAuthResponse = new UsuarioAuthResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getAtivo(),
                usuario.getSenhaHash(),
                usuario.getRoles());

        return ResponseEntity
                .ok()
                .body(usuarioAuthResponse);
    }
}
