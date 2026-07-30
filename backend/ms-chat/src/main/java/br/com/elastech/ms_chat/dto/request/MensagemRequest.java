package br.com.elastech.ms_chat.dto.request;

public record MensagemRequest(
        Integer conversa,
        Integer remetenteId,
        Integer destinatarioId,
        String conteudo
) {
}
