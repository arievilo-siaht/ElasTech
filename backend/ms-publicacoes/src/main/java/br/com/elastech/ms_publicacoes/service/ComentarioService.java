package br.com.elastech.ms_publicacoes.service;

import br.com.elastech.ms_publicacoes.dto.request.CriarComentarioRequest;
import br.com.elastech.ms_publicacoes.dto.request.EditarComentarioRequest;
import br.com.elastech.ms_publicacoes.dto.request.ExcluirComentarioRequest;
import br.com.elastech.ms_publicacoes.dto.response.ComentarioResponse;
import br.com.elastech.ms_publicacoes.dto.response.CriarComentarioResponse;
import br.com.elastech.ms_publicacoes.enums.ErrorEnum;
import br.com.elastech.ms_publicacoes.enums.StatusPublicacao;
import br.com.elastech.ms_publicacoes.exception.BaseException;
import br.com.elastech.ms_publicacoes.mapper.requestMapper.CriarComentarioRequestMapper;
import br.com.elastech.ms_publicacoes.mapper.responseMapper.ComentarioResponseMapper;
import br.com.elastech.ms_publicacoes.mapper.responseMapper.CriarComentarioResponseMapper;
import br.com.elastech.ms_publicacoes.model.Comentario;
import br.com.elastech.ms_publicacoes.model.Publicacao;
import br.com.elastech.ms_publicacoes.repository.ComentarioRepository;
import br.com.elastech.ms_publicacoes.repository.PublicacaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ComentarioService {
    private final ComentarioRepository comentarioRepository;
    private final PublicacaoRepository publicacaoRepository;
    private final CriarComentarioRequestMapper criarComentarioRequestMapper;
    private final CriarComentarioResponseMapper criarComentarioResponseMapper;
    private final ComentarioResponseMapper comentarioResponseMapper;

    @Transactional
    public CriarComentarioResponse criar(CriarComentarioRequest request) {
        if (request.conteudo() == null || request.conteudo().isBlank()) {
            throw new BaseException(ErrorEnum.CONTEUDO_INVALIDO);
        }
        if (request.idUsuario() == null) {
            throw new BaseException(ErrorEnum.USUARIO_NAO_AUTORIZADO);
        }
        Publicacao publicacao = buscarPublicacao(request.idPublicacao());
        validarStatusPublicacao(publicacao);
        Comentario comentario = criarComentarioRequestMapper.map(request);
        comentario.setPublicacao(publicacao);
        comentarioRepository.save(comentario);

        return criarComentarioResponseMapper.map(comentario);

    }

    private Publicacao buscarPublicacao(Integer idPublicacao) {
        return publicacaoRepository.findById(idPublicacao).orElseThrow(() -> new BaseException(ErrorEnum.PUBLICACAO_NAO_ENCONTRADA));
    }

    private Comentario buscarComentario(Integer idComentario) {
        return comentarioRepository.findById(idComentario).orElseThrow(() -> new BaseException(ErrorEnum.COMENTARIO_NAO_ENCONTRADO));
    }

    private void validarStatusPublicacao(Publicacao publicacao) {
        if (publicacao.getStatus() == StatusPublicacao.ARQUIVADA || publicacao.getStatus() == StatusPublicacao.EXCLUIDA) {
            throw new BaseException(ErrorEnum.PUBLICACAO_NAO_ENCONTRADA);
        }
    }

    public List<ComentarioResponse> listarComentariosPorPublicacao(Integer idPublicacao) {
        Publicacao publicacao = buscarPublicacao(idPublicacao);
        validarStatusPublicacao(publicacao);

        return comentarioRepository.findByPublicacaoIdAndAtivoTrueOrderByDataCriacaoAsc(idPublicacao)
                .stream()
                .map(comentarioResponseMapper::map)
                .toList();

    }

    @Transactional
    public ComentarioResponse editar(EditarComentarioRequest request) {
        if (request.conteudo() == null || request.conteudo().isBlank()) {
            throw new BaseException(ErrorEnum.CONTEUDO_INVALIDO);
        }

        Publicacao publicacao = buscarPublicacao(request.idPublicacao());
        validarStatusPublicacao(publicacao);

        Comentario comentario = buscarComentario(request.id());
        if (!comentario.isAtivo()) {
            throw new BaseException(ErrorEnum.COMENTARIO_NAO_ENCONTRADO);
        }
        if (!Objects.equals(comentario.getUsuarioId(), request.idUsuario())) {
            throw new BaseException(ErrorEnum.USUARIO_NAO_AUTORIZADO);
        }


        comentario.setConteudo(request.conteudo());
        return comentarioResponseMapper.map(comentario);
    }

    @Transactional
    public void excluir(ExcluirComentarioRequest request) {
        Comentario comentario = buscarComentario(request.idComentario());
        Publicacao publicacao = buscarPublicacao(request.idPublicacao());

        if (!Objects.equals(comentario.getUsuarioId(), request.idUsuario())) {
            throw new BaseException(ErrorEnum.USUARIO_NAO_AUTORIZADO);
        }
        if (!comentario.isAtivo()) {
            throw new BaseException(ErrorEnum.COMENTARIO_NAO_ENCONTRADO);
        }
        comentario.setAtivo(false);
    }


}
