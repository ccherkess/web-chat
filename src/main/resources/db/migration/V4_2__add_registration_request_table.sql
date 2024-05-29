create table if not exists t_reqistration_request
(
    id bigserial primary key,
    c_name varchar not null check (length(trim(c_name)) >= 1 ),
    c_password varchar not null check (length(trim(c_password)) >= 1 )
);