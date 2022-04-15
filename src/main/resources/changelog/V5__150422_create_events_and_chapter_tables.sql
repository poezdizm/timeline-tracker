create table events (
    id serial primary key,
    title varchar not null,
    image_url varchar,
    start_date timestamp,
    end_date timestamp
);

create table chapters (
    id serial primary key,
    title varchar not null
);
