package br.com.elastech.ms_chat.dto.request;

public record ConversaRequest(
        Integer usuario1Id,
        Integer usuario2Id
) {
}
