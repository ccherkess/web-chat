create table if not exists t_user
(
    id bigserial primary key,
    c_username varchar not null check (length(trim(c_username)) > 2) unique,
    c_password varchar
);