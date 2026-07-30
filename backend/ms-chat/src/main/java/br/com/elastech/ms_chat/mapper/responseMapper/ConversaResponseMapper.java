package br.com.elastech.ms_chat.mapper.responseMapper;

import br.com.elastech.ms_chat.dto.response.ConversaResponse;
import br.com.elastech.ms_chat.mapper.Mapper;
import br.com.elastech.ms_chat.model.Conversa;
import org.springframework.stereotype.Component;

@Component
public class ConversaResponseMapper implements Mapper<Conversa, ConversaResponse> {
    @Override
    public ConversaResponse map(Conversa conversa) {
        return ConversaResponse.builder()
                .usuario1Id(conversa.getUsuario1Id())
                .usuario2Id(conversa.getUsuario2Id())
                .dataCriacao(conversa.getDataCriacao())
                .ativa(conversa.isAtiva())
                .build();
    }
}
