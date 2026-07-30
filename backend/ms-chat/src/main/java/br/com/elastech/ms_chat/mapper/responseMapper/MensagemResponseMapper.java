package br.com.elastech.ms_chat.mapper.responseMapper;

import br.com.elastech.ms_chat.dto.response.ConversaResponse;
import br.com.elastech.ms_chat.dto.response.MensagemResponse;
import br.com.elastech.ms_chat.mapper.Mapper;
import br.com.elastech.ms_chat.model.Conversa;
import br.com.elastech.ms_chat.model.Mensagem;
import org.springframework.stereotype.Component;

@Component
public class MensagemResponseMapper implements Mapper<Mensagem, MensagemResponse> {
    @Override
    public MensagemResponse map(Mensagem mensagem) {
        return MensagemResponse.builder()
                .id(mensagem.getId())
                .idConversa(mensagem.getConversa().getId())
                .remetenteId(mensagem.getRemetenteId())
                .destinatarioId(mensagem.getDestinatarioId())
                .conteudo(mensagem.getConteudo())
                .dataEnvio(mensagem.getDataEnvio())
                .statusMensagem(mensagem.getStatusMensagem())
                .build();
    }
}
