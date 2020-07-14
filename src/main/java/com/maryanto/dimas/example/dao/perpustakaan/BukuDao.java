package com.maryanto.dimas.example.dao.perpustakaan;

import com.maryanto.dimas.example.dao.CrudRepository;
import com.maryanto.dimas.example.entity.perpustakaan.Buku;
import com.maryanto.dimas.example.entity.perpustakaan.Penerbit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BukuDao implements CrudRepository<Buku, String> {

    private Connection connection;

    public BukuDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Buku save(Buku value) throws SQLException {
        return null;
    }

    @Override
    public Buku update(Buku value) throws SQLException {
        return null;
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        return null;
    }

    @Override
    public Optional<Buku> findById(String value) throws SQLException {
        //language=PostgreSQL
        String query = "select b.id     as bukuId,\n" +
                "       b.nama   as bukuName,\n" +
                "       b.isbn   as isbn,\n" +
                "       p.id     as penerbitId,\n" +
                "       p.nama   as penerbitName,\n" +
                "       p.alamat as penerbitAlamat\n" +
                "from perpustakaan.buku b\n" +
                "         left join perpustakaan.penerbit p on (b.penerbit_id = p.id)\n" +
                "where b.id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, value);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            resultSet.close();
            statement.close();
            return Optional.empty();
        }

        Buku data = new Buku(
                resultSet.getString("bukuId"),
                resultSet.getString("bukuName"),
                resultSet.getString("isbn"),
                new Penerbit(
                        resultSet.getString("penerbitId"),
                        resultSet.getString("penerbitName"),
                        resultSet.getString("penerbitAlamat"),
                        new ArrayList<>()
                )
        );
        resultSet.close();
        statement.close();
        return Optional.of(data);
    }

    @Override
    public List<Buku> findAll() throws SQLException {
        List<Buku> list = new ArrayList<>();
        //language=PostgreSQL
        String query = "select b.id     as bukuId,\n" +
                "       b.nama   as bukuName,\n" +
                "       b.isbn   as isbn,\n" +
                "       p.id     as penerbitId,\n" +
                "       p.nama   as penerbitName,\n" +
                "       p.alamat as penerbitAlamat\n" +
                "from perpustakaan.buku b\n" +
                "         left join perpustakaan.penerbit p on (b.penerbit_id = p.id)";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {

            Buku data = new Buku(
                    resultSet.getString("bukuId"),
                    resultSet.getString("bukuName"),
                    resultSet.getString("isbn"),
                    new Penerbit(
                            resultSet.getString("penerbitId"),
                            resultSet.getString("penerbitName"),
                            resultSet.getString("penerbitAlamat"),
                            new ArrayList<>()
                    )
            );
            list.add(data);
        }
        statement.close();
        resultSet.close();
        return list;
    }

    @Override
    public List<Buku> findAll(Long start, Long limit, Long orderIndex, String orderDirection, Buku param) throws SQLException {
        return null;
    }
}
