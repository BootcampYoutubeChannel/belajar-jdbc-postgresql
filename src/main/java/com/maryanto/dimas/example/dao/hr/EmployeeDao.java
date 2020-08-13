package com.maryanto.dimas.example.dao.hr;

import com.maryanto.dimas.example.dao.ViewRepository;
import com.maryanto.dimas.example.entity.hr.Employee;
import com.maryanto.dimas.example.entity.hr.Job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDao implements ViewRepository<Employee, String> {

    private final Connection connection;

    public EmployeeDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Employee> findById(String value) throws SQLException {
        //language=PostgreSQL
        String query = "select employee_id,\n" +
                "       employee_name,\n" +
                "       employee_birthdate,\n" +
                "       employee_join,\n" +
                "       employee_salary,\n" +
                "       employee_job_id,\n" +
                "       employee_job_name,\n" +
                "       employee_created_date,\n" +
                "       employee_created_by,\n" +
                "       employee_last_updated_date,\n" +
                "       employee_last_updated_by,\n" +
                "       manager_id,\n" +
                "       manager_name,\n" +
                "       manager_birthdate,\n" +
                "       manager_join,\n" +
                "       manager_salary,\n" +
                "       manager_job_id,\n" +
                "       manager_job_name,\n" +
                "       manager_created_date,\n" +
                "       manager_created_by,\n" +
                "       manager_last_updated_date,\n" +
                "       manager_last_updated_by\n" +
                "from hr.employee_detail\n" +
                "where employee_id = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, value);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            statement.close();
            resultSet.close();
            return Optional.empty();
        }

        Employee employee = Employee.builder()
                .id(resultSet.getString("employee_id"))
                .nama(resultSet.getString("employee_name"))
                .salary(resultSet.getBigDecimal("employee_salary"))
                .tanggalJoin(resultSet.getDate("employee_join"))
                .tanggalLahir(resultSet.getDate("employee_birthdate"))
                .job(
                        new Job(
                                resultSet.getString("employee_job_id"),
                                resultSet.getString("employee_job_name"))
                ).build();
        if (resultSet.getString("manager_id") != null)
            employee.setManager(
                    Employee.builder()
                            .id(resultSet.getString("manager_id"))
                            .nama(resultSet.getString("manager_name"))
                            .salary(resultSet.getBigDecimal("manager_salary"))
                            .tanggalJoin(resultSet.getDate("manager_join"))
                            .tanggalLahir(resultSet.getDate("manager_birthdate"))
                            .job(
                                    new Job(
                                            resultSet.getString("manager_job_id"),
                                            resultSet.getString("manager_job_name"))
                            ).build()
            );
        return Optional.of(employee);
    }

    @Override
    public List<Employee> findAll() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String query = "select employee_id,\n" +
                "       employee_name,\n" +
                "       employee_birthdate,\n" +
                "       employee_join,\n" +
                "       employee_salary,\n" +
                "       employee_job_id,\n" +
                "       employee_job_name,\n" +
                "       employee_created_date,\n" +
                "       employee_created_by,\n" +
                "       employee_last_updated_date,\n" +
                "       employee_last_updated_by,\n" +
                "       manager_id,\n" +
                "       manager_name,\n" +
                "       manager_birthdate,\n" +
                "       manager_join,\n" +
                "       manager_salary,\n" +
                "       manager_job_id,\n" +
                "       manager_job_name,\n" +
                "       manager_created_date,\n" +
                "       manager_created_by,\n" +
                "       manager_last_updated_date,\n" +
                "       manager_last_updated_by\n" +
                "from hr.employee_detail";

        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Employee employee = Employee.builder()
                    .id(resultSet.getString("employee_id"))
                    .nama(resultSet.getString("employee_name"))
                    .salary(resultSet.getBigDecimal("employee_salary"))
                    .tanggalJoin(resultSet.getDate("employee_join"))
                    .tanggalLahir(resultSet.getDate("employee_birthdate"))
                    .job(
                            new Job(
                                    resultSet.getString("employee_job_id"),
                                    resultSet.getString("employee_job_name"))
                    ).build();
            if (resultSet.getString("manager_id") != null)
                employee.setManager(
                        Employee.builder()
                                .id(resultSet.getString("manager_id"))
                                .nama(resultSet.getString("manager_name"))
                                .salary(resultSet.getBigDecimal("manager_salary"))
                                .tanggalJoin(resultSet.getDate("manager_join"))
                                .tanggalLahir(resultSet.getDate("manager_birthdate"))
                                .job(
                                        new Job(
                                                resultSet.getString("manager_job_id"),
                                                resultSet.getString("manager_job_name"))
                                ).build()
                );
            employees.add(employee);
        }
        statement.close();
        resultSet.close();
        return employees;
    }

    @Override
    public List<Employee> findAll(Long start, Long limit, Long orderIndex, String orderDirection, Employee param) throws SQLException {
        return null;
    }
}
