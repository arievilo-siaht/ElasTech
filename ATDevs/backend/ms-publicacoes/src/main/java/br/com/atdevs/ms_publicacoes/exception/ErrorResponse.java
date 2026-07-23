package br.com.atdevs.ms_publicacoes.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp;
    private String errorCode;
    private String message;
    private int status;
    private String path;

}