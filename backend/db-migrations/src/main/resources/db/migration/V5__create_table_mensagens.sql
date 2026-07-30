CREATE TABLE mensagens
(
    id INT NOT NULL AUTO_INCREMENT,
    conversa_id INT NOT NULL,
    remetente_id INT NOT NULL,
    destinatario_id INT NOT NULL,
    conteudo VARCHAR(1000) NOT NULL,
    data_envio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status_mensagem VARCHAR(50) NOT NULL,

    CONSTRAINT pk_mensagens PRIMARY KEY (id),

    CONSTRAINT fk_mensagens_conversa
        FOREIGN KEY (conversa_id)
            REFERENCES conversas (id)
            ON DELETE CASCADE,

    INDEX idx_mensagens_conversa (conversa_id),
    INDEX idx_mensagens_data (data_envio)
);