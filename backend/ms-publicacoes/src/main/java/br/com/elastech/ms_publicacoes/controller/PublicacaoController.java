package br.com.elastech.ms_publicacoes.controller;

import br.com.elastech.ms_publicacoes.dto.request.CriarPublicacaoRequest;
import br.com.elastech.ms_publicacoes.dto.request.EditarPublicacaoRequest;
import br.com.elastech.ms_publicacoes.dto.response.CriarPublicacaoResponse;
import br.com.elastech.ms_publicacoes.dto.response.PublicacaoResponse;
import br.com.elastech.ms_publicacoes.service.PublicacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/publicacao")
public class PublicacaoController {

    private PublicacaoService service;

    @PostMapping
    public ResponseEntity<CriarPublicacaoResponse> criar(
            @RequestBody CriarPublicacaoRequest request
    ) {
        CriarPublicacaoResponse response = service.criarPublicacao(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicacaoResponse> buscarPorId(
            @PathVariable Integer id
    ){
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PublicacaoResponse>> listarFeed(){
        return ResponseEntity.ok(service.listarFeed());
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<PublicacaoResponse>> listarPorUsuario(
            @PathVariable Integer id
    ){
        return ResponseEntity.ok(service.listarPorUsuario(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PublicacaoResponse> editar(
            @PathVariable Integer id,
            @RequestBody EditarPublicacaoRequest request
    ){
        return ResponseEntity.ok(service.editar(request,id));
    }

    @PatchMapping("/{id}/arquivar")
    public ResponseEntity<Void> arquivar(
            @PathVariable Integer id
    ){
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/desarquivar")
    public ResponseEntity<Void> desarquivar(
            @PathVariable Integer id
    ){
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Integer id
    ){
        return ResponseEntity.noContent().build();
    }


}
