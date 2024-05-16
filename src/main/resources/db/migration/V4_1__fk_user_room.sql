create table t_user_room
(
    id bigserial primary key,
    c_user_id bigint not null references t_user(id) on delete cascade,
    c_room_id bigint not null references t_room(id) on delete cascade,
    constraint uk_user_room unique (c_user_id, c_room_id)
);