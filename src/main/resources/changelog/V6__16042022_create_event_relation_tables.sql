create table events_characters (
    id           bigserial primary key,
    character_id int not null references characters(id) on delete cascade,
    event_id     int not null references events(id) on delete cascade,
    unique (character_id, event_id)
);

create table events_chapters (
    id         bigserial primary key,
    event_id   int not null references events(id) on delete cascade,
    chapter_id int not null references chapters(id) on delete cascade,
    unique (event_id, chapter_id)
);
