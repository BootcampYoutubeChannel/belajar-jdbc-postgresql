package com.maryanto.dimas.example.dao.bank;

import com.maryanto.dimas.example.dao.CrudRepository;
import com.maryanto.dimas.example.entity.bank.BadanUsaha;
import com.maryanto.dimas.example.entity.bank.Nasabah;
import com.maryanto.dimas.example.entity.bank.Perorangan;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NasabahMultiTableDao implements CrudRepository<Nasabah, String> {

    private final Connection connection;

    public NasabahMultiTableDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Nasabah save(Nasabah value) throws SQLException {
        if (value instanceof BadanUsaha) {
            String query = "insert into bank.nasabah_badan_usaha(nama, npwp, siup, created_date, created_by)\n" +
                    "values (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            BadanUsaha badanUsaha = (BadanUsaha) value;
            statement.setString(1, value.getNama());
            statement.setString(2, badanUsaha.getNpwp());
            statement.setString(3, badanUsaha.getSiup());
            statement.setTimestamp(4, value.getCreatedDate());
            statement.setString(5, value.getCreatedBy());
            int row = statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next())
                value.setCif(generatedKeys.getString("cif"));
            statement.close();
            return value;
        } else {
            String query = "insert into bank.nasabah_perorangan(nama, ktp, foto, created_date, created_by)\n" +
                    "values (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            Perorangan badanUsaha = (Perorangan) value;
            statement.setString(1, value.getNama());
            statement.setString(2, badanUsaha.getKtp());
            statement.setString(3, badanUsaha.getFoto());
            statement.setTimestamp(4, value.getCreatedDate());
            statement.setString(5, value.getCreatedBy());
            int row = statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next())
                value.setCif(generatedKeys.getString("cif"));
            statement.close();
            return value;
        }
    }

    @Override
    public Nasabah update(Nasabah value) throws SQLException {
        return null;
    }

    public Boolean transferSaldoNasabahPerorangToBadanUsaha(Perorangan perorangan, BadanUsaha badanUsaha, BigDecimal saldo) throws SQLException {
        CallableStatement statement = connection.prepareCall("call bank.trf_perorangan_ke_badan_usaha(?, ?, ?)");
        statement.setString(1, perorangan.getCif());
        statement.setString(2, badanUsaha.getCif());
        statement.setBigDecimal(3, saldo);
        int row = statement.executeUpdate();
        return row >= 1;
    }

    @Override
    @Deprecated
    public Boolean removeById(String value) throws SQLException {
        return null;
    }

    public Boolean removeById(Nasabah value) throws SQLException {
        int rowDeleted = 0;
        if (value instanceof Perorangan) {
            String query = "delete\n" +
                    "from bank.nasabah_perorangan\n" +
                    "where cif = ?";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, value.getCif());
            rowDeleted = statement.executeUpdate();
            statement.close();
        } else {
            String query = "delete\n" +
                    "from bank.nasabah_badan_usaha\n" +
                    "where cif = ?";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, value.getCif());
            rowDeleted = statement.executeUpdate();
            statement.close();
        }
        return rowDeleted >= 1;
    }

    @Override
    public Optional<Nasabah> findById(String value) throws SQLException {
        //language=PostgreSQL
        String query = "select nasabah_cif               as nasabah_id,\n" +
                "       nasabah_nama              as nasabah_nama,\n" +
                "       nasabah_npwp              as badan_usaha_npwp,\n" +
                "       nasabah_siup              as badan_usaha_siup,\n" +
                "       nasabah_ktp               as perorangan_ktp,\n" +
                "       nasabah_foto              as perorangan_foto,\n" +
                "       nasabah_type              as nasabah_type,\n" +
                "       nasabah_created_date      as nasabah_created_date,\n" +
                "       nasabah_created_by        as nasabah_created_by,\n" +
                "       nasabah_last_updated_date as nasabah_last_updated_date,\n" +
                "       nasabah_last_updated_by   as nasabah_last_updated_by\n" +
                "from bank.findbyidnasabah(?)";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, value);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            statement.close();
            resultSet.close();
            return Optional.empty();
        }

        int typeNasabah = resultSet.getInt("nasabah_type");
        if (typeNasabah == 1) {
            Perorangan perorangan = new Perorangan();
            perorangan.setCif(resultSet.getString("nasabah_id"));
            perorangan.setNama(resultSet.getString("nasabah_nama"));
            perorangan.setKtp(resultSet.getString("perorangan_ktp"));
            perorangan.setFoto(resultSet.getString("perorangan_foto"));
            perorangan.setCreatedDate(resultSet.getTimestamp("nasabah_created_date"));
            perorangan.setCreatedBy(resultSet.getString("nasabah_created_by"));
            perorangan.setLastUpdatedBy(resultSet.getString("nasabah_last_updated_date"));
            perorangan.setLastUpdatedDate(resultSet.getTimestamp("nasabah_last_updated_by"));

            statement.close();
            resultSet.close();
            return Optional.of(perorangan);
        } else {
            BadanUsaha badanUsaha = new BadanUsaha();
            badanUsaha.setCif(resultSet.getString("nasabah_id"));
            badanUsaha.setNama(resultSet.getString("nasabah_nama"));
            badanUsaha.setNpwp(resultSet.getString("badan_usaha_npwp"));
            badanUsaha.setSiup(resultSet.getString("badan_usaha_siup"));
            badanUsaha.setCreatedDate(resultSet.getTimestamp("nasabah_created_date"));
            badanUsaha.setCreatedBy(resultSet.getString("nasabah_created_by"));
            badanUsaha.setLastUpdatedBy(resultSet.getString("nasabah_last_updated_date"));
            badanUsaha.setLastUpdatedDate(resultSet.getTimestamp("nasabah_last_updated_by"));

            statement.close();
            resultSet.close();
            return Optional.of(badanUsaha);
        }
    }

    @Override
    public List<Nasabah> findAll() throws SQLException {
        List<Nasabah> list = new ArrayList<>();
        //language=PostgreSQL
        String query = "select cif               as nasabah_id,\n" +
                "       nama              as nasabah_nama,\n" +
                "       null              as badan_usaha_npwp,\n" +
                "       null              as badan_usaha_siup,\n" +
                "       ktp               as perorangan_ktp,\n" +
                "       foto              as perorangan_foto,\n" +
                "       1                 as nasabah_type,\n" +
                "       created_date      as nasabah_created_" +
                "date,\n" +
                "       created_by        as nasabah_created_by,\n" +
                "       last_updated_date as nasabah_last_updated_date,\n" +
                "       last_updated_by   as nasabah_last_updated_by\n" +
                "from bank.nasabah_perorangan\n" +
                "union\n" +
                "select cif               as nasabah_id,\n" +
                "       nama              as nasabah_nama,\n" +
                "       npwp              as badan_usaha_npwp,\n" +
                "       siup              as badan_usaha_siup,\n" +
                "       null              as perorangan_ktp,\n" +
                "       null              as perorangan_foto,\n" +
                "       2                 as nasabah_type,\n" +
                "       created_date      as nasabah_created_date,\n" +
                "       created_by        as nasabah_created_by,\n" +
                "       last_updated_date as nasabah_last_updated_date,\n" +
                "       last_updated_by   as nasabah_last_updated_by\n" +
                "from bank.nasabah_badan_usaha";

        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int typeNasabah = resultSet.getInt("nasabah_type");
            if (typeNasabah == 1) {
                Perorangan perorangan = new Perorangan();
                perorangan.setCif(resultSet.getString("nasabah_id"));
                perorangan.setNama(resultSet.getString("nasabah_nama"));
                perorangan.setKtp(resultSet.getString("perorangan_ktp"));
                perorangan.setFoto(resultSet.getString("perorangan_foto"));
                perorangan.setCreatedDate(resultSet.getTimestamp("nasabah_created_date"));
                perorangan.setCreatedBy(resultSet.getString("nasabah_created_by"));
                perorangan.setLastUpdatedBy(resultSet.getString("nasabah_last_updated_date"));
                perorangan.setLastUpdatedDate(resultSet.getTimestamp("nasabah_last_updated_by"));
                list.add(perorangan);
            } else {
                BadanUsaha badanUsaha = new BadanUsaha();
                badanUsaha.setCif(resultSet.getString("nasabah_id"));
                badanUsaha.setNama(resultSet.getString("nasabah_nama"));
                badanUsaha.setNpwp(resultSet.getString("badan_usaha_npwp"));
                badanUsaha.setSiup(resultSet.getString("badan_usaha_siup"));
                badanUsaha.setCreatedDate(resultSet.getTimestamp("nasabah_created_date"));
                badanUsaha.setCreatedBy(resultSet.getString("nasabah_created_by"));
                badanUsaha.setLastUpdatedBy(resultSet.getString("nasabah_last_updated_date"));
                badanUsaha.setLastUpdatedDate(resultSet.getTimestamp("nasabah_last_updated_by"));
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
