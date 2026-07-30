package br.com.elastech.ms_chat.service;

import br.com.elastech.ms_chat.dto.request.MensagemRequest;
import br.com.elastech.ms_chat.dto.response.MensagemResponse;
import br.com.elastech.ms_chat.enums.ErrorEnum;
import br.com.elastech.ms_chat.enums.StatusMensagem;
import br.com.elastech.ms_chat.exception.BaseException;
import br.com.elastech.ms_chat.mapper.requestMapper.MensagemRequestMapper;
import br.com.elastech.ms_chat.mapper.responseMapper.MensagemResponseMapper;
import br.com.elastech.ms_chat.model.Conversa;
import br.com.elastech.ms_chat.model.Mensagem;
import br.com.elastech.ms_chat.repository.ConversaRepository;
import br.com.elastech.ms_chat.repository.MensagemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MensagemService {
    private final MensagemRepository mensagemRepository;
    private final ConversaRepository conversaRepository;
    private final MensagemRequestMapper mensagemRequestMapper;
    private final MensagemResponseMapper mensagemResponseMapper;

    @Transactional
    public MensagemResponse enviarMensagem(MensagemRequest mensagemRequest) {

        validarMensagem(mensagemRequest);

        Conversa conversa = buscarConversaEntreUsuarios(mensagemRequest.remetenteId(), mensagemRequest.destinatarioId());

        if (!conversa.isAtiva()) {
            throw new BaseException(ErrorEnum.CONVERSA_ENCERRADA);
        }

        Mensagem mensagem = mensagemRequestMapper.map(mensagemRequest);
        mensagem.setConversa(conversa);
        mensagem.setStatusMensagem(StatusMensagem.ENVIADA);

        mensagemRepository.save(mensagem);

        return mensagemResponseMapper.map(mensagem);
    }

    private Conversa buscarConversaEntreUsuarios(Integer remetenteId, Integer destinatarioId) {
        // Ordena os IDs para que a conversa (1,2) e (2,1) sejam consideradas a mesma.
        if (remetenteId > destinatarioId) {
            Integer aux = remetenteId;
            remetenteId = destinatarioId;
            destinatarioId = aux;
        }
        return conversaRepository.findByUsuario1IdAndUsuario2Id(remetenteId, destinatarioId).orElseThrow(() -> new BaseException(ErrorEnum.CONVERSA_NAO_ENCONTRADA));
    }

    private Conversa buscarConversaPorId(Integer idConversa) {
        return conversaRepository.findById(idConversa).orElseThrow(() -> new BaseException(ErrorEnum.CONVERSA_NAO_ENCONTRADA));
    }

    private Mensagem buscarMensagem(Integer mensagemId) {
        return mensagemRepository.findById(mensagemId).orElseThrow(() -> new BaseException(ErrorEnum.MENSAGEM_NAO_ENCONTRADA));
    }

    private void validarMensagem(MensagemRequest mensagemRequest){
        if (mensagemRequest.remetenteId() == null || mensagemRequest.destinatarioId() == null) {
            throw new BaseException(ErrorEnum.USUARIO_NAO_AUTORIZADO);
        }

        if (mensagemRequest.remetenteId().equals(mensagemRequest.destinatarioId())) {
            throw new BaseException(ErrorEnum.CONVERSA_INVALIDA);
        }
        if (mensagemRequest.conteudo() == null || mensagemRequest.conteudo().isBlank()) {
            throw new BaseException(ErrorEnum.CONTEUDO_INVALIDO);
        }
    }

    public List<MensagemResponse> listarMensagens(Integer idConversa) {

        buscarConversaPorId(idConversa);

        return mensagemRepository
                .findByConversaIdAndStatusMensagemNotOrderByDataEnvioAsc(
                        idConversa,
                        StatusMensagem.EXCLUIDA)
                .stream()
                .map(mensagemResponseMapper::map)
                .toList();

    }

    @Transactional
    public MensagemResponse marcarComoLida(Integer mensagemId, Integer usuarioLogado) {

        Mensagem mensagem = buscarMensagem(mensagemId);
        if (!mensagem.getDestinatarioId().equals(usuarioLogado)) {
            throw new BaseException(ErrorEnum.USUARIO_NAO_AUTORIZADO);
        }
        if (mensagem.getStatusMensagem() == StatusMensagem.EXCLUIDA) {
            throw new BaseException(ErrorEnum.MENSAGEM_NAO_ENCONTRADA);
        }

        if (mensagem.getStatusMensagem() == StatusMensagem.LIDA) {
            return mensagemResponseMapper.map(mensagem);
        }

        mensagem.setStatusMensagem(StatusMensagem.LIDA);

        return mensagemResponseMapper.map(mensagem);
    }

    @Transactional
    public MensagemResponse editarMensagem(MensagemRequest mensagemRequest, Integer mensagemId) {
        validarMensagem(mensagemRequest);

        Mensagem mensagem = buscarMensagem(mensagemId);
        if (!mensagem.getRemetenteId().equals(mensagemRequest.remetenteId())) {
            throw new BaseException(ErrorEnum.USUARIO_NAO_AUTORIZADO);
        }

        if (mensagem.getStatusMensagem() == StatusMensagem.EXCLUIDA) {
            throw new BaseException(ErrorEnum.MENSAGEM_NAO_ENCONTRADA);
        }

        mensagem.setConteudo(mensagemRequest.conteudo());
        mensagem.setStatusMensagem(StatusMensagem.EDITADA);

        return mensagemResponseMapper.map(mensagem);
    }

    @Transactional
    public void excluirMensagem(Integer mensagemId, Integer remetenteId) {
        Mensagem mensagem = buscarMensagem(mensagemId);
        if (!mensagem.getRemetenteId().equals(remetenteId)) {
            throw new BaseException(ErrorEnum.USUARIO_NAO_AUTORIZADO);
        }

        if (mensagem.getStatusMensagem() == StatusMensagem.EXCLUIDA) {
            throw new BaseException(ErrorEnum.MENSAGEM_NAO_ENCONTRADA);
        }

        mensagem.setStatusMensagem(StatusMensagem.EXCLUIDA);
    }

}



