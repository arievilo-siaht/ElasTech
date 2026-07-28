package br.com.elastech.ms_publicacoes.controller;

import br.com.elastech.ms_publicacoes.dto.request.CurtidaRequest;
import br.com.elastech.ms_publicacoes.dto.response.CurtidaResponse;
import br.com.elastech.ms_publicacoes.service.CurtidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/curtidas")
@RequiredArgsConstructor
public class CurtidaController {
    private final CurtidaService curtidaService;

    @PostMapping
    public ResponseEntity<CurtidaResponse> curtir(
            @RequestBody CurtidaRequest request
    ) {
        return ResponseEntity.ok(curtidaService.curtir(request));
    }

    @DeleteMapping
    public ResponseEntity<Void> descurtir(
            @RequestBody CurtidaRequest request
    ) {
        curtidaService.descurtir(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/publicacao/{idPublicacao}")
    public ResponseEntity<List<CurtidaResponse>> listarCurtidas(
            @PathVariable Integer idPublicacao
    ) {

        return ResponseEntity.ok(curtidaService.listarCurtidas(idPublicacao));
    }

    @GetMapping("/publicacao/{idPublicacao}/usuario/{idUsuario}")
    public ResponseEntity<Boolean> usuarioJaCurtiu(
            @PathVariable Integer idPublicacao,
            @PathVariable Integer idUsuario) {

        return ResponseEntity.ok(curtidaService.usuarioJaCurtiu(idPublicacao, idUsuario));
    }
}


