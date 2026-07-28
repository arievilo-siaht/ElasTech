package br.com.elastech.ms_publicacoes.dto.request;

import lombok.Builder;

@Builder
public record CriarPublicacaoRequest(
        Integer idUsuario,
        String conteudo,
        String imagem

) {
}
