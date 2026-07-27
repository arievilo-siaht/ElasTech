package br.com.elastech.ms_publicacoes.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CriarPublicacaoResponse(
        Integer usuarioId,
        String conteudo,
        String imagem,
        LocalDateTime dataCriacao
) {
}
