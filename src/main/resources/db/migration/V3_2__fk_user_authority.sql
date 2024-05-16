create table t_user_authority
(
    id bigserial primary key,
    c_user_id bigint not null references t_user(id) on delete cascade,
    c_authority_id bigint not null references t_authority(id) on delete cascade,
    constraint uk_user_authority unique (c_user_id, c_authority_id)
);