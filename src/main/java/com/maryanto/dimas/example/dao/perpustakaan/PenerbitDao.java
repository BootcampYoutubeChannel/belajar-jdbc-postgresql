package com.maryanto.dimas.example.dao.perpustakaan;

import com.maryanto.dimas.example.dao.CrudRepository;
import com.maryanto.dimas.example.entity.perpustakaan.Buku;
import com.maryanto.dimas.example.entity.perpustakaan.Penerbit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PenerbitDao implements CrudRepository<Penerbit, String> {

    private final Connection connection;

    public PenerbitDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Penerbit save(Penerbit value) throws SQLException {
        //language=PostgreSQL
        String query = "insert into perpustakaan.penerbit(nama, alamat) values (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, value.getNama());
        statement.setString(2, value.getAlamat());
        int row = statement.executeUpdate();
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next())
            value.setId(generatedKeys.getString("id"));
        statement.close();
        return value;
    }

    @Override
    public Penerbit update(Penerbit value) throws SQLException {
        //language=PostgreSQL
        String query = "update perpustakaan.penerbit set nama = ?, alamat = ? where id = ?";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, value.getNama());
        statement.setString(2, value.getAlamat());
        statement.setString(3, value.getId());
        int row = statement.executeUpdate();
        statement.close();
        return value;
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        //language=PostgreSQL
        String query = "delete from perpustakaan.penerbit where id = ?";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, value);
        int row = statement.executeUpdate();
        statement.close();
        return row >= 1;
    }

    @Override
    public Optional<Penerbit> findById(String value) throws SQLException {
        //language=PostgreSQL
        String query = "select p.id     as id,\n" +
                "       p.nama   as nama,\n" +
                "       p.alamat as alamat,\n" +
                "       b.id     as buku_id,\n" +
                "       b.nama   as buku_nama,\n" +
                "       b.isbn   as buku_isbn\n" +
                "from perpustakaan.penerbit p\n" +
                "         join perpustakaan.buku b on p.id = b.penerbit_id\n" +
                "where p.id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, value);
        ResultSet resultSet = statement.executeQuery();
        Penerbit penerbit = new Penerbit();
        List<Buku> listBuku = new ArrayList<>();

        while (resultSet.next()) {
            Buku buku = new Buku();
            penerbit.setId(resultSet.getString("id"));
            penerbit.setNama(resultSet.getString("nama"));
            penerbit.setAlamat(resultSet.getString("alamat"));
            buku.setId(resultSet.getString("buku_id"));
            buku.setNama(resultSet.getString("buku_nama"));
            buku.setIsbn(resultSet.getString("buku_isbn"));
            listBuku.add(buku);
        }

        penerbit.setListBuku(listBuku);
        statement.close();
        resultSet.close();

        return penerbit.getId() != null ? Optional.of(penerbit) : Optional.empty();
    }

    public List<Buku> findByPenerbitId(String value) throws SQLException {
        //language=PostgreSQL
        String query = "select b.id     as buku_id,\n" +
                "       b.nama   as buku_nama,\n" +
                "       b.isbn   as buku_isbn\n" +
                "from perpustakaan.penerbit p\n" +
                "         join perpustakaan.buku b on p.id = b.penerbit_id\n" +
                "where p.id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, value);
        ResultSet resultSet = statement.executeQuery();
        Penerbit penerbit = new Penerbit();
        List<Buku> listBuku = new ArrayList<>();

        while (resultSet.next()) {
            Buku buku = new Buku();
            buku.setId(resultSet.getString("buku_id"));
            buku.setNama(resultSet.getString("buku_nama"));
            buku.setIsbn(resultSet.getString("buku_isbn"));
            listBuku.add(buku);
        }

        statement.close();
        resultSet.close();
        return listBuku;
    }

    @Override
    public List<Penerbit> findAll() throws SQLException {
        List<Penerbit> list = new ArrayList<>();
        //language=PostgreSQL
        String query = "select id     as id,\n" +
                "       nama   as nama,\n" +
                "       alamat as alamat\n" +
                "from perpustakaan.penerbit";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Penerbit data = new Penerbit(
                    resultSet.getString("id"),
                    resultSet.getString("nama"),
                    resultSet.getString("alamat"),
                    this.findByPenerbitId(resultSet.getString("id"))
            );
            list.add(data);
        }
        statement.close();
        resultSet.close();
        return list;
    }

    @Override
    public List<Penerbit> findAll(Long start, Long limit, Long orderIndex, String orderDirection, Penerbit param) throws SQLException {
        return null;
    }
}
