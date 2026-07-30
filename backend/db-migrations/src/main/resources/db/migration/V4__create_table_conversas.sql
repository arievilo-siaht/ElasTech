CREATE TABLE conversas
(
    id INT NOT NULL AUTO_INCREMENT,
    usuario1_id INT NOT NULL,
    usuario2_id INT NOT NULL,
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ativa BOOLEAN  NOT NULL DEFAULT TRUE,

    CONSTRAINT pk_conversas PRIMARY KEY (id),
    CONSTRAINT uk_conversas_usuarios UNIQUE (usuario1_id, usuario2_id)
);