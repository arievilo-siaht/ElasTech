package br.com.elastech.ms_chat.controller;

import br.com.elastech.ms_chat.dto.request.ConversaRequest;
import br.com.elastech.ms_chat.dto.response.ConversaResponse;
import br.com.elastech.ms_chat.service.ConversaService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/conversas")
public class ConversaController {
    private final ConversaService conversaService;

    @PostMapping
    public ResponseEntity<ConversaResponse> criarConversa(
            @RequestBody ConversaRequest conversaRequest
            ){
        ConversaResponse conversaResponse = conversaService.criarConversa(conversaRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(conversaResponse);
    }

    @GetMapping("/{idConversa}")
    public ResponseEntity<ConversaResponse> buscarConversa(
            @PathVariable Integer idConversa
    ){
        return ResponseEntity.ok(conversaService.buscarConversa(idConversa));
    }

    @GetMapping
    public ResponseEntity<List<ConversaResponse>> listarConversasUsuario(
            @RequestBody ConversaRequest conversaRequest
    ){
        return ResponseEntity.ok(conversaService.listarConversasUsuario(conversaRequest));
    }

    @DeleteMapping("/{idConversa}")
    public ResponseEntity<Void> excluirConversa(
            @PathVariable Integer idConversa
    ){
        conversaService.excluirConversa(idConversa);
        return ResponseEntity.noContent().build();
    }
}
