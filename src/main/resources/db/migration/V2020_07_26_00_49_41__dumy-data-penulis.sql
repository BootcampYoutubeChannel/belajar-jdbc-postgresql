insert into perpustakaan.penulis(id, nama, alamat)
values ('001', 'Dimas Maryanto', 'Bandung'),
       ('002', 'Hari Sapto Adi', 'Jakarta'),
       ('003', 'Deni Sutisna', 'Jakarta');

insert into perpustakaan.penulis_buku(buku_id, penulis_id)
values ('001', '001'),
       ('001', '002'),
       ('002', '003');

