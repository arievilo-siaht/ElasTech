package br.com.elastech.ms_publicacoes.mapper.responseMapper;

import br.com.elastech.ms_publicacoes.dto.response.CriarComentarioResponse;
import br.com.elastech.ms_publicacoes.mapper.Mapper;
import br.com.elastech.ms_publicacoes.model.Comentario;
import br.com.elastech.ms_publicacoes.model.Publicacao;
import org.springframework.stereotype.Component;

@Component
public class CriarComentarioResponseMapper implements Mapper<Comentario, CriarComentarioResponse> {
    @Override
    public CriarComentarioResponse map(Comentario comentario) {
        return CriarComentarioResponse.builder()
                .usuarioId(comentario.getUsuarioId())
                .conteudo(comentario.getConteudo())
                .build();
    }
}
