package br.com.atdevs.ms_publicacoes.service;

import br.com.atdevs.ms_publicacoes.dto.request.CriarPublicacaoRequest;
import br.com.atdevs.ms_publicacoes.dto.request.EditarPublicacaoRequest;
import br.com.atdevs.ms_publicacoes.dto.response.PublicacoesResponse;
import br.com.atdevs.ms_publicacoes.dto.response.CriarPublicacaoResponse;
import br.com.atdevs.ms_publicacoes.enums.ErrorEnum;
import br.com.atdevs.ms_publicacoes.enums.StatusPublicacao;
import br.com.atdevs.ms_publicacoes.exception.BaseException;
import br.com.atdevs.ms_publicacoes.model.Publicacao;
import br.com.atdevs.ms_publicacoes.repository.PublicacaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicacaoService {

    private PublicacaoRepository repository;

    @Transactional
    public CriarPublicacaoResponse criarPublicacao(CriarPublicacaoRequest request) {
        if (request.conteudo().isEmpty() || request.conteudo().isBlank()) {
            throw new BaseException(ErrorEnum.CONTEUDO_INVALIDO);
        }
        if (request.idUsuario() == null) {
            throw new BaseException(ErrorEnum.USUARIO_NAO_AUTORIZADO);
        }

        repository.save(Publicacao.builder()
                .usuarioId(request.idUsuario())
                .conteudo(request.conteudo())
                .imagem(request.imagem())
                .dataAtualizacao(null)
                .status(StatusPublicacao.PUBLICADA)
                .comentarios(null)
                .curtidas(null)
                .build());

        return CriarPublicacaoResponse.builder()
                .usuarioId(request.idUsuario())
                .conteudo(request.conteudo())
                .imagem(request.imagem())
                .build();

    }

    public PublicacoesResponse buscarPorId(Integer id){
       Publicacao publicacao = repository.findById(id).orElseThrow(()-> new BaseException(ErrorEnum.PUBLICACAO_NAO_ENCONTRADA));

       if (publicacao.getStatus().equals(StatusPublicacao.ARQUIVADA) ||
       publicacao.getStatus().equals(StatusPublicacao.EXCLUIDA)){
           throw new BaseException(ErrorEnum.PUBLICACAO_NAO_ENCONTRADA);
       }

      return PublicacoesResponse.builder()
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

    public List<Publicacao> listarFeed(){
        List<Publicacao> publicacoes = repository.findAll()
                .stream()
                .sorted(Comparator.comparing(Publicacao::getDataCriacao).reversed())
                .toList();

        return publicacoes;
    }

    public List<Publicacao> listarPorUsuario(Long usuarioId){
        List<Publicacao> publicacoes = repository.findByUsuarioIdOrderByDataCriacaoDesc(usuarioId);

        return publicacoes;
    }

    @Transactional
    public Publicacao editar(EditarPublicacaoRequest request, Integer idPublicacao){
        Publicacao publicacao = repository.findById(idPublicacao).orElseThrow(()->new BaseException(ErrorEnum.PUBLICACAO_NAO_ENCONTRADA));

        if (request.conteudo()!= null){
            publicacao.setConteudo(request.conteudo());
        }
        if(request.imagem()!= null){
            publicacao.setImagem(request.imagem());
        }
        return publicacao;
    }
}
