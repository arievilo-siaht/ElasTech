package br.com.elastech.ms_publicacoes.dto.request;

public record EditarComentarioRequest(
        Integer id,
        Integer idUsuario,
        Integer idPublicacao,
        String conteudo
) {
}
