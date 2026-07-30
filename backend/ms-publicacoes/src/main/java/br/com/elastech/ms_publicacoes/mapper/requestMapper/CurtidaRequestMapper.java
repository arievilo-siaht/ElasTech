package br.com.elastech.ms_publicacoes.mapper.requestMapper;

import br.com.elastech.ms_publicacoes.dto.request.CurtidaRequest;
import br.com.elastech.ms_publicacoes.mapper.Mapper;
import br.com.elastech.ms_publicacoes.model.Curtida;
import org.springframework.stereotype.Component;

@Component
public class CurtidaRequestMapper implements Mapper<CurtidaRequest, Curtida> {
    @Override
    public Curtida map(CurtidaRequest request) {
        return Curtida.builder()
                .usuarioId(request.idUsuario())
                .ativo(true)
                .build();
    }
}
