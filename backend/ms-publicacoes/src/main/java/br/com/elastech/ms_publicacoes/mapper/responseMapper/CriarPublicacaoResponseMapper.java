package br.com.elastech.ms_publicacoes.mapper.responseMapper;

import br.com.elastech.ms_publicacoes.dto.response.CriarPublicacaoResponse;
import br.com.elastech.ms_publicacoes.mapper.Mapper;
import br.com.elastech.ms_publicacoes.model.Publicacao;

public class CriarPublicacaoResponseMapper implements Mapper<Publicacao, CriarPublicacaoResponse> {
    @Override
    public CriarPublicacaoResponse map(Publicacao publicacao) {
        return CriarPublicacaoResponse.builder()
                .usuarioId(publicacao.getUsuarioId())
                .conteudo(publicacao.getConteudo())
                .imagem(publicacao.getImagem())
                .build();
    }
}
