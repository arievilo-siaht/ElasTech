package br.com.elastech.ms_chat.mapper.requestMapper;

import br.com.elastech.ms_chat.dto.request.ConversaRequest;
import br.com.elastech.ms_chat.mapper.Mapper;
import br.com.elastech.ms_chat.model.Conversa;
import org.springframework.stereotype.Component;

@Component
public class ConversaRequestMapper implements Mapper<ConversaRequest, Conversa> {
    @Override
    public Conversa map(ConversaRequest request) {
        return Conversa.builder()
                .usuario1Id(request.usuario1Id())
                .usuario2Id(request.usuario2Id())
                .build();
    }
}
