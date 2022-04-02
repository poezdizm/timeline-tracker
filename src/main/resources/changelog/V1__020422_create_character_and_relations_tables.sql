create table characters (
    id serial primary key,
    name varchar not null,
    image_url varchar,
    main boolean not null default true,
    dead boolean not null default false
);

create table character_relations (
    id bigserial primary key,
    from_char int not null references characters(id) on delete cascade,
    to_char int not null references characters(id) on delete cascade,
    unique(from_char, to_char)
);