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
        return null;
    }

    @Override
    public ExampleTable update(ExampleTable value) throws SQLException {
        return null;
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        return null;
    }

    @Override
    public Optional<ExampleTable> findById(String id) throws SQLException {
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
