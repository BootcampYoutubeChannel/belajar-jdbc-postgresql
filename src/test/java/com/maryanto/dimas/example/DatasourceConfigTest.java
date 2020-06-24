package com.maryanto.dimas.example;

import com.maryanto.dimas.example.config.DatasourceConfig;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class DatasourceConfigTest extends TestCase {

    private DatasourceConfig config;
    @Override
    protected void setUp() throws Exception {
//        this.config = new DatasourceConfig(
//                "jdbc:postgresql://localhost:5432/bootcamp",
//                "bootcamp",
//                "admin");
        this.config = new DatasourceConfig();
    }

    @Test
    public void testConnectionToDB() throws SQLException {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = dataSource.getConnection();
        log.info("status connected");
    }

}
