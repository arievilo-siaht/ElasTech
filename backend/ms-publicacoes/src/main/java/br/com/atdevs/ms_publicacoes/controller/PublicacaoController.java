package br.com.atdevs.ms_publicacoes.controller;

import br.com.atdevs.ms_publicacoes.dto.request.CriarPublicacaoRequest;
import br.com.atdevs.ms_publicacoes.dto.response.CriarPublicacaoResponse;
import br.com.atdevs.ms_publicacoes.dto.response.PublicacoesResponse;
import br.com.atdevs.ms_publicacoes.model.Publicacao;
import br.com.atdevs.ms_publicacoes.service.PublicacaoService;
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
    public ResponseEntity<PublicacoesResponse> buscarPorId(
            @PathVariable Integer id
    ){
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Publicacao>> listarFeed(){
        return ResponseEntity.ok(service.listarFeed());
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<Publicacao>> listarPorUsuario(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(service.listarPorUsuario(id));
    }

}
