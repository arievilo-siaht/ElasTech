package br.com.atdevs.ms_publicacoes.exception;

import br.com.atdevs.ms_publicacoes.enums.ErrorEnum;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorEnum errorEnum;

    public BaseException(ErrorEnum errorEnum) {
        super(errorEnum.getErrorMessage());
        this.errorEnum = errorEnum;
    }
}
