package com.maryanto.dimas.example.dao.bank;

import com.maryanto.dimas.example.dao.CrudRepository;
import com.maryanto.dimas.example.entity.bank.BadanUsaha;
import com.maryanto.dimas.example.entity.bank.Nasabah;
import com.maryanto.dimas.example.entity.bank.Perorangan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NasabahDao implements CrudRepository<Nasabah, String> {


    private final Connection connection;

    public NasabahDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Nasabah save(Nasabah value) throws SQLException {
        String query = "insert into bank.nasabah(nama, npwp, siup, ktp, foto, type, created_date, created_by)\n" +
                "values (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, value.getNama());
        statement.setTimestamp(7, value.getCreatedDate());
        statement.setString(8, value.getCreatedBy());

        if (value instanceof BadanUsaha) {
            BadanUsaha badanUsaha = (BadanUsaha) value;
            statement.setString(2, badanUsaha.getNpwp());
            statement.setString(3, badanUsaha.getSiup());
            statement.setNull(4, Types.VARCHAR);
            statement.setNull(5, Types.VARCHAR);
            statement.setInt(6, 2);
        } else {
            Perorangan perorangan = (Perorangan) value;
            statement.setNull(2, Types.VARCHAR);
            statement.setNull(3, Types.VARCHAR);
            statement.setString(4, perorangan.getKtp());
            statement.setString(5, perorangan.getFoto());
            statement.setInt(6, 1);
        }
        int row = statement.executeUpdate();
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next())
            value.setCif(generatedKeys.getString("cif"));
        statement.close();
        return value;
    }

    @Override
    public Nasabah update(Nasabah value) throws SQLException {
        return null;
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        return null;
    }

    @Override
    public Optional<Nasabah> findById(String value) throws SQLException {
        //language=PostgreSQL
        String query = "select cif               as id,\n" +
                "       nama              as nama,\n" +
                "       npwp              as badan_usaha_npwp,\n" +
                "       siup              as badan_usaha_siup,\n" +
                "       ktp               as perorangan_ktp,\n" +
                "       foto              as perorangan_foto,\n" +
                "       type              as type_nasabah,\n" +
                "       created_date      as created_date,\n" +
                "       created_by        as created_by,\n" +
                "       last_updated_date as last_updated_date,\n" +
                "       last_updated_by   as last_updated_by\n" +
                "from bank.nasabah\n" +
                "where cif = ?";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, value);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            statement.close();
            resultSet.close();
            return Optional.empty();
        }

        int typeNasabah = resultSet.getInt("type_nasabah");
        if (typeNasabah == 1) {
            Perorangan perorangan = new Perorangan();
            perorangan.setCif(resultSet.getString("id"));
            perorangan.setNama(resultSet.getString("nama"));
            perorangan.setKtp(resultSet.getString("perorangan_ktp"));
            perorangan.setFoto(resultSet.getString("perorangan_foto"));
            perorangan.setCreatedDate(resultSet.getTimestamp("created_date"));
            perorangan.setCreatedBy(resultSet.getString("created_by"));
            perorangan.setLastUpdatedBy(resultSet.getString("last_updated_by"));
            perorangan.setLastUpdatedDate(resultSet.getTimestamp("last_updated_date"));

            statement.close();
            resultSet.close();
            return Optional.of(perorangan);
        } else {
            BadanUsaha badanUsaha = new BadanUsaha();
            badanUsaha.setCif(resultSet.getString("id"));
            badanUsaha.setNama(resultSet.getString("nama"));
            badanUsaha.setNpwp(resultSet.getString("badan_usaha_npwp"));
            badanUsaha.setSiup(resultSet.getString("badan_usaha_siup"));
            badanUsaha.setCreatedDate(resultSet.getTimestamp("created_date"));
            badanUsaha.setCreatedBy(resultSet.getString("created_by"));
            badanUsaha.setLastUpdatedBy(resultSet.getString("last_updated_by"));
            badanUsaha.setLastUpdatedDate(resultSet.getTimestamp("last_updated_date"));

            statement.close();
            resultSet.close();
            return Optional.of(badanUsaha);
        }
    }

    @Override
    public List<Nasabah> findAll() throws SQLException {
        List<Nasabah> list = new ArrayList<>();
        String query = "select cif               as id,\n" +
                "       nama              as nama,\n" +
                "       npwp              as badan_usaha_npwp,\n" +
                "       siup              as badan_usaha_siup,\n" +
                "       ktp               as perorangan_ktp,\n" +
                "       foto              as perorangan_foto,\n" +
                "       type              as type_nasabah,\n" +
                "       created_date      as created_date,\n" +
                "       created_by        as created_by,\n" +
                "       last_updated_date as last_updated_date,\n" +
                "       last_updated_by   as last_updated_by\n" +
                "from bank.nasabah";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int typeNasabah = resultSet.getInt("type_nasabah");
            if (typeNasabah == 1) {
                Perorangan perorangan = new Perorangan();
                perorangan.setCif(resultSet.getString("id"));
                perorangan.setNama(resultSet.getString("nama"));
                perorangan.setKtp(resultSet.getString("perorangan_ktp"));
                perorangan.setFoto(resultSet.getString("perorangan_foto"));
                perorangan.setCreatedDate(resultSet.getTimestamp("created_date"));
                perorangan.setCreatedBy(resultSet.getString("created_by"));
                perorangan.setLastUpdatedBy(resultSet.getString("last_updated_by"));
                perorangan.setLastUpdatedDate(resultSet.getTimestamp("last_updated_date"));

                list.add(perorangan);
            } else {
                BadanUsaha badanUsaha = new BadanUsaha();
                badanUsaha.setCif(resultSet.getString("id"));
                badanUsaha.setNama(resultSet.getString("nama"));
                badanUsaha.setNpwp(resultSet.getString("badan_usaha_npwp"));
                badanUsaha.setSiup(resultSet.getString("badan_usaha_siup"));
                badanUsaha.setCreatedDate(resultSet.getTimestamp("created_date"));
                badanUsaha.setCreatedBy(resultSet.getString("created_by"));
                badanUsaha.setLastUpdatedBy(resultSet.getString("last_updated_by"));
                badanUsaha.setLastUpdatedDate(resultSet.getTimestamp("last_updated_date"));
                list.add(badanUsaha);
            }
        }
        statement.close();
        resultSet.close();
        return list;
    }

    @Override
    public List<Nasabah> findAll(Long start, Long limit, Long orderIndex, String orderDirection, Nasabah param) throws SQLException {
        return null;
    }
}
