package br.com.elastech.ms_chat.dto.response;

import br.com.elastech.ms_chat.enums.StatusMensagem;
import br.com.elastech.ms_chat.model.Conversa;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record MensagemResponse(
        Integer id,
        Integer idConversa,
        Integer remetenteId,
        Integer destinatarioId,
        String conteudo,
        LocalDateTime dataEnvio,
        StatusMensagem statusMensagem
) {
}
