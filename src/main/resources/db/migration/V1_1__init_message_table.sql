create table if not exists t_message
(
    id bigserial primary key,
    c_room_id bigint not null,
    c_user_id bigint not null,
    c_payload varchar not null check (length(trim(c_payload)) > 1 ),
    c_send_at timestamp not null
);