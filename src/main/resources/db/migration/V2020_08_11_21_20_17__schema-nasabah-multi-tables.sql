create table bank.nasabah_perorangan
(
    cif               character varying(64)  not null primary key default uuid_generate_v4(),
    nama              character varying(100) not null,
    ktp               character varying(64),
    foto              character varying(255),
    created_date      timestamp              not null             default now(),
    created_by        character varying(64)  not null,
    last_updated_date timestamp,
    last_updated_by   character varying(64)
);

create table bank.nasabah_badan_usaha
(
    cif               character varying(64)  not null primary key default uuid_generate_v4(),
    nama              character varying(100) not null,
    npwp              character varying(24),
    siup              character varying(60),
    created_date      timestamp              not null             default now(),
    created_by        character varying(64)  not null,
    last_updated_date timestamp,
    last_updated_by   character varying(64)
);
