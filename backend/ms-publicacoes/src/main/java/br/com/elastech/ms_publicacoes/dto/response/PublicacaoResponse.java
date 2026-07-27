package br.com.elastech.ms_publicacoes.dto.response;

import br.com.elastech.ms_publicacoes.enums.StatusPublicacao;
import br.com.elastech.ms_publicacoes.model.Comentario;
import br.com.elastech.ms_publicacoes.model.Curtida;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PublicacaoResponse(
        Integer usuarioId,
        String conteudo,
        String imagem,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao,
        StatusPublicacao status,
        List<Comentario> comentarios,
        List<Curtida> curtidas
) {
}
