package br.com.elastech.ms_publicacoes.mapper.responseMapper;

import br.com.elastech.ms_publicacoes.dto.response.PublicacaoResponse;
import br.com.elastech.ms_publicacoes.mapper.Mapper;
import br.com.elastech.ms_publicacoes.model.Publicacao;

public class PublicacaoResponseMapper implements Mapper<Publicacao, PublicacaoResponse> {
    @Override
    public PublicacaoResponse map(Publicacao publicacao) {
        return PublicacaoResponse.builder()
                .usuarioId(publicacao.getId())
                .conteudo(publicacao.getConteudo())
                .imagem(publicacao.getImagem())
                .dataCriacao(publicacao.getDataCriacao())
                .status(publicacao.getStatus())
                .dataAtualizacao(publicacao.getDataAtualizacao())
                .comentarios(publicacao.getComentarios())
                .curtidas(publicacao.getCurtidas())
                .build();
    }
}
