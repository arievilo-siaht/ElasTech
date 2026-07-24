package br.com.atdevs.ms_publicacoes.dto.request;

public record CriarPublicacaoRequest(
        Integer idUsuario,
        String conteudo,
        String imagem

) {
}
