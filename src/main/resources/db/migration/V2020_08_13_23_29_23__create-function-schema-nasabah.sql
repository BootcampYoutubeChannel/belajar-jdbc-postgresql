create or replace function bank.findByIdNasabah(nasabah_id character varying(64))
    returns table
            (
                nasabah_cif               character varying(64),
                nasabah_nama              character varying(100),
                nasabah_npwp              character varying(24),
                nasabah_siup              character varying(60),
                nasabah_ktp               character varying(64),
                nasabah_foto              character varying(255),
                nasabah_type              int,
                nasabah_created_date      timestamp,
                nasabah_created_by        character varying(64),
                nasabah_last_updated_date timestamp,
                nasabah_last_updated_by   character varying(64)
            )
AS
$$
DECLARE
    data record;
BEGIN

    for data in (
        select perorangan.cif               as cif,
               perorangan.nama              as nama,
               null                         as npwp,
               null                         as siup,
               perorangan.ktp               as ktp,
               perorangan.foto              as foto,
               1                            as type_nasabah,
               perorangan.created_date      as created_date,
               perorangan.created_by        as created_by,
               perorangan.last_updated_date as last_updated_date,
               perorangan.last_updated_by   as last_updated_by
        from bank.nasabah_perorangan perorangan
        where perorangan.cif = nasabah_id
        union
        select badan_usaha.cif               as cif,
               badan_usaha.nama              as nama,
               badan_usaha.npwp              as npwp,
               badan_usaha.siup              as siup,
               null                          as ktp,
               null                          as foto,
               2                             as type_nasabah,
               badan_usaha.created_date      as created_date,
               badan_usaha.created_by        as created_by,
               badan_usaha.last_updated_date as last_updated_date,
               badan_usaha.last_updated_by   as last_updated_by
        from bank.nasabah_badan_usaha badan_usaha
        where badan_usaha.cif = nasabah_id)
        LOOP
            nasabah_cif := data.cif;
            nasabah_nama := data.nama;
            nasabah_npwp := data.npwp;
            nasabah_siup := data.siup;
            nasabah_ktp := data.ktp;
            nasabah_foto := data.foto;
            nasabah_type := data.type_nasabah;
            nasabah_created_by := data.created_by;
            nasabah_created_date := data.created_date;
            nasabah_last_updated_by := data.last_updated_by;
            nasabah_last_updated_date := data.last_updated_date;
            RETURN NEXT;
        END LOOP;
END;
$$
    LANGUAGE 'plpgsql';
