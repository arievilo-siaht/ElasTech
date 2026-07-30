package br.com.elastech.ms_publicacoes.dto.response;

import br.com.elastech.ms_publicacoes.model.Comentario;
import br.com.elastech.ms_publicacoes.model.Publicacao;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ComentarioResponse(
        Integer id,
        Integer idPublicacao,
        Integer usuarioId,
        String conteudo,
        LocalDateTime dataCriacao
) {
}
