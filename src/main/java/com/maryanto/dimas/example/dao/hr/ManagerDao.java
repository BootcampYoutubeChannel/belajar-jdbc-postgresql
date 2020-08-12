package com.maryanto.dimas.example.dao.hr;

import com.maryanto.dimas.example.dao.CrudRepository;
import com.maryanto.dimas.example.entity.hr.Job;
import com.maryanto.dimas.example.entity.hr.Manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManagerDao implements CrudRepository<Manager, String> {


    private final Connection connection;

    public ManagerDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Manager save(Manager value) throws SQLException {
        return null;
    }

    @Override
    public Manager update(Manager value) throws SQLException {
        return null;
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        return null;
    }

    @Override
    public Optional<Manager> findById(String value) throws SQLException {
        return Optional.empty();
    }

    @Override
    public List<Manager> findAll() throws SQLException {
        List<Manager> list = new ArrayList<>();
        //language=PostgreSQL
        String baseQuery = "select emp.id                as id,\n" +
                "       emp.nama              as nama,\n" +
                "       emp.tanggal_lahir     as tanggal_lahir,\n" +
                "       emp.tanggal_join      as tanggal_join,\n" +
                "       emp.salary            as salary,\n" +
                "       emp.job_id            as job_id,\n" +
                "       j.name                as job_name,\n" +
                "       emp.manager_id        as manager_id,\n" +
                "       emp.created_date      as created_date,\n" +
                "       emp.created_by        as created_by,\n" +
                "       emp.last_updated_date as last_updated_date,\n" +
                "       emp.last_updated_by   as last_updated_by\n" +
                "from hr.employees emp\n" +
                "         left join hr.jobs j on emp.job_id = j.id";
        StringBuilder query = new StringBuilder(baseQuery)
                .append(" where emp.manager_id is null ");
        PreparedStatement statement = connection.prepareStatement(query.toString());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Manager manager = new Manager();
            manager.setId(resultSet.getString("id"));
            manager.setNama(resultSet.getString("nama"));
            manager.setJob(
                    new Job(resultSet.getString("job_id"),
                            resultSet.getString("job_name"))
            );
            manager.setSalary(resultSet.getBigDecimal("salary"));
            manager.setTanggalLahir(resultSet.getDate("tanggal_lahir"));
            manager.setTanggalJoin(resultSet.getDate("tanggal_join"));
            manager.setChildren(this.findAllByManagerId(baseQuery, manager.getId()));
            list.add(manager);
        }

        statement.close();
        resultSet.close();
        return list;
    }

    public List<Manager> findAllByManagerId(String baseQuery, String managerId) throws SQLException {
        List<Manager> list = new ArrayList<>();
        StringBuilder query = new StringBuilder(baseQuery)
                .append(" where emp.manager_id = ? ");
        PreparedStatement statement = connection.prepareStatement(query.toString());
        statement.setString(1, managerId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Manager manager = new Manager();
            manager.setId(resultSet.getString("id"));
            manager.setNama(resultSet.getString("nama"));
            manager.setJob(
                    new Job(resultSet.getString("job_id"),
                            resultSet.getString("job_name"))
            );
            manager.setSalary(resultSet.getBigDecimal("salary"));
            manager.setTanggalLahir(resultSet.getDate("tanggal_lahir"));
            manager.setTanggalJoin(resultSet.getDate("tanggal_join"));
            manager.setChildren(findAllByManagerId(baseQuery, manager.getId()));
            list.add(manager);
        }
        statement.close();
        resultSet.close();
        return list;
    }

    @Override
    public List<Manager> findAll(Long start, Long limit, Long orderIndex, String orderDirection, Manager param) throws SQLException {
        return null;
    }
}
