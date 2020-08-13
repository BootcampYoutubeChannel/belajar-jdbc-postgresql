package com.maryanto.dimas.example.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ViewRepository<T, ID> {

    public Optional<T> findById(ID value) throws SQLException;

    public List<T> findAll() throws SQLException;

    public List<T> findAll(Long start, Long limit, Long orderIndex, String orderDirection, T param) throws SQLException;
}
