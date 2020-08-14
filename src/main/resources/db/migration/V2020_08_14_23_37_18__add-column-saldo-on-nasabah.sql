alter table bank.nasabah_perorangan
    add column saldo decimal not null default 0;

alter table bank.nasabah_badan_usaha
    add column saldo decimal not null default 0;
