create table if not exists agencia(
    id serial primary key,
    nome text not null,
    razao_social text not null,
    cnpj text not null,
    situacao_cadastral text not null
);

insert into agencia (nome, razao_social, cnpj, situacao_cadastral)
    values ('Agencia BSB', 'Asa Norte AGENCIA BSB', '15130254000100', 'ATIVO');