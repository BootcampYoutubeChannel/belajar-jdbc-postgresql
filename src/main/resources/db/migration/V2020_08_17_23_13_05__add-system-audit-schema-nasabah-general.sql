create table audit.nasabah_general_aud
(
    cif               character varying(64),
    nama              character varying(100),
    npwp              character varying(24),
    siup              character varying(60),
    ktp               character varying(64),
    foto              character varying(255),
    type              int,
    created_date      timestamp,
    created_by        character varying(64),
    last_updated_date timestamp,
    last_updated_by   character varying(64),
    version_id        character varying(64)
        constraint fk_audit_nasabah_general references audit.system_audit (version_id) on update cascade on delete cascade,
    constraint pk_nasabah_general_audit primary key (version_id, cif)
);

create or replace procedure audit.nasabah_general_aud_trigger(app_mode character varying(10),
                                                              app_created_by character varying(100),
                                                              app_cif character varying(64))
    language plpgsql
as
$$
declare
    system_aud_version_id audit.system_audit.version_id%type;
    old_cif               bank.nasabah.cif%type;
    old_nama              bank.nasabah.nama%type;
    old_npwp              bank.nasabah.npwp%type;
    old_siup              bank.nasabah.siup%type;
    old_ktp               bank.nasabah.ktp%type;
    old_foto              bank.nasabah.foto%type;
    old_type              bank.nasabah.type%type;
    old_created_date      bank.nasabah.created_date%type;
    old_created_by        bank.nasabah.created_by%type;
    old_last_updated_date bank.nasabah.last_updated_date%type;
    old_last_updated_by   bank.nasabah.last_updated_by%type;
begin
    insert into audit.system_audit(version_mode, version_created_date, version_created_by)
    VALUES (app_mode, now(), app_created_by)
    returning version_id into system_aud_version_id;

    select cif,
           nama,
           npwp,
           siup,
           ktp,
           foto,
           type,
           created_date,
           created_by,
           last_updated_date,
           last_updated_by
    into
        old_cif,
        old_nama,
        old_npwp,
        old_siup,
        old_ktp,
        old_foto,
        old_type,
        old_created_date,
        old_created_by,
        old_last_updated_date,
        old_last_updated_by
    from bank.nasabah
    where cif = app_cif;

    insert into audit.nasabah_general_aud(cif, nama, npwp, siup, ktp, foto, type, created_date, created_by,
                                          last_updated_date, last_updated_by, version_id)
    values (old_cif, old_nama, old_npwp, old_siup, old_ktp, old_foto, old_type, old_created_date, old_created_by,
            old_last_updated_date, old_last_updated_by, system_aud_version_id);

end;
$$
