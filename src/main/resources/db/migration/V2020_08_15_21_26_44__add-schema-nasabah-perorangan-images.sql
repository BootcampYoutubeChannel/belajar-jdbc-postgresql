create table bank.nasabah_perorangan_image
(
    id           character varying(64)  not null primary key default uuid_generate_v4(),
    filename     character varying(100) not null,
    file         bytea                  not null,
    nasabah_id   character varying(64)  not null
        constraint fk_nasabah_perorangan_id
            references bank.nasabah_perorangan (cif)
            on update cascade on delete cascade,
    created_by   character varying(100) not null,
    created_date timestamp              not null             default now()
);
