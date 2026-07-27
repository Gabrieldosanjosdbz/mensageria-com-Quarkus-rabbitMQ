create table if not exists audit(
    id serial primary key,
    cnpj text not null,
    status text not null,
    dateTime timestamp default now()
);