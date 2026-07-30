package br.com.elastech.ms_auth.service;

import br.com.elastech.ms_auth.client.UsuarioAuthClient;
import br.com.elastech.ms_auth.security.AuthUserDetails;
import br.com.elastech.ms_usuarios.dto.internal.UsuarioAuthResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final UsuarioAuthClient usuarioAuthClient;

    public AuthUserDetailsService(UsuarioAuthClient usuarioAuthClient) {
        this.usuarioAuthClient = usuarioAuthClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioAuthResponse usuario = usuarioAuthClient.findByUsername(username);

        System.out.println(usuario);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado.");
        }

        if (!usuario.ativo()) {
            throw new UsernameNotFoundException("Usuário inativo.");
        }

        List<SimpleGrantedAuthority> authorities =
                usuario.roles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .toList();

        return new AuthUserDetails(
                usuario.id(),
                usuario.username(),
                usuario.senhaHash(),
                authorities
        );
    }
}
