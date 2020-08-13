package com.maryanto.dimas.example;

import com.maryanto.dimas.example.config.DatasourceConfig;
import com.maryanto.dimas.example.dao.hr.EmployeeDao;
import com.maryanto.dimas.example.entity.hr.Employee;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class TestEmployeeDao extends TestCase {


    private DatasourceConfig config;

    @Override
    protected void setUp() throws Exception {
        this.config = new DatasourceConfig();
    }

    @Test
    public void testFindById() throws SQLException {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = dataSource.getConnection();

        EmployeeDao dao = new EmployeeDao(connection);
        Optional<Employee> optional = dao.findById("002");

        assertTrue("employee id 002", optional.isPresent());

        Employee employee = optional.get();
        log.info("{}", employee);
        assertNotNull("employee id 002 has manager", employee.getManager());

        optional = dao.findById("001");
        assertTrue("employee id 001", optional.isPresent());

        employee = optional.get();
        log.info("{}", employee);
        assertNull("employee id 001 has no manager", employee.getManager());
    }

    @Test
    public void testFindAll() throws SQLException {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = dataSource.getConnection();

        EmployeeDao dao = new EmployeeDao(connection);
        List<Employee> list = dao.findAll();
        long count = list.stream().count();
        assertEquals("jumlah data employee", count, 7);
    }

}
