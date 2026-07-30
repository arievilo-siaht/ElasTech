package br.com.elastech.ms_chat.service;

import br.com.elastech.ms_chat.dto.request.ConversaRequest;
import br.com.elastech.ms_chat.dto.response.ConversaResponse;
import br.com.elastech.ms_chat.enums.ErrorEnum;
import br.com.elastech.ms_chat.exception.BaseException;
import br.com.elastech.ms_chat.mapper.requestMapper.ConversaRequestMapper;
import br.com.elastech.ms_chat.mapper.responseMapper.ConversaResponseMapper;
import br.com.elastech.ms_chat.model.Conversa;
import br.com.elastech.ms_chat.repository.ConversaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConversaService {
    private final ConversaRepository conversaRepository;
    private final ConversaRequestMapper conversaRequestMapper;
    private final ConversaResponseMapper conversaResponseMapper;

    @Transactional
    public ConversaResponse criarConversa(ConversaRequest conversaRequest) {
        Integer usuario1Id = conversaRequest.usuario1Id();
        Integer usuario2Id = conversaRequest.usuario2Id();

        if (usuario1Id == null || usuario2Id == null) {
            throw new BaseException(ErrorEnum.USUARIO_NAO_AUTORIZADO);
        }

        if (usuario1Id.equals(usuario2Id)) {
            throw new BaseException(ErrorEnum.CONVERSA_INVALIDA);
        }

        // Ordena os IDs para que a conversa (1,2) e (2,1) sejam consideradas a mesma.
        if (usuario1Id > usuario2Id) {
            Integer aux = usuario1Id;
            usuario1Id = usuario2Id;
            usuario2Id = aux;
        }

        if (buscarConversaPeloIdUsuario(usuario1Id, usuario2Id).isPresent()) {
            throw new BaseException(ErrorEnum.CONVERSA_JA_EXISTE);
        }

        Conversa conversa = conversaRequestMapper.map(conversaRequest);
        conversa.setUsuario1Id(usuario1Id);
        conversa.setUsuario2Id(usuario2Id);
        conversa.setAtiva(true);
        conversaRepository.save(conversa);

        return conversaResponseMapper.map(conversa);
    }

    private Optional<Conversa> buscarConversaPeloIdUsuario(Integer usuario1Id, Integer usuario2Id) {
        return conversaRepository.findByUsuario1IdAndUsuario2Id(usuario1Id, usuario2Id);
    }

    private Conversa buscarConversaPeloId(Integer idConversa) {
        return conversaRepository.findById(idConversa).orElseThrow(() -> new BaseException(ErrorEnum.CONVERSA_NAO_ENCONTRADA));
    }

    public ConversaResponse buscarConversa(Integer idConversa) {
        Conversa conversa = buscarConversaPeloId(idConversa);
        return conversaResponseMapper.map(conversa);
    }

    public List<ConversaResponse> listarConversasUsuario(ConversaRequest conversaRequest) {

        return conversaRepository.findByUsuario1IdOrUsuario2IdOrderByDataCriacaoDesc(
                conversaRequest.usuario1Id(), conversaRequest.usuario2Id())
                .stream()
                .map(conversaResponseMapper::map)
                .toList();
    }

    @Transactional
    public void excluirConversa(Integer idConversa){
        Conversa conversa = buscarConversaPeloId(idConversa);
        conversa.setAtiva(false);
    }

}
