package br.com.elastech.ms_publicacoes.mapper.responseMapper;

import br.com.elastech.ms_publicacoes.dto.response.ComentarioResponse;
import br.com.elastech.ms_publicacoes.mapper.Mapper;
import br.com.elastech.ms_publicacoes.model.Comentario;
import org.springframework.stereotype.Component;

@Component
public class ComentarioResponseMapper implements Mapper<Comentario, ComentarioResponse> {
    @Override
    public ComentarioResponse map(Comentario comentario) {
        return ComentarioResponse.builder()
                .id(comentario.getId())
                .idPublicacao(comentario.getPublicacao().getId())
                .usuarioId(comentario.getUsuarioId())
                .conteudo(comentario.getConteudo())
                .dataCriacao(comentario.getDataCriacao())
                .build();
    }
}
