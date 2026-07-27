package br.com.elastech.ms_publicacoes.mapper.responseMapper;

import br.com.elastech.ms_publicacoes.dto.response.CriarComentarioResponse;
import br.com.elastech.ms_publicacoes.mapper.Mapper;
import br.com.elastech.ms_publicacoes.model.Publicacao;

public class CriarComentarioResponseMapper implements Mapper<Publicacao, CriarComentarioResponse> {
    @Override
    public CriarComentarioResponse map(Publicacao publicacao) {
        return CriarComentarioResponse.builder()
                .idPublicacao(publicacao.getId())
                .usuarioId(publicacao.getUsuarioId())
                .conteudo(publicacao.getConteudo())
                .build();
    }
}
