ALTER TABLE usuarios
ADD COLUMN senha_hash VARCHAR(255) NOT NULL;

CREATE TABLE usuarios_roles(
    usuario_id INT NOT NULL,
    role VARCHAR(30) NOT NULL,

    CONSTRAINT pk_usuarios
        PRIMARY KEY (usuario_id, role),

    CONSTRAINT fk_usuarios_roles_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
);