package br.com.elastech.ms_chat.dto.response;

import br.com.elastech.ms_chat.model.Mensagem;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ConversaResponse(
        Integer usuario1Id,
        Integer usuario2Id,
        List<Mensagem> mensagens,
        LocalDateTime dataCriacao,
        boolean ativa
) {
}
