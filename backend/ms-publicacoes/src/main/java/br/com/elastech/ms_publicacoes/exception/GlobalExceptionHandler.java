package br.com.elastech.ms_publicacoes.exception;

import br.com.elastech.ms_publicacoes.enums.ErrorEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(
            BaseException ex,
            HttpServletRequest request) {

        ErrorEnum error = ex.getErrorEnum();

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                error.getErrorCode(),
                error.getErrorMessage(),
                error.getHttpStatus().value(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(error.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String mensagem = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                ErrorEnum.CONTEUDO_INVALIDO.getErrorCode(),
                mensagem,
                ErrorEnum.CONTEUDO_INVALIDO.getHttpStatus().value(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(ErrorEnum.CONTEUDO_INVALIDO.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            HttpServletRequest request) {

        ex.printStackTrace();

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                ErrorEnum.ERRO_INTERNO.getErrorCode(),
                ErrorEnum.ERRO_INTERNO.getErrorMessage(),
                ErrorEnum.ERRO_INTERNO.getHttpStatus().value(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(ErrorEnum.ERRO_INTERNO.getHttpStatus())
                .body(response);
    }
}