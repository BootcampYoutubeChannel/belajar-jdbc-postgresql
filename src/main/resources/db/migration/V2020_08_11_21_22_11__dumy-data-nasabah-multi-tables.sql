insert into bank.nasabah_perorangan(cif, nama, ktp, foto, created_date, created_by)
values ('001', 'Dimas Maryanto', '62641234', null, now(), 'migration');

insert into bank.nasabah_badan_usaha(cif, nama, npwp, siup, created_date, created_by)
VALUES ('002', 'PT. Multipolar', '1234', '2343434', now(), 'migration');
