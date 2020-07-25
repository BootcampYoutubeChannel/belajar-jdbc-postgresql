create table perpustakaan.penulis
(
    id     character varying(64) not null primary key default uuid_generate_v4(),
    nama   character varying(64) not null,
    alamat text
);

create table perpustakaan.penulis_buku
(
    id         character varying(64) not null primary key default uuid_generate_v4(),
    buku_id    character varying(64) not null,
    penulis_id character varying(64) not null
);

alter table perpustakaan.penulis_buku
    add constraint fk_penulis_buku_id foreign key (buku_id)
        references perpustakaan.buku (id) on update cascade on delete cascade;

alter table perpustakaan.penulis_buku
    add constraint fk_penulis_buku_penulis_id foreign key (penulis_id)
        references perpustakaan.penulis (id) on update cascade on delete cascade;
