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
        //language=PostgreSQL
        String query = "insert into perpustakaan.buku(nama, isbn, penerbit_id) values (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, value.getNama());
        statement.setString(2, value.getIsbn());
        if (value.getPenerbit() != null) {
            statement.setNull(3, Types.VARCHAR);
        } else {
            statement.setString(3, value.getPenerbit().getId());
        }
        int row = statement.executeUpdate();
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next())
            value.setId(generatedKeys.getString("id"));
        statement.close();
        return value;
    }

    @Override
    public Buku update(Buku value) throws SQLException {
        //language=PostgreSQL
        String query = "update perpustakaan.buku set nama = ?, isbn = ?, penerbit_id =? where id = ?";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, value.getNama());
        statement.setString(2, value.getIsbn());
        if (value.getPenerbit() != null) {
            statement.setNull(3, Types.VARCHAR);
        } else {
            statement.setString(3, value.getPenerbit().getId());
        }
        statement.setString(4, value.getId());
        int row = statement.executeUpdate();
        statement.close();
        return value;
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        //language=PostgreSQL
        String query = "delete from perpustakaan.buku where id = ?";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, value);
        int row = statement.executeUpdate();
        statement.close();
        return row >= 1;
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
