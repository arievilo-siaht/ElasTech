package br.com.elastech.ms_usuarios.dto.request;

import java.time.LocalDate;

public record CriarUsuarioRequest(
        String nome,
        String username,
        String telefone,
        LocalDate dataNascimento,
        String email,
        LocalDate dataCadastro
) {
}