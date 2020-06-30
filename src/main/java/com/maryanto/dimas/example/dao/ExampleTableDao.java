package com.maryanto.dimas.example.dao;

import com.maryanto.dimas.example.entity.ExampleTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExampleTableDao implements CrudRepository<ExampleTable, String> {

    private final Connection connection;

    public ExampleTableDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ExampleTable save(ExampleTable value) throws SQLException {
        String query = "insert into example_table (id, name, created_date, created_time, is_active, counter, currency, description, floating)\n" +
                "values (uuid_generate_v4(), ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, value.getName());
        statement.setDate(2, value.getCreatedDate());
        statement.setTimestamp(3, value.getCreatedTime());
        statement.setBoolean(4, value.getActive());
        statement.setInt(5, value.getCounter());
        statement.setBigDecimal(6, value.getCurrency());
        statement.setString(7, value.getDescription());
        statement.setDouble(8, value.getFloating());
        int row = statement.executeUpdate();
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next())
            value.setId(generatedKeys.getString("id"));
        statement.close();
        return value;
    }

    @Override
    public ExampleTable update(ExampleTable value) throws SQLException {
        //language=PostgreSQL
        String query = "update example_table\n" +
                "set name        = ?,\n" +
                "    is_active   = ?,\n" +
                "    counter     = ?,\n" +
                "    currency    = ?,\n" +
                "    description = ?,\n" +
                "    floating    = ?\n" +
                "where id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, value.getName());
        statement.setBoolean(2, value.getActive());
        statement.setInt(3, value.getCounter());
        statement.setBigDecimal(4, value.getCurrency());
        statement.setString(5, value.getDescription());
        statement.setDouble(6, value.getFloating());
        statement.setString(7, value.getId());
        int row = statement.executeUpdate();
        statement.close();
        return value;
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        //language=PostgreSQL
        String query = "delete\n" +
                "from example_table\n" +
                "where id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, value);
        int row = statement.executeUpdate();
        statement.close();
        return row >= 1;
    }

    @Override
    public Optional<ExampleTable> findById(String id) throws SQLException {
        //language=PostgreSQL
        String query = "select id           as id,\n" +
                "       name         as name,\n" +
                "       created_date as createdDate,\n" +
                "       created_time as createdTime,\n" +
                "       is_active    as active,\n" +
                "       counter      as counter,\n" +
                "       currency     as currency,\n" +
                "       description  as description,\n" +
                "       floating     as floating\n" +
                "from example_table where id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        ExampleTable data;
        if (resultSet.next()) {
            data = new ExampleTable(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getDate("createdDate"),
                    resultSet.getTimestamp("createdTime"),
                    resultSet.getObject("active", Boolean.class),
                    resultSet.getInt("counter"),
                    resultSet.getBigDecimal("currency"),
                    resultSet.getString("description"),
                    resultSet.getDouble("floating")
            );
            statement.close();
            resultSet.close();
            return Optional.of(data);
        } else {
            statement.close();
            resultSet.close();
            return Optional.empty();
        }
    }

    @Override
    public List<ExampleTable> findAll() throws SQLException {
        List<ExampleTable> list = new ArrayList<>();
        //language=PostgreSQL
        String query = "select id           as id,\n" +
                "       name         as name,\n" +
                "       created_date as createdDate,\n" +
                "       created_time as createdTime,\n" +
                "       is_active    as active,\n" +
                "       counter      as counter,\n" +
                "       currency     as currency,\n" +
                "       description  as description,\n" +
                "       floating     as floating\n" +
                "from example_table";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            ExampleTable data = new ExampleTable(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getDate("createdDate"),
                    resultSet.getTimestamp("createdTime"),
                    resultSet.getObject("active", Boolean.class),
                    resultSet.getInt("counter"),
                    resultSet.getBigDecimal("currency"),
                    resultSet.getString("description"),
                    resultSet.getDouble("floating")
            );
            list.add(data);
        }
        statement.close();
        resultSet.close();
        return list;
    }

    @Override
    public List<ExampleTable> findAll(Long start, Long limit, Long orderIndex, String orderDirection, ExampleTable param) throws SQLException {
        List<ExampleTable> list = new ArrayList<>();
        //language=PostgreSQL
        String query = "select id           as id,\n" +
                "       name         as name,\n" +
                "       created_date as createdDate,\n" +
                "       created_time as createdTime,\n" +
                "       is_active    as active,\n" +
                "       counter      as counter,\n" +
                "       currency     as currency,\n" +
                "       description  as description,\n" +
                "       floating     as floating\n" +
                "from example_table\n" +
                "where lower(name) like ? or is_active = ?\n" +
                "limit ? offset ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, param.getName());
        statement.setBoolean(2, param.getActive());
        statement.setLong(3, limit);
        statement.setLong(4, start);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            ExampleTable data = new ExampleTable(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getDate("createdDate"),
                    resultSet.getTimestamp("createdTime"),
                    resultSet.getObject("active", Boolean.class),
                    resultSet.getInt("counter"),
                    resultSet.getBigDecimal("currency"),
                    resultSet.getString("description"),
                    resultSet.getDouble("floating")
            );
            list.add(data);
        }
        statement.close();
        resultSet.close();
        return list;
    }
}
