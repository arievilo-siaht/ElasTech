package br.com.elastech.ms_chat.mapper.requestMapper;

import br.com.elastech.ms_chat.dto.request.MensagemRequest;
import br.com.elastech.ms_chat.mapper.Mapper;
import br.com.elastech.ms_chat.model.Mensagem;
import org.springframework.stereotype.Component;

@Component
public class MensagemRequestMapper implements Mapper<MensagemRequest, Mensagem> {
    @Override
    public Mensagem map(MensagemRequest request) {
        return Mensagem.builder()
                .remetenteId(request.remetenteId())
                .destinatarioId(request.destinatarioId())
                .conteudo(request.conteudo())
                .build();
    }
}
