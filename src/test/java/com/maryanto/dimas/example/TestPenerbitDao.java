package com.maryanto.dimas.example;

import com.maryanto.dimas.example.config.DatasourceConfig;
import com.maryanto.dimas.example.dao.perpustakaan.PenerbitDao;
import com.maryanto.dimas.example.entity.perpustakaan.Penerbit;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class TestPenerbitDao extends TestCase {

    private DatasourceConfig config;

    @Override
    protected void setUp() throws Exception {
        this.config = new DatasourceConfig();
    }

    @Test
    public void testFindAll() throws SQLException {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = dataSource.getConnection();
        log.info("status connected");
        PenerbitDao dao = new PenerbitDao(connection);
        List<Penerbit> list = dao.findAll();

        assertEquals("jumlah data di database", 2, list.size());
        connection.close();
    }

    @Test
    public void testFindById() throws SQLException {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = dataSource.getConnection();
        log.info("status connected");
        PenerbitDao dao = new PenerbitDao(connection);
        Optional<Penerbit> id001Optional = dao.findById("001");

        assertTrue("id 001 data ditemukan", id001Optional.isPresent());
        Penerbit penerbit = id001Optional.get();
        assertEquals("id 001 namanya adalah ", "Informatika", penerbit.getNama());

        Optional<Penerbit> randomId = dao.findById(UUID.randomUUID().toString());
        assertFalse("id ramdom tidak ditemukan", randomId.isPresent());
        connection.close();
    }
}
