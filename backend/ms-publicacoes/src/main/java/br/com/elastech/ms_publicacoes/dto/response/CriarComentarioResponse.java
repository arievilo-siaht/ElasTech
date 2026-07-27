package br.com.elastech.ms_publicacoes.dto.response;

import lombok.Builder;

@Builder
public record CriarComentarioResponse(
        Integer idPublicacao,
        Integer usuarioId,
        String conteudo
) {
}
