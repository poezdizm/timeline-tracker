create table relation_types (
    id serial primary key,
    label varchar not null unique,
    color varchar(7) not null
);

alter table character_relations add column type_id integer default null references relation_types(id) ON DELETE RESTRICT;
