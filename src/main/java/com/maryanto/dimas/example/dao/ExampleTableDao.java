package com.maryanto.dimas.example.dao;

import com.maryanto.dimas.example.entity.ExampleTable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExampleTableDao implements CrudRepository<ExampleTable, String> {

    private Connection connection;

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
    public Optional<ExampleTable> findById(String value) throws SQLException {
        return Optional.empty();
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
        return null;
    }
}
