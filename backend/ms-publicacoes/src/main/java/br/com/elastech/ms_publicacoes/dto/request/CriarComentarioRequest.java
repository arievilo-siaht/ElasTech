package br.com.elastech.ms_publicacoes.dto.request;

public record CriarComentarioRequest(
        Integer idPublicacao,
        Integer usuarioId,
        String conteudo
) {
}
