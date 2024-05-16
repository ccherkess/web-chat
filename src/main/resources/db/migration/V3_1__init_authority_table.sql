create table t_authority
(
    id bigserial primary key,
    c_authority varchar not null check (length(trim(c_authority)) > 0) unique
);

insert  into t_authority (c_authority) values ('ROLE_ADMIN');