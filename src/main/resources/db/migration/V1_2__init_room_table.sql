create table if not exists t_room
(
    id bigserial primary key,
    c_name varchar not null check (length(trim(c_name)) > 1)
);