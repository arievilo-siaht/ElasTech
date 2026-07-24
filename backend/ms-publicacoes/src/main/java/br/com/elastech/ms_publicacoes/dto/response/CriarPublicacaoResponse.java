package br.com.elastech.ms_publicacoes.dto.response;

import lombok.Builder;

@Builder
public record CriarPublicacaoResponse(
        Integer usuarioId,
        String conteudo,
        String imagem
) {
}
