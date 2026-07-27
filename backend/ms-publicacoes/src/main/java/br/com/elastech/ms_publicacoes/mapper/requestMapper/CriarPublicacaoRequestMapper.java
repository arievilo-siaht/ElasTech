package br.com.elastech.ms_publicacoes.mapper.requestMapper;

import br.com.elastech.ms_publicacoes.dto.request.CriarPublicacaoRequest;
import br.com.elastech.ms_publicacoes.enums.StatusPublicacao;
import br.com.elastech.ms_publicacoes.mapper.Mapper;
import br.com.elastech.ms_publicacoes.model.Publicacao;
import org.springframework.stereotype.Component;

@Component
public class CriarPublicacaoRequestMapper implements Mapper<CriarPublicacaoRequest, Publicacao> {
    @Override
    public Publicacao map(CriarPublicacaoRequest criarPublicacaoRequest) {
        return Publicacao.builder()
                .usuarioId(criarPublicacaoRequest.idUsuario())
                .conteudo(criarPublicacaoRequest.conteudo())
                .imagem(criarPublicacaoRequest.imagem())
                .dataAtualizacao(null)
                .status(StatusPublicacao.PUBLICADA)
                .comentarios(null)
                .curtidas(null)
                .build();
    }
}
