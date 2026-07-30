package br.com.elastech.ms_publicacoes.controller;


import br.com.elastech.ms_publicacoes.dto.request.CriarComentarioRequest;
import br.com.elastech.ms_publicacoes.dto.request.EditarComentarioRequest;
import br.com.elastech.ms_publicacoes.dto.request.ExcluirComentarioRequest;
import br.com.elastech.ms_publicacoes.dto.response.ComentarioResponse;
import br.com.elastech.ms_publicacoes.dto.response.CriarComentarioResponse;
import br.com.elastech.ms_publicacoes.service.ComentarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    @PostMapping
    public ResponseEntity<CriarComentarioResponse> criar(
            @PathVariable Integer idPublicacao,
            @RequestBody CriarComentarioRequest request
    ) {
        CriarComentarioResponse response = comentarioService.criar(request, idPublicacao);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/publicacao/{idPublicacao}")
    public ResponseEntity<List<ComentarioResponse>> listarComentariosPorPublicacao(
            @PathVariable Integer idPublicacao
    ) {
        return ResponseEntity.ok(comentarioService.listarComentariosPorPublicacao(idPublicacao));
    }

    @PatchMapping
    public ResponseEntity<ComentarioResponse> editar(
            @RequestBody EditarComentarioRequest request
    ) {
        return ResponseEntity.ok(comentarioService.editar(request));
    }

    @DeleteMapping
    public ResponseEntity<Void> excluir (
            @RequestBody ExcluirComentarioRequest request
            ){
        comentarioService.excluir(request);
        return ResponseEntity.noContent().build();
    }
}
