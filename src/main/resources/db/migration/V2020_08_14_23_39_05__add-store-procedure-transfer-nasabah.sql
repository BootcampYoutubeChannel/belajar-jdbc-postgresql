create or replace procedure bank.trf_perorangan_ke_badan_usaha(cif_perorangan bank.nasabah_perorangan.cif%TYPE,
                                                               cif_badan_usaha bank.nasabah_badan_usaha.cif%TYPE,
                                                               saldo_transfer bank.nasabah_perorangan.saldo%type)
    language plpgsql
as
$$
declare
    saldo_nasabah_peorangan_current   bank.nasabah_perorangan.saldo%type := 0;
    saldo_nasabah_badan_usaha_current bank.nasabah_perorangan.saldo%type := 0;
begin

    update bank.nasabah_perorangan set saldo = saldo - saldo_transfer where cif = cif_perorangan;
    select saldo
    into saldo_nasabah_peorangan_current
    from bank.nasabah_perorangan
    where cif = cif_perorangan;
    raise info 'Saldo nasabah perorangan berkurang sebesar % menjadi % ',
        saldo_transfer,
        saldo_nasabah_peorangan_current;

    update bank.nasabah_badan_usaha set saldo = saldo + saldo_transfer where cif = cif_badan_usaha;
    select saldo
    into saldo_nasabah_badan_usaha_current
    from bank.nasabah_badan_usaha
    where cif = cif_badan_usaha;
    raise info 'Saldo nasabah badan usaha bertambah sebesar % menjadi % ',
        saldo_transfer,
        saldo_nasabah_badan_usaha_current;
end;
$$
