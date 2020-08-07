package com.maryanto.dimas.example.dao.perpustakaan;

import com.maryanto.dimas.example.dao.CrudRepository;
import com.maryanto.dimas.example.entity.perpustakaan.Anggota;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AnggotaDao implements CrudRepository<Anggota, String> {

    private final Connection connection;

    public AnggotaDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Anggota save(Anggota value) throws SQLException {
        return null;
    }

    @Override
    public Anggota update(Anggota value) throws SQLException {
        return null;
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        return null;
    }

    @Override
    public Optional<Anggota> findById(String value) throws SQLException {
        //language=PostgreSQL
        String query = "select id        as id,\n" +
                "       nomor_ktp as ktp,\n" +
                "       nama      as nama,\n" +
                "       alamat    as alamat\n" +
                "from perpustakaan.anggota\n" +
                "where id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, value);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            statement.close();
            resultSet.close();
            return Optional.empty();
        }

        Anggota anggota = new Anggota(
                resultSet.getString("id"),
                resultSet.getString("ktp"),
                resultSet.getString("nama"),
                resultSet.getString("alamat")
        );
        statement.close();
        resultSet.close();
        return Optional.of(anggota);
    }

    @Override
    public List<Anggota> findAll() throws SQLException {
        return null;
    }

    @Override
    public List<Anggota> findAll(Long start, Long limit, Long orderIndex, String orderDirection, Anggota param) throws SQLException {
        return null;
    }
}
