package br.com.elastech.ms_chat.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "conversas", uniqueConstraints = {@UniqueConstraint(columnNames = {"usuario1_id", "usuario2_id"})})

public class Conversa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario1_id")
    private Integer usuario1Id;

    @Column(name = "usuario2_id")
    private Integer usuario2Id;

    @CreationTimestamp
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "ativa")
    private boolean ativa = true;

}
