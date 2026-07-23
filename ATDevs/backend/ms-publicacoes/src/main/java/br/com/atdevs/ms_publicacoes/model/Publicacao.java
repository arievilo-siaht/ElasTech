package br.com.atdevs.ms_publicacoes.model;

import br.com.atdevs.ms_publicacoes.enums.StatusPublicacao;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "publicacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Publicacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(nullable = false, length = 1000)
    private String conteudo;

    private String imagem;

    @CreationTimestamp
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Enumerated(EnumType.STRING)
    private StatusPublicacao status;

    @OneToMany(
            mappedBy = "publicacao",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comentario> comentarios = new ArrayList<>();

    @OneToMany(
            mappedBy = "publicacao",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Curtida> curtidas = new ArrayList<>();
}