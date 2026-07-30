package br.com.elastech.ms_usuarios.entities;

import br.com.elastech.ms_usuarios.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "username")
    private String username;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "email")
    private String email;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Column(name = "ativo")
    private Boolean ativo;

    @Column(name = "senha_hash")
    private String senhaHash;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "usuarios_roles",
            joinColumns = @JoinColumn(name = "usuario_id")
    )
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();
}