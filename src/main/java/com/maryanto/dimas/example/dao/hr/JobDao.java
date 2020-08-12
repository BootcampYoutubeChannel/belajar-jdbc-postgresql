package com.maryanto.dimas.example.dao.hr;

import com.maryanto.dimas.example.dao.CrudRepository;
import com.maryanto.dimas.example.entity.hr.Job;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JobDao implements CrudRepository<Job, String> {

    private final Connection connection;

    public JobDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Job save(Job value) throws SQLException {
        return null;
    }

    @Override
    public Job update(Job value) throws SQLException {
        return null;
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        return null;
    }

    @Override
    public Optional<Job> findById(String value) throws SQLException {
        return Optional.empty();
    }

    @Override
    public List<Job> findAll() throws SQLException {
        return null;
    }

    @Override
    public List<Job> findAll(Long start, Long limit, Long orderIndex, String orderDirection, Job param) throws SQLException {
        return null;
    }
}
