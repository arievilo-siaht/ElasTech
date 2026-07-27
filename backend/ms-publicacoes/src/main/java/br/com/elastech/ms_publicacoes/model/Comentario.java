package br.com.elastech.ms_publicacoes.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@Entity(name = "comentarios")
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacao_id", nullable = false)
    private Publicacao publicacao;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(nullable = false)
    private String conteudo;

    @CreationTimestamp
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
}
