package br.com.atdevs.ms_publicacoes.dto.request;

public record CriarPublicacaoRequest(
        Long idUsuario,
        String conteudo,
        String imagem

) {
}
