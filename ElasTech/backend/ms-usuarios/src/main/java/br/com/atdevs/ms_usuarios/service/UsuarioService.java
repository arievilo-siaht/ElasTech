package br.com.atdevs.ms_usuarios.service;

import br.com.atdevs.ms_usuarios.dto.request.AtualizarUsuarioRequest;
import br.com.atdevs.ms_usuarios.dto.request.CriarUsuarioRequest;
import br.com.atdevs.ms_usuarios.entities.Usuario;
import br.com.atdevs.ms_usuarios.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Usuario create(CriarUsuarioRequest criarUsuarioRequest) {
        Usuario usuario = Usuario
                .builder()
                .nome(criarUsuarioRequest.nome())
                .username(criarUsuarioRequest.username())
                .telefone(criarUsuarioRequest.telefone())
                .dataNascimento(criarUsuarioRequest.dataNascimento())
                .email(criarUsuarioRequest.email())
                .dataCadastro(LocalDate.now())
                .ativo(true)
                .build();

        usuarioRepository.save(usuario);

        return usuario;
    }

    public Usuario read(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario inexistente."));

        return usuario;
    }

    public List<Usuario> readAll() {
        List<Usuario> listaUsuarios = usuarioRepository.findAll();

        return listaUsuarios;
    }

    @Transactional
    public void update(AtualizarUsuarioRequest atualizarUsuarioRequest, Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario inexistente."));

        if(atualizarUsuarioRequest.nome() != null) {
            usuario.setNome(atualizarUsuarioRequest.nome());
        }

        if(atualizarUsuarioRequest.telefone() != null) {
            usuario.setTelefone(atualizarUsuarioRequest.telefone());
        }

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void delete(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario inexistente."));

        usuario.setAtivo(false);

        usuarioRepository.save(usuario);
    }
}