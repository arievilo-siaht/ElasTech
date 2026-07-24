package br.com.elastech.ms_publicacoes.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"publicacao_id", "usuario_id"})
        }
)
public class Curtida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacao_id", nullable = false)
    private Publicacao publicacao;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @CreationTimestamp
    @Column(name = "data_curtida")
    private LocalDateTime dataCurtida;
}
