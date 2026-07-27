package br.com.elastech.ms_publicacoes.mapper.requestMapper;

import br.com.elastech.ms_publicacoes.dto.request.CriarComentarioRequest;
import br.com.elastech.ms_publicacoes.mapper.Mapper;
import br.com.elastech.ms_publicacoes.model.Comentario;
import org.springframework.stereotype.Component;

@Component
public class CriarComentarioRequestMapper implements Mapper<CriarComentarioRequest, Comentario> {
    @Override
    public Comentario map(CriarComentarioRequest request) {
        return Comentario.builder()
                .usuarioId(request.idUsuario())
                .conteudo(request.conteudo())
                .ativo(true)
                .build();
    }
}
