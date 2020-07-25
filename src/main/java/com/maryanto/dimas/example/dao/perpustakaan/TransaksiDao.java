package com.maryanto.dimas.example.dao.perpustakaan;

import com.maryanto.dimas.example.dao.CrudRepository;
import com.maryanto.dimas.example.entity.perpustakaan.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransaksiDao implements CrudRepository<Transaksi, String> {

    private final Connection connection;

    public TransaksiDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Transaksi save(Transaksi value) throws SQLException {
        //language=PostgreSQL
        String transactionQuery = "insert into perpustakaan.transaksi(tanggal_transaksi, anggota_id)\n" +
                "values (?, ?);";
        PreparedStatement statement = connection.prepareStatement(transactionQuery, Statement.RETURN_GENERATED_KEYS);
        statement.setDate(1, value.getCreatedDate());
        if (value.getAnggota() != null)
            statement.setString(2, value.getAnggota().getId());
        else
            statement.setNull(2, Types.VARCHAR);

        int row = statement.executeUpdate();
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next())
            value.setId(generatedKeys.getString("id"));

        //language=PostgreSQL
        String transactionDetailQUery = "insert into perpustakaan.transaksi_detail(transaksi_id, buku_id, tanggal_kembali, is_return)\n" +
                "values (?, ?, ?, false)";
        statement = connection.prepareStatement(transactionDetailQUery);
        for (TransaksiDetail detail : value.getDetails()) {
            statement.setString(1, value.getId());
            if (detail.getBuku() != null)
                statement.setString(2, detail.getBuku().getId());
            else
                statement.setNull(2, Types.VARCHAR);
            statement.setDate(3, detail.getTanggalKembali());
            statement.addBatch();
        }

        generatedKeys.close();
        statement.executeBatch();
        statement.close();
        return value;
    }

    @Override
    public Transaksi update(Transaksi value) throws SQLException {
        return null;
    }

    public TransaksiDetail update(TransaksiDetail value) throws SQLException {
        //language=PostgreSQL
        String query = "update perpustakaan.transaksi_detail\n" +
                "set is_return   = true,\n" +
                "    date_return = now()\n" +
                "where id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, value.getId());
        statement.executeUpdate();
        return value;
    }

    public Optional<TransaksiDetail> findByTransactionIdAndBookId(String transactionId, String bookId) throws SQLException {
        //language=PostgreSQL
        String query = "select trx_detail.id              as id,\n" +
                "       trx_detail.buku_id         as buku_id,\n" +
                "       trx_detail.tanggal_kembali as tanggal_kembali,\n" +
                "       trx_detail.is_return       as status_kembali,\n" +
                "       trx_detail.date_return     as last_updated_date,\n" +
                "       book.id                    as buku_id,\n" +
                "       book.nama                  as buku_name,\n" +
                "       book.isbn                  as buku_isbn,\n" +
                "       book.penerbit_id           as penerbit_id,\n" +
                "       pub.nama                   as penerbit_nama,\n" +
                "       pub.alamat                 as penerbit_alamat,\n" +
                "       trx.id                     as transactionId,\n" +
                "       trx.tanggal_transaksi      as transactionDate,\n" +
                "       trx.anggota_id             as anggotaId,\n" +
                "       " +
                "agt.nomor_ktp              as anggotaKtp,\n" +
                "       agt.nama                   as anggotaNama,\n" +
                "       agt.alamat                 as anggotaAlamat\n" +
                "from perpustakaan.transaksi_detail trx_detail\n" +
                "         left join perpustakaan.transaksi trx on trx_detail.transaksi_id = trx.id\n" +
                "         left join perpustakaan.buku book on trx_detail.buku_id = book.id\n" +
                "         left join perpustakaan.penerbit pub on book.penerbit_id = pub.id\n" +
                "         " +
                "left join perpustakaan.anggota agt on trx.anggota_id = agt.id\n" +
                "where trx_detail.transaksi_id = ?\n" +
                "  and trx_detail.buku_id = ?\n" +
                "  " +
                "and trx_detail.is_return = false";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, transactionId);
        statement.setString(2, bookId);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            statement.close();
            resultSet.close();
            return Optional.empty();
        }

        TransaksiDetail detail = new TransaksiDetail(
                resultSet.getString("id"),
                new Transaksi(
                        resultSet.getString("transactionId"),
                        resultSet.getDate("transactionDate"),
                        new Anggota(
                                resultSet.getString("anggotaId"),
                                resultSet.getString("anggotaKtp"),
                                resultSet.getString("anggotaNama"),
                                resultSet.getString("anggotaAlamat")
                        ),
                        null
                ),
                new Buku(
                        resultSet.getString("buku_id"),
                        resultSet.getString("buku_name"),
                        resultSet.getString("buku_isbn"),
                        new Penerbit(
                                resultSet.getString("penerbit_id"),
                                resultSet.getString("penerbit_nama"),
                                resultSet.getString("penerbit_alamat"),
                                new ArrayList<>()
                        ),
                        new ArrayList<>()
                ),
                resultSet.getDate("tanggal_kembali"),
                resultSet.getBoolean("status_kembali"),
                resultSet.getDate("last_updated_date")
        );
        statement.close();
        resultSet.close();
        return Optional.of(detail);
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        String query = "delete\n" +
                "from perpustakaan.transaksi\n" +
                "where id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, value);
        int row = statement.executeUpdate();
        return row >= 1;
    }

    @Override
    public Optional<Transaksi> findById(String value) throws SQLException {
        //language=PostgreSQL
        String query = "select trx.id                as id,\n" +
                "       trx.tanggal_transaksi as tanggal,\n" +
                "       agt.id                as anggota_id,\n" +
                "       agt.nomor_ktp         as anggota_ktp,\n" +
                "       agt.nama              as anggota_nama,\n" +
                "       agt.alamat            as anggota_alamat\n" +
                "from perpustakaan.transaksi trx\n" +
                "         left join perpustakaan.anggota agt on trx.anggota_id = agt.id\n" +
                "where trx.id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, value);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            statement.close();
            resultSet.close();
            return Optional.empty();
        }

        Transaksi transaksi = new Transaksi(
                resultSet.getString("id"),
                resultSet.getDate("tanggal"),
                new Anggota(
                        resultSet.getString("anggota_id"),
                        resultSet.getString("anggota_ktp"),
                        resultSet.getString("anggota_nama"),
                        resultSet.getString("anggota_alamat")
                ),
                this.findByTransactionId(resultSet.getString("id"))
        );

        return Optional.of(transaksi);
    }

    public List<TransaksiDetail> findByTransactionId(String value) throws SQLException {
        List<TransaksiDetail> list = new ArrayList<>();
        //language=PostgreSQL
        String query = "select trx_detail.id              as id,\n" +
                "       trx_detail.buku_id         as buku_id,\n" +
                "       trx_detail.tanggal_kembali as tanggal_kembali,\n" +
                "       trx_detail.is_return       as status_kembali,\n" +
                "       trx_detail.date_return     as last_updated_date,\n" +
                "       book.id                    as buku_id,\n" +
                "       book.nama                  as buku_name,\n" +
                "       book.isbn                  as buku_isbn,\n" +
                "       book.penerbit_id           as penerbit_id,\n" +
                "       pub.nama                   as penerbit_nama,\n" +
                "       pub.alamat                 as penerbit_alamat\n" +
                "from perpustakaan.transaksi_detail trx_detail\n" +
                "         left join perpustakaan.buku book on trx_detail.buku_id = book.id\n" +
                "         " +
                "left join perpustakaan.penerbit pub on book.penerbit_id = pub.id\n" +
                "where trx_detail.transaksi_id = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, value);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            TransaksiDetail detail = new TransaksiDetail(
                    resultSet.getString("id"),
                    null,
                    new Buku(
                            resultSet.getString("buku_id"),
                            resultSet.getString("buku_name"),
                            resultSet.getString("buku_isbn"),
                            new Penerbit(
                                    resultSet.getString("penerbit_id"),
                                    resultSet.getString("penerbit_nama"),
                                    resultSet.getString("penerbit_alamat"),
                                    new ArrayList<>()
                            ),
                            new ArrayList<>()
                    ),
                    resultSet.getDate("tanggal_kembali"),
                    resultSet.getBoolean("status_kembali"),
                    resultSet.getDate("last_updated_date")
            );
            list.add(detail);
        }
        statement.close();
        resultSet.close();
        return list;
    }

    @Override
    public List<Transaksi> findAll() throws SQLException {
        return null;
    }

    @Override
    public List<Transaksi> findAll(Long start, Long limit, Long orderIndex, String orderDirection, Transaksi param) throws SQLException {
        return null;
    }
}
