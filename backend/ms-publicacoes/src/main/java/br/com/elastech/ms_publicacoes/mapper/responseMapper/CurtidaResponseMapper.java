package br.com.elastech.ms_publicacoes.mapper.responseMapper;

import br.com.elastech.ms_publicacoes.dto.response.CurtidaResponse;
import br.com.elastech.ms_publicacoes.mapper.Mapper;
import br.com.elastech.ms_publicacoes.model.Curtida;
import jakarta.persistence.Column;
import org.springframework.stereotype.Component;

@Component
public class CurtidaResponseMapper implements Mapper<Curtida, CurtidaResponse> {
    @Override
    public CurtidaResponse map(Curtida curtida) {
        return CurtidaResponse.builder()
                .id(curtida.getId())
                .idPublicacao(curtida.getPublicacao().getId())
                .dataCurtida(curtida.getDataCurtida()).build();
    }
}
