package br.com.elastech.ms_publicacoes.dto.request;

import lombok.Builder;

@Builder
public record EditarPublicacaoRequest(
        String conteudo,
        String imagem
) {
}
