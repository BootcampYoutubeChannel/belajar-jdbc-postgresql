create or replace function audit.nasabah_perorangan_fun_aud()
    returns trigger as
$$
declare
    system_aud_version_id audit.system_audit.version_id%type;
begin

    insert into audit.system_audit(version_mode, version_created_date)
    VALUES (tg_op, now())
    returning version_id into system_aud_version_id;

    insert into audit.nasabah_perorangan_aud (version_id, cif, nama, ktp, foto, saldo, created_date, created_by,
                                              last_updated_date, last_updated_by)
    select system_aud_version_id,
           old.cif,
           old.nama,
           old.ktp,
           old.foto,
           old.saldo,
           old.created_date,
           old.created_by,
           old.last_updated_date,
           old.last_updated_by;
    return old;
end;
$$
    language plpgsql;

create trigger nasabah_perorangan_trigger
    after update or delete
    on bank.nasabah_perorangan
    for each row
execute procedure audit.nasabah_perorangan_fun_aud();

create or replace function audit.nasabah_badan_usaha_fun_aud()
    returns trigger as
$$
declare
    system_aud_version_id audit.system_audit.version_id%type;
begin

    insert into audit.system_audit(version_mode, version_created_date)
    VALUES (tg_op, now())
    returning version_id into system_aud_version_id;

    insert into audit.nasabah_badan_usaha_aud (version_id, cif, nama, npwp, siup, saldo, created_date, created_by,
                                               last_updated_date, last_updated_by)
    select system_aud_version_id,
           old.cif,
           old.nama,
           old.npwp,
           old.siup,
           old.saldo,
           old.created_date,
           old.created_by,
           old.last_updated_date,
           old.last_updated_by;
    return old;
end;
$$
    language plpgsql;

create trigger nasabah_badan_usaha_trigger
    after update or delete
    on bank.nasabah_badan_usaha
    for each row
execute procedure audit.nasabah_badan_usaha_fun_aud();

