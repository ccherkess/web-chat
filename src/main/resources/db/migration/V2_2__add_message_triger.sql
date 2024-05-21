create or replace function message_event()
returns trigger as
$$
begin
    if TG_OP = 'INSERT' then
       perform (
            with payload("eventType", "message") as (select 'CREATE', new)
            select pg_notify('message_queue', row_to_json(payload)::TEXT) from payload
       );
    elseif TG_OP = 'UPDATE' then
        perform (
            with payload("eventType", "message") as (select 'UPDATE', new)
            select pg_notify('message_queue', row_to_json(payload)::TEXT) from payload
       );
    elseif TG_OP = 'DELETE' then
        perform (
            with payload("eventType", "message") as (select 'DELETE', old)
            select pg_notify('message_queue', row_to_json(payload)::TEXT) from payload
       );

       return old;
    end if;

    return new;
end;
$$
language plpgsql;

create trigger message_insert_event_trg after insert on t_message for each row execute function message_event();
create trigger message_update_event_trg after update on t_message for each row execute function message_event();
create trigger message_delete_event_trg before delete on t_message for each row execute function message_event();