package br.com.elastech.ms_usuarios.dto.internal;

import br.com.elastech.ms_usuarios.enums.Role;
import java.util.Set;

public record UsuarioAuthResponse(
        Integer id,
        String username,
        Boolean ativo,
        String senhaHash,
        Set<Role> roles
) {
}