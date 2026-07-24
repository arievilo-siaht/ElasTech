package br.com.atdevs.ms_usuarios.dto.request;

public record AtualizarUsuarioRequest(
        String nome,
        String telefone
) {
}