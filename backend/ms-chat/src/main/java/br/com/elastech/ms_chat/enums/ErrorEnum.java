package br.com.elastech.ms_chat.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorEnum {
    CONVERSA_NAO_ENCONTRADA(
            "001",
            "Conversa não encontrada.",
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
    CONVERSA_INVALIDA(
            "004",
            "Não é possivel criar uma conversa consigo mesmo.",
            HttpStatus.BAD_REQUEST
    ),
    CONVERSA_JA_EXISTE(
            "005",
            "Já existe uma conversa com esse usuário.",
            HttpStatus.BAD_REQUEST
    ),
    MENSAGEM_NAO_ENCONTRADA(
            "006",
            "Mensagem não encontrada.",
            HttpStatus.NOT_FOUND
    ),
    CONVERSA_ENCERRADA(
            "007",
            "A conversa ja foi encerrada",
            HttpStatus.BAD_REQUEST
    ),

    ERRO_INTERNO(
            "008",
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
