package com.maryanto.dimas.example.dao.perpustakaan;

import com.maryanto.dimas.example.dao.CrudRepository;
import com.maryanto.dimas.example.entity.perpustakaan.Penerbit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PenerbitDao implements CrudRepository<Penerbit, String> {

    private Connection connection;

    public PenerbitDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Penerbit save(Penerbit value) throws SQLException {
        return null;
    }

    @Override
    public Penerbit update(Penerbit value) throws SQLException {
        return null;
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        return null;
    }

    @Override
    public Optional<Penerbit> findById(String value) throws SQLException {
        String query = "select id     as id,\n" +
                "       nama   as nama,\n" +
                "       alamat as alamat\n" +
                "from perpustakaan.penerbit\n" +
                "where id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, value);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            statement.close();
            resultSet.close();
            return Optional.empty();
        }

        Penerbit data = new Penerbit(
                resultSet.getString("id"),
                resultSet.getString("nama"),
                resultSet.getString("alamat"),
                new ArrayList<>()
        );
        statement.close();
        resultSet.close();
        return Optional.of(data);
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
                    new ArrayList<>()
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
