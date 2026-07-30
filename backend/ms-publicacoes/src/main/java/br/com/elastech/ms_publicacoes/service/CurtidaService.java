package br.com.elastech.ms_publicacoes.service;

import br.com.elastech.ms_publicacoes.dto.request.CurtidaRequest;
import br.com.elastech.ms_publicacoes.dto.response.CurtidaResponse;
import br.com.elastech.ms_publicacoes.enums.ErrorEnum;
import br.com.elastech.ms_publicacoes.enums.StatusPublicacao;
import br.com.elastech.ms_publicacoes.exception.BaseException;
import br.com.elastech.ms_publicacoes.mapper.requestMapper.CurtidaRequestMapper;
import br.com.elastech.ms_publicacoes.mapper.responseMapper.CurtidaResponseMapper;
import br.com.elastech.ms_publicacoes.model.Curtida;
import br.com.elastech.ms_publicacoes.model.Publicacao;
import br.com.elastech.ms_publicacoes.repository.CurtidaRepository;
import br.com.elastech.ms_publicacoes.repository.PublicacaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CurtidaService {
    private final CurtidaRepository curtidaRepository;
    private final PublicacaoRepository publicacaoRepository;
    private final CurtidaRequestMapper requestMapper;
    private final CurtidaResponseMapper responseMapper;

    @Transactional
    public CurtidaResponse curtir(CurtidaRequest request) {

        if (request.idUsuario() == null) {
            throw new BaseException(ErrorEnum.USUARIO_NAO_AUTORIZADO);
        }

        Publicacao publicacao = buscarPublicacao(request.idPublicacao());

        Curtida curtida = curtidaRepository
                .findByPublicacaoIdAndUsuarioId(
                        request.idPublicacao(),
                        request.idUsuario())
                .orElse(null);

        if (curtida != null) {

            if (curtida.isAtivo()) {
                throw new BaseException(ErrorEnum.PUBLICACAO_JA_CURTIDA);
            }

            curtida.setAtivo(true);
            curtida.setDataCurtida(LocalDateTime.now());

            return responseMapper.map(curtida);
        }

        Curtida novaCurtida = requestMapper.map(request);
        novaCurtida.setPublicacao(publicacao);
        novaCurtida.setAtivo(true);
        novaCurtida.setDataCurtida(LocalDateTime.now());

        curtidaRepository.save(novaCurtida);

        return responseMapper.map(novaCurtida);
    }

    @Transactional
    public void descurtir(CurtidaRequest request) {

        Curtida curtida = curtidaRepository
                .findByPublicacaoIdAndUsuarioId(
                        request.idPublicacao(),
                        request.idUsuario())
                .orElseThrow(() ->
                        new BaseException(ErrorEnum.CURTIDA_NAO_ENCONTRADA));

        if (!curtida.isAtivo()) {
            throw new BaseException(ErrorEnum.CURTIDA_NAO_ENCONTRADA);
        }

        curtida.setAtivo(false);
    }

    public List<CurtidaResponse> listarCurtidas(Integer idPublicacao) {

        buscarPublicacao(idPublicacao);

        return curtidaRepository
                .findByPublicacaoIdAndAtivoTrue(idPublicacao)
                .stream()
                .map(responseMapper::map)
                .toList();
    }

    public boolean usuarioJaCurtiu(Integer idPublicacao, Integer idUsuario) {

        return curtidaRepository
                .existsByPublicacaoIdAndUsuarioIdAndAtivoTrue(
                        idPublicacao,
                        idUsuario);
    }

    private Publicacao buscarPublicacao(Integer idPublicacao) {
        Publicacao publicacao = publicacaoRepository.findById(idPublicacao)
                .orElseThrow(() ->
                        new BaseException(ErrorEnum.PUBLICACAO_NAO_ENCONTRADA));

        if (publicacao.getStatus() == StatusPublicacao.EXCLUIDA) {
            throw new BaseException(ErrorEnum.PUBLICACAO_NAO_ENCONTRADA);
        }

        return publicacao;
    }
}
