package br.com.elastech.ms_publicacoes.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorEnum {
    PUBLICACAO_NAO_ENCONTRADA(
            "001",
            "Publicacão não encontrada.",
            HttpStatus.NOT_FOUND
    ),
    USUARIO_NAO_AUTORIZADO(
            "002",
            "Usuario não autorizado.",
            HttpStatus.UNAUTHORIZED
    ),
    CONTEUDO_INVALIDO(
            "003",
            "Contéudo não pode ser vazio.",
            HttpStatus.BAD_REQUEST

    ),
    PUBLICACAO_ARQUIVADA(
            "004",
            "Publicação ja está arquivada.",
            HttpStatus.BAD_REQUEST
    ),
    PUBLICACAO_PUBLICADA(
            "005",
            "Publicação ja está publicada.",
            HttpStatus.BAD_REQUEST
    ),
    ERRO_INTERNO(
            "006",
            "Ocorreu um erro interno no servidor. Tente novamente mais tarde.",
            HttpStatus.INTERNAL_SERVER_ERROR
    );

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    ErrorEnum(String errorCode, String errorMessage, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
