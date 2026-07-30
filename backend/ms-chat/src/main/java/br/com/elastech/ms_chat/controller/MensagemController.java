package br.com.elastech.ms_chat.controller;


import br.com.elastech.ms_chat.dto.request.MensagemRequest;
import br.com.elastech.ms_chat.dto.response.MensagemResponse;
import br.com.elastech.ms_chat.service.MensagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mensagens")
public class MensagemController {
    private final MensagemService mensagemService;

    @PostMapping
    public ResponseEntity<MensagemResponse> enviarMensagem(
            @RequestBody MensagemRequest mensagemRequest
    ) {
        MensagemResponse mensagemResponse = mensagemService.enviarMensagem(mensagemRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mensagemResponse);
    }

    @GetMapping(("/{idConversa}"))
    public ResponseEntity<List<MensagemResponse>> listarMensagens(
            @PathVariable Integer idConversa
    ) {
        return ResponseEntity.ok(mensagemService.listarMensagens(idConversa));
    }

    @PutMapping(("/idMensagem/{idMensagem}/idUsuario/{idUsuario}"))
    public ResponseEntity<MensagemResponse> marcarComoLida(
            @PathVariable Integer idMensagem,
            @PathVariable Integer idUsuario
    ) {
        return ResponseEntity.ok(mensagemService.marcarComoLida(idMensagem, idUsuario));
    }

    @PatchMapping(("/{idMensagem}"))
    public ResponseEntity<MensagemResponse> editarMensagem(
            @RequestBody MensagemRequest mensagemRequest,
            @PathVariable Integer idMensagem
    ) {
        return ResponseEntity.ok(mensagemService.editarMensagem(mensagemRequest, idMensagem));
    }

    @DeleteMapping(("/idMensagem/{idMensagem}/idRemetente/{idRemetente}"))
    public ResponseEntity<Void> excluirMensagem(
            @PathVariable Integer idMensagem,
            @PathVariable Integer idRemetente
    ){
        mensagemService.excluirMensagem(idMensagem,idRemetente);
        return ResponseEntity.noContent().build();
    }
}
