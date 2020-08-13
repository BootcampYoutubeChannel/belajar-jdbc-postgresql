package com.maryanto.dimas.example.dao;

import java.sql.SQLException;

public interface CrudRepository<T, ID> extends ViewRepository<T, ID> {

    public T save(T value) throws SQLException;

    public T update(T value) throws SQLException;

    public Boolean removeById(ID value) throws SQLException;

}
