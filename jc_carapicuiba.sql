create database jc_carapicuiba;

use jc_carapicuiba;

create table dedicacao (
	cod_dedicacao INT PRIMARY KEY AUTO_INCREMENT,
    dt_dedicacao date not null,
    qtde_membros int not null,
    qtde_frequentadores int not null,
    qtde_primvez int not null
);

create table pedidos(
	cod_pedidos int primary key auto_increment,
    dt_pedidos date not null,
    qtde_agradecimento int not null,
    qtde_graca int not null,
    qtde_elevacao int not null,
    qtde_anivfalec int not null
);

CREATE TABLE membro (
    cod_membro INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    endereco varchar (100) NOT NULL,
    telefone VARCHAR(15),
    email VARCHAR(100),
    cpf VARCHAR(14) UNIQUE,
    data_outorga DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE primvez (
    cod_primvez INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cod_membro INT NOT NULL,
    endereco varchar (100) NOT NULL,
    telefone VARCHAR(15),
    email VARCHAR(100),
    data_primvez DATETIME DEFAULT CURRENT_TIMESTAMP,
    foreign key (cod_membro) references membro (cod_membro)
);

CREATE TABLE gratidao (
    cod_gratidao INT PRIMARY KEY AUTO_INCREMENT,
    cod_membro INT NOT NULL,
    dt_gratidao datetime default current_timestamp,
    vl_gratidao decimal (10,2) not null,
    tipo_gratidao varchar (20) not null,
    foreign key (cod_membro) references membro (cod_membro)
);