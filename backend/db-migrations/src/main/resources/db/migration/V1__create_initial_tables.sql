CREATE TABLE IF NOT EXISTS usuarios (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    username VARCHAR(100) NOT NULL,
    telefone VARCHAR(20),
    data_nascimento DATE,
    email VARCHAR(255) NOT NULL,
    data_cadastro DATE NOT NULL,
    ativo BOOLEAN NOT NULL,

    CONSTRAINT pk_usuarios PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_username UNIQUE (username),
    CONSTRAINT uk_usuarios_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS publicacoes (
    id INT NOT NULL AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    conteudo VARCHAR(1000) NOT NULL,
    imagem VARCHAR(255),
    data_criacao DATETIME,
    data_atualizacao DATETIME,
    status VARCHAR(50),

    CONSTRAINT pk_publicacoes PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comentarios (
    id INT NOT NULL AUTO_INCREMENT,
    publicacao_id INT NOT NULL,
    usuario_id INT,
    conteudo VARCHAR(255) NOT NULL,
    data_criacao DATETIME,

    CONSTRAINT pk_comentarios PRIMARY KEY (id),
    CONSTRAINT fk_comentarios_publicacao
        FOREIGN KEY (publicacao_id)
        REFERENCES publicacoes(id)
);

CREATE TABLE IF NOT EXISTS curtidas (
    id INT NOT NULL AUTO_INCREMENT,
    publicacao_id INT NOT NULL,
    usuario_id INT NOT NULL,
    data_curtida DATETIME,

    CONSTRAINT pk_curtidas PRIMARY KEY (id),

    CONSTRAINT fk_curtidas_publicacao
        FOREIGN KEY (publicacao_id)
        REFERENCES publicacoes(id),

    CONSTRAINT uk_curtidas_publicacao_usuario
        UNIQUE (publicacao_id, usuario_id)
);