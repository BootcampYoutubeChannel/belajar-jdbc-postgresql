create table audit.system_audit
(
    version_id           character varying(64) not null primary key default uuid_generate_v4(),
    version_mode         character varying(10) not null             default 'INSERT',
    version_created_date timestamp             not null             default now()
);

create table audit.nasabah_perorangan_aud
(
    version_id        character varying(64)
        constraint fk_audit_nasabah_perorangan references audit.system_audit (version_id) on update cascade on delete cascade,
    cif               character varying(64),
    nama              character varying(100),
    ktp               character varying(64),
    foto              character varying(255),
    saldo             decimal,
    created_date      timestamp,
    created_by        character varying(64),
    last_updated_date timestamp,
    last_updated_by   character varying(64),
    constraint pk_composite_nasabah_perorangan primary key (version_id, cif)
);

create table audit.nasabah_badan_usaha_aud
(
    version_id        character varying(64)
        constraint fk_audit_nasabah_perorangan
            references audit.system_audit (version_id)
            on update cascade on delete cascade,
    cif               character varying(64),
    nama              character varying(100),
    npwp              character varying(24),
    siup              character varying(60),
    saldo             decimal,
    created_date      timestamp,
    created_by        character varying(64),
    last_updated_date timestamp,
    last_updated_by   character varying(64),
    constraint pk_composite_nasabah_badan_usaha primary key (version_id, cif)
)
