package br.com.elastech.ms_publicacoes.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record CurtidaResponse(
        Integer id,
        Integer idPublicacao,
        LocalDateTime dataCurtida
) {
}
