package br.com.atdevs.ms_usuarios.dto.response;

import java.time.LocalDate;

public record UsuarioResponse(
        Integer id,
        String nome,
        String username,
        String telefone,
        LocalDate dataNascimento,
        String email,
        LocalDate dataCadastro,
        Boolean ativo
) {
}