package br.com.elastech.ms_publicacoes.dto.request;

public record CriarPublicacaoRequest(
        String conteudo,
        String imagem
) {
}