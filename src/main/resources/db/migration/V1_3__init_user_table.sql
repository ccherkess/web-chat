create table if not exists t_user
(
    id bigserial primary key,
    c_username varchar not null check (length(trim(c_username)) > 2) unique,
    c_password varchar
);

insert into t_user (c_username, c_password) values ('admin', '{bcrypt}$2a$10$HVCQP5S7Utu4ZkeoBHPnWOB/7EC8ZCnzzSq4i8GHsbwIsOGTrk0Si');