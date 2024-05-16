create or replace function message_event()
returns trigger as
$$
begin
    if TG_OP = 'INSERT' then
        perform pg_notify('message_queue', new.id::text);
    elsif TG_OP = 'UPDATE' then
        perform pg_notify('message_queue', new.id::text);
    elseif TG_OP = 'DELETE' then
        perform pg_notify('message_queue', old.id::text);
        return old;
    end if;
  return new;
end;
$$
language plpgsql;

create trigger message_insert_update_event_trg after insert or update on t_message for each row execute function message_event();
create trigger message_delete_event_trg before delete on t_message for each row execute function message_event();