package br.com.elastech.ms_publicacoes.dto.request;

public record ExcluirComentarioRequest(
        Integer idComentario,
        Integer idPublicacao,
        Integer idUsuario
) {
}
