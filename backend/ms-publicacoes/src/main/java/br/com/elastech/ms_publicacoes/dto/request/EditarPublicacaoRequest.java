package br.com.elastech.ms_publicacoes.dto.request;

public record EditarPublicacaoRequest(
        String conteudo,
        String imagem
) {
}
