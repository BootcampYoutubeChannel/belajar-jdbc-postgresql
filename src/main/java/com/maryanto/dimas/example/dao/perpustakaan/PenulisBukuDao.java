package com.maryanto.dimas.example.dao.perpustakaan;

import com.maryanto.dimas.example.dao.CrudRepository;
import com.maryanto.dimas.example.entity.perpustakaan.PenulisBuku;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class PenulisBukuDao implements CrudRepository<PenulisBuku, String> {

    private final Connection connection;

    public PenulisBukuDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public PenulisBuku save(PenulisBuku value) throws SQLException {
        String query = "insert into perpustakaan.penulis_buku (buku_id, penulis_id)\n" +
                "values (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        if (value.getBuku() != null)
            statement.setString(1, value.getBuku().getId());
        else
            statement.setNull(1, Types.VARCHAR);

        if (value.getPenulis() != null)
            statement.setString(2, value.getPenulis().getId());
        else
            statement.setNull(2, Types.VARCHAR);
        int row = statement.executeUpdate();
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next())
            value.setId(generatedKeys.getString("id"));
        statement.close();
        return value;
    }

    public void save(List<PenulisBuku> list) throws SQLException {
        String query = "insert into perpustakaan.penulis_buku (buku_id, penulis_id)\n" +
                "values (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        for (PenulisBuku value : list) {
            if (value.getBuku() != null)
                statement.setString(1, value.getBuku().getId());
            else
                statement.setNull(1, Types.VARCHAR);

            if (value.getPenulis() != null)
                statement.setString(2, value.getPenulis().getId());
            else
                statement.setNull(2, Types.VARCHAR);
            statement.addBatch();
        }

        statement.executeBatch();
        statement.close();
    }

    @Override
    @Deprecated
    public PenulisBuku update(PenulisBuku value) throws SQLException {
        return null;
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        String query = "delete\n" +
                "from perpustakaan.penulis_buku\n" +
                "where id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, value);
        int row = statement.executeUpdate();
        statement.close();
        return row >= 1;
    }

    @Override
    @Deprecated
    public Optional<PenulisBuku> findById(String value) throws SQLException {
        return Optional.empty();
    }

    @Override
    @Deprecated
    public List<PenulisBuku> findAll() throws SQLException {
        return null;
    }

    @Override
    @Deprecated
    public List<PenulisBuku> findAll(Long start, Long limit, Long orderIndex, String orderDirection, PenulisBuku param) throws SQLException {
        return null;
    }
}
