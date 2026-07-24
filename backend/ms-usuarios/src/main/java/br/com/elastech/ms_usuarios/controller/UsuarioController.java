package br.com.elastech.ms_usuarios.controller;

import br.com.elastech.ms_usuarios.dto.request.AtualizarUsuarioRequest;
import br.com.elastech.ms_usuarios.dto.request.CriarUsuarioRequest;
import br.com.elastech.ms_usuarios.dto.response.UsuarioResponse;
import br.com.elastech.ms_usuarios.entities.Usuario;
import br.com.elastech.ms_usuarios.mapper.UsuarioResponseMapper;
import br.com.elastech.ms_usuarios.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final UsuarioResponseMapper usuarioResponseMapper;

    public UsuarioController(UsuarioService usuarioService, UsuarioResponseMapper usuarioResponseMapper) {
        this.usuarioService = usuarioService;
        this.usuarioResponseMapper = usuarioResponseMapper;
    }

    @PostMapping
    private ResponseEntity<Void> create(
            @RequestBody CriarUsuarioRequest usuarioRequest
    ) {
        Usuario usuario = usuarioService.create(usuarioRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/{id}")
    private ResponseEntity<UsuarioResponse> findById(
            @PathVariable Integer id
    ) {
        Usuario usuario = usuarioService.read(id);

        return ResponseEntity
                .ok()
                .body(usuarioResponseMapper.toResponse(usuario));
    }

    @GetMapping
    private ResponseEntity<List<UsuarioResponse>> findAll() {
        List<Usuario> listaUsuario = usuarioService.readAll();

        List<UsuarioResponse> listaUsuarioResponse = listaUsuario
                .stream()
                .map(usuarioResponseMapper::toResponse)
                .toList();

        return ResponseEntity
                .ok()
                .body(listaUsuarioResponse);
    }

    @PatchMapping("/{id}")
    private ResponseEntity<Void> pacth(
            @RequestBody AtualizarUsuarioRequest atualizarUsuarioRequest,
            @PathVariable Integer id
    ) {
        usuarioService.update(atualizarUsuarioRequest, id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> delete(
            @PathVariable Integer id
    ) {
        usuarioService.delete(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}