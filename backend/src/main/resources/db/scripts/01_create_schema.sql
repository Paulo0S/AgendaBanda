IF DB_ID('AgendaBanda') IS NULL
BEGIN
    CREATE DATABASE AgendaBanda;
END
GO

USE AgendaBanda;
GO

DROP TABLE IF EXISTS ensaio_musicos;
DROP TABLE IF EXISTS shows;
DROP TABLE IF EXISTS reunioes;
DROP TABLE IF EXISTS ensaios;
DROP TABLE IF EXISTS agenda;
DROP TABLE IF EXISTS produtores;
DROP TABLE IF EXISTS musicos;
DROP TABLE IF EXISTS locais;
DROP TABLE IF EXISTS cidades;
DROP TABLE IF EXISTS membros;
GO

CREATE TABLE membros (
    codigo INT IDENTITY(1,1) PRIMARY KEY,
    nome NVARCHAR(100) NOT NULL,
    cpf NVARCHAR(14) NOT NULL UNIQUE,
    email NVARCHAR(120) NOT NULL UNIQUE,
    login NVARCHAR(50) NOT NULL UNIQUE,
    senha NVARCHAR(255) NOT NULL,
    administrador BIT NOT NULL DEFAULT 0,
    ativo BIT NOT NULL DEFAULT 1,
    criado_em DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);
GO

CREATE TABLE musicos (
    codigo INT PRIMARY KEY,
    instrumento NVARCHAR(80) NOT NULL,
    biografia NVARCHAR(500) NULL,
    CONSTRAINT fk_musicos_membros FOREIGN KEY (codigo) REFERENCES membros(codigo)
);
GO

CREATE TABLE produtores (
    codigo INT PRIMARY KEY,
    especialidade NVARCHAR(100) NOT NULL,
    CONSTRAINT fk_produtores_membros FOREIGN KEY (codigo) REFERENCES membros(codigo)
);
GO

CREATE TABLE cidades (
    codigo_cidade INT IDENTITY(1,1) PRIMARY KEY,
    nome NVARCHAR(100) NOT NULL,
    uf CHAR(2) NOT NULL
);
GO

CREATE TABLE locais (
    codigo_local INT IDENTITY(1,1) PRIMARY KEY,
    nome NVARCHAR(120) NOT NULL,
    endereco NVARCHAR(180) NOT NULL,
    complemento NVARCHAR(120) NULL,
    codigo_cidade INT NOT NULL,
    CONSTRAINT fk_locais_cidades FOREIGN KEY (codigo_cidade) REFERENCES cidades(codigo_cidade)
);
GO

CREATE TABLE agenda (
    codigo_agenda INT IDENTITY(1,1) PRIMARY KEY,
    titulo NVARCHAR(120) NOT NULL,
    descricao NVARCHAR(800) NULL,
    dt_hora_in DATETIME2 NOT NULL,
    dt_hora_fim DATETIME2 NOT NULL,
    codigo_local INT NOT NULL,
    cod_membro_criador INT NOT NULL,
    criado_em DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    atualizado_em DATETIME2 NULL,
    CONSTRAINT fk_agenda_local FOREIGN KEY (codigo_local) REFERENCES locais(codigo_local),
    CONSTRAINT fk_agenda_membro_criador FOREIGN KEY (cod_membro_criador) REFERENCES membros(codigo),
    CONSTRAINT ck_agenda_periodo CHECK (dt_hora_fim > dt_hora_in)
);
GO

CREATE TABLE ensaios (
    codigo_agenda INT PRIMARY KEY,
    repertorio NVARCHAR(800) NOT NULL,
    observacao NVARCHAR(500) NULL,
    CONSTRAINT fk_ensaios_agenda FOREIGN KEY (codigo_agenda) REFERENCES agenda(codigo_agenda) ON DELETE CASCADE
);
GO

CREATE TABLE reunioes (
    codigo_agenda INT PRIMARY KEY,
    pauta NVARCHAR(800) NOT NULL,
    CONSTRAINT fk_reunioes_agenda FOREIGN KEY (codigo_agenda) REFERENCES agenda(codigo_agenda) ON DELETE CASCADE
);
GO

CREATE TABLE shows (
    codigo_agenda INT PRIMARY KEY,
    codigo_cidade INT NOT NULL,
    CONSTRAINT fk_shows_agenda FOREIGN KEY (codigo_agenda) REFERENCES agenda(codigo_agenda) ON DELETE CASCADE,
    CONSTRAINT fk_shows_cidade FOREIGN KEY (codigo_cidade) REFERENCES cidades(codigo_cidade)
);
GO

CREATE TABLE ensaio_musicos (
    codigo_agenda INT NOT NULL,
    cod_musico INT NOT NULL,
    PRIMARY KEY (codigo_agenda, cod_musico),
    CONSTRAINT fk_ensaio_musicos_ensaio FOREIGN KEY (codigo_agenda) REFERENCES ensaios(codigo_agenda) ON DELETE CASCADE,
    CONSTRAINT fk_ensaio_musicos_musico FOREIGN KEY (cod_musico) REFERENCES musicos(codigo)
);
GO
