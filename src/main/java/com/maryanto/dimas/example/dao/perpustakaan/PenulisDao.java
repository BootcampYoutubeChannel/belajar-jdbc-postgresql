package com.maryanto.dimas.example.dao.perpustakaan;

import com.maryanto.dimas.example.dao.CrudRepository;
import com.maryanto.dimas.example.entity.perpustakaan.Buku;
import com.maryanto.dimas.example.entity.perpustakaan.Penerbit;
import com.maryanto.dimas.example.entity.perpustakaan.Penulis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PenulisDao implements CrudRepository<Penulis, String> {

    private final Connection connection;

    public PenulisDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Penulis save(Penulis value) throws SQLException {
        return null;
    }

    @Override
    public Penulis update(Penulis value) throws SQLException {
        return null;
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        return null;
    }

    @Override
    public Optional<Penulis> findById(String value) throws SQLException {
        //language=PostgreSQL
        String query = "select id     as id,\n" +
                "       nama   as nama,\n" +
                "       alamat as alamat\n" +
                "from perpustakaan.penulis\n" +
                "where id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, value);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            statement.close();
            resultSet.close();
            return Optional.empty();
        }

        Penulis penulis = new Penulis(
                resultSet.getString("id"),
                resultSet.getString("nama"),
                resultSet.getString("alamat"),
                this.findBukuByPenulisId(resultSet.getString("id"))
        );
        statement.close();
        resultSet.close();
        return Optional.of(penulis);
    }

    public List<Buku> findBukuByPenulisId(String value) throws SQLException {
        List<Buku> list = new ArrayList<>();
        //language=PostgreSQL
        String query = "select pb.id         as id,\n" +
                "       b.id          as buku_id,\n" +
                "       b.nama        as buku_nama,\n" +
                "       b.isbn        as buku_isbn,\n" +
                "       b.penerbit_id as penerbit_id,\n" +
                "       pub.nama      as penerbit_nama,\n" +
                "       pub.alamat    as penerbit_alamat\n" +
                "from perpustakaan.penulis_buku pb\n" +
                "         left join perpustakaan.buku b on pb.buku_id = b.id\n" +
                "         left join perpustakaan.penerbit pub on b.penerbit_id = pub.id\n" +
                "where pb.penulis_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, value);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Buku buku = new Buku(
                    resultSet.getString("buku_id"),
                    resultSet.getString("buku_nama"),
                    resultSet.getString("buku_isbn"),
                    new Penerbit(
                            resultSet.getString("penerbit_id"),
                            resultSet.getString("penerbit_nama"),
                            resultSet.getString("penerbit_alamat"),
                            new ArrayList<>()
                    ),
                    new ArrayList<>()
            );
            list.add(buku);
        }

        statement.close();
        resultSet.close();
        return list;
    }

    @Override
    public List<Penulis> findAll() throws SQLException {
        return null;
    }

    @Override
    public List<Penulis> findAll(Long start, Long limit, Long orderIndex, String orderDirection, Penulis param) throws SQLException {
        return null;
    }
}
