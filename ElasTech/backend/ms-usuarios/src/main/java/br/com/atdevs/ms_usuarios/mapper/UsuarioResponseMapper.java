package br.com.atdevs.ms_usuarios.mapper;

import br.com.atdevs.ms_usuarios.dto.response.UsuarioResponse;
import br.com.atdevs.ms_usuarios.entities.Usuario;

public class UsuarioResponseMapper {
    public UsuarioResponse toResponse(Usuario usuario) {
        UsuarioResponse usuarioResponse =
                new UsuarioResponse(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getUsername(),
                        usuario.getTelefone(),
                        usuario.getDataNascimento(),
                        usuario.getEmail(),
                        usuario.getDataCadastro(),
                        usuario.getAtivo()
                );

        return usuarioResponse;
    }
}