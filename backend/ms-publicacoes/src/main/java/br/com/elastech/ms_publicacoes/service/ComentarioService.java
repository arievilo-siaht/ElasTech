package br.com.elastech.ms_publicacoes.service;

import br.com.elastech.ms_publicacoes.dto.request.CriarComentarioRequest;
import br.com.elastech.ms_publicacoes.dto.response.CriarComentarioResponse;
import br.com.elastech.ms_publicacoes.enums.ErrorEnum;
import br.com.elastech.ms_publicacoes.exception.BaseException;
import br.com.elastech.ms_publicacoes.model.Publicacao;
import br.com.elastech.ms_publicacoes.repository.ComentarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComentarioService {
    private final ComentarioRepository repository;

    public CriarComentarioResponse criar(CriarComentarioRequest request){
        return null;
    }

    private Publicacao buscarPublicacao(Integer idPublicacao){
        return repository.findById(idPublicacao).orElseThrow(()->new BaseException(ErrorEnum.PUBLICACAO_NAO_ENCONTRADA)).getPublicacao();
    }
}
