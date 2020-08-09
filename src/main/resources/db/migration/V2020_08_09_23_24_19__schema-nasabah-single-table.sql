create table bank.nasabah
(
    cif               character varying(64)  not null primary key default uuid_generate_v4(),
    nama              character varying(100) not null,
    npwp              character varying(24),
    siup              character varying(60),
    ktp               character varying(64),
    foto              character varying(255),
    type              int                    not null             default 0,
    created_date      timestamp              not null             default now(),
    created_by        character varying(64)  not null,
    last_updated_date timestamp,
    last_updated_by   character varying(64)
);
