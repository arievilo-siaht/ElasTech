package br.com.elastech.ms_publicacoes.service;

import br.com.elastech.ms_publicacoes.dto.request.CriarPublicacaoRequest;
import br.com.elastech.ms_publicacoes.dto.request.EditarPublicacaoRequest;
import br.com.elastech.ms_publicacoes.dto.response.PublicacaoResponse;
import br.com.elastech.ms_publicacoes.dto.response.CriarPublicacaoResponse;
import br.com.elastech.ms_publicacoes.enums.ErrorEnum;
import br.com.elastech.ms_publicacoes.enums.StatusPublicacao;
import br.com.elastech.ms_publicacoes.exception.BaseException;
import br.com.elastech.ms_publicacoes.mapper.requestMapper.CriarPublicacaoRequestMapper;
import br.com.elastech.ms_publicacoes.mapper.responseMapper.CriarPublicacaoResponseMapper;
import br.com.elastech.ms_publicacoes.mapper.responseMapper.PublicacaoResponseMapper;
import br.com.elastech.ms_publicacoes.model.Publicacao;
import br.com.elastech.ms_publicacoes.repository.PublicacaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicacaoService {

    private PublicacaoRepository repository;
    private final CriarPublicacaoRequestMapper criarPublicacaoRequestMapper;
    private final CriarPublicacaoResponseMapper criarPublicacaoResponseMapper;
    private final PublicacaoResponseMapper publicacaoResponseMapper;

    @Transactional
    public CriarPublicacaoResponse criarPublicacao(CriarPublicacaoRequest request) {

        if (request.conteudo() == null || request.conteudo().isBlank()) {
            throw new BaseException(ErrorEnum.CONTEUDO_INVALIDO);
        }

        if (request.idUsuario() == null) {
            throw new BaseException(ErrorEnum.USUARIO_NAO_AUTORIZADO);
        }

        Publicacao publicacao = criarPublicacaoRequestMapper.map(request);

        repository.save(publicacao);

        return criarPublicacaoResponseMapper.map(publicacao);
    }

    private Publicacao buscarPublicacao(Integer idPublicacao) {
        Publicacao publicacao = repository.findById(idPublicacao)
                .orElseThrow(() ->
                        new BaseException(ErrorEnum.PUBLICACAO_NAO_ENCONTRADA));

        if (publicacao.getStatus() == StatusPublicacao.EXCLUIDA) {
            throw new BaseException(ErrorEnum.PUBLICACAO_NAO_ENCONTRADA);
        }

        return publicacao;
    }

    private void validarPublicacaoEditavel(Publicacao publicacao) {
        if (publicacao.getStatus() == StatusPublicacao.ARQUIVADA) {
            throw new BaseException(ErrorEnum.PUBLICACAO_ARQUIVADA);
        }
    }


    public PublicacaoResponse buscarPorId(Integer idPublicacao) {
        Publicacao publicacao = buscarPublicacao(idPublicacao);
        return publicacaoResponseMapper.map(publicacao);
    }

    public List<PublicacaoResponse> listarFeed() {
        return repository.findByStatusOrderByDataCriacaoDesc(StatusPublicacao.PUBLICADA)
                .stream()
                .map(publicacaoResponseMapper::map)
                .toList();
    }

    public List<PublicacaoResponse> listarPorUsuario(Integer usuarioId) {
        return repository.findByUsuarioIdOrderByDataCriacaoDesc(usuarioId)
                .stream()
                .map(publicacaoResponseMapper::map)
                .toList();
    }

    @Transactional
    public PublicacaoResponse editar(EditarPublicacaoRequest request, Integer idPublicacao) {
        Publicacao publicacao = buscarPublicacao(idPublicacao);

        validarPublicacaoEditavel(publicacao);

        if (request.conteudo() != null && !request.conteudo().isBlank()) {
            publicacao.setConteudo(request.conteudo());
        }

        if (request.imagem() != null) {
            publicacao.setImagem(request.imagem());
        }
        publicacao.setDataAtualizacao(LocalDateTime.now());

        return publicacaoResponseMapper.map(publicacao);
    }

    @Transactional
    public void arquivar(Integer idPublicacao) {
        Publicacao publicacao = buscarPublicacao(idPublicacao);
        validarPublicacaoEditavel(publicacao);

        publicacao.setStatus(StatusPublicacao.ARQUIVADA);
    }

    @Transactional
    public void desarquivar(Integer idPublicacao){
        Publicacao publicacao = buscarPublicacao(idPublicacao);
        if (publicacao.getStatus() == StatusPublicacao.PUBLICADA){
            throw new BaseException(ErrorEnum.PUBLICACAO_PUBLICADA);
        }
        publicacao.setStatus(StatusPublicacao.PUBLICADA);
    }

    @Transactional
    public void excluir(Integer idPublicacao) {
        Publicacao publicacao = buscarPublicacao(idPublicacao);

        publicacao.setStatus(StatusPublicacao.EXCLUIDA);
    }
}
