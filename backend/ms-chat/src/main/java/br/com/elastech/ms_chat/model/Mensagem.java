package br.com.elastech.ms_chat.model;

import br.com.elastech.ms_chat.enums.StatusMensagem;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mensagens")
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversa_id", nullable = false)
    private Conversa conversa;

    @Column(name = "remetente_id", nullable = false)
    private Integer remetenteId;

    @Column(name = "destinatario_id", nullable = false)
    private Integer destinatarioId;

    @Column(nullable = false, length = 1000)
    private String conteudo;

    @CreationTimestamp
    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    @Enumerated(EnumType.STRING)
    private StatusMensagem statusMensagem;

}
