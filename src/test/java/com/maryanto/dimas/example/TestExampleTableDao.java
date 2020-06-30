package com.maryanto.dimas.example;

import com.maryanto.dimas.example.config.DatasourceConfig;
import com.maryanto.dimas.example.dao.ExampleTableDao;
import com.maryanto.dimas.example.entity.ExampleTable;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class TestExampleTableDao extends TestCase {

    private DatasourceConfig config;

    @Override
    protected void setUp() throws Exception {
        this.config = new DatasourceConfig();
    }

    @Test
    public void testShowListData() throws SQLException {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = dataSource.getConnection();
        log.info("status connected");
        ExampleTableDao dao = new ExampleTableDao(connection);
        List<ExampleTable> listData = dao.findAll();
        log.info("{}", listData);
        Assert.assertEquals("Jumlah list data", listData.size(), 6);

        connection.close();
    }

    @Test
    public void testFindById() throws SQLException {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = dataSource.getConnection();
        log.info("status connected");
        ExampleTableDao dao = new ExampleTableDao(connection);
        Optional<ExampleTable> optional = dao.findById("00'1");
        log.info("{}", optional.isPresent());

        connection.close();
    }
}
