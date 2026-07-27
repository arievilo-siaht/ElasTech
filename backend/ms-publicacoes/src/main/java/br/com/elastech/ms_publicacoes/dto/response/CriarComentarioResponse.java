package br.com.elastech.ms_publicacoes.dto.response;

import br.com.elastech.ms_publicacoes.model.Publicacao;
import lombok.Builder;

@Builder
public record CriarComentarioResponse(
        Integer idPublicacao,
        Integer usuarioId,
        String conteudo
) {
}
