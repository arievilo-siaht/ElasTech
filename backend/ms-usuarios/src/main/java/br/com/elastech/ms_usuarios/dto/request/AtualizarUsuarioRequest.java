package br.com.elastech.ms_usuarios.dto.request;

public record AtualizarUsuarioRequest(
        String nome,
        String telefone
) {
}