package br.com.atdevs.ms_publicacoes.dto.request;

public record EditarPublicacaoRequest(
        String conteudo,
        String imagem
) {
}
