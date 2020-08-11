package com.maryanto.dimas.example;

import com.maryanto.dimas.example.config.DatasourceConfig;
import com.maryanto.dimas.example.dao.bank.NasabahSingleTableDao;
import com.maryanto.dimas.example.entity.bank.BadanUsaha;
import com.maryanto.dimas.example.entity.bank.Nasabah;
import com.maryanto.dimas.example.entity.bank.Perorangan;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class NasabahSingleTableDaoTest extends TestCase {

    private DatasourceConfig config;

    @Override
    protected void setUp() throws Exception {
        this.config = new DatasourceConfig();
    }


    @Test
    public void testFindById() throws SQLException {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = dataSource.getConnection();
        log.info("status connected");

        NasabahSingleTableDao dao = new NasabahSingleTableDao(connection);
        Optional<Nasabah> optional = dao.findById("001");

        assertTrue("Data nasabah 001 di temukan", optional.isPresent());

        assertTrue("Data nasabah 001 adalah perorangan", optional.get() instanceof Perorangan);

        optional = dao.findById("002");

        assertTrue("Data nasabah 002 di temukan", optional.isPresent());

        assertTrue("Data nasabah 002 adalah badan usaha", optional.get() instanceof BadanUsaha);

        connection.close();
    }

    @Test
    public void testFindAll() throws SQLException {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = dataSource.getConnection();
        log.info("status connected");

        NasabahSingleTableDao dao = new NasabahSingleTableDao(connection);
        List<Nasabah> list = dao.findAll();
        long jumlahNasabahPerorangan = list.stream().filter(data -> data instanceof Perorangan).count();
        assertEquals("jumlah nasabah perorangan", 1, jumlahNasabahPerorangan);

        long jumlahNasabahBadanUsahah = list.stream().filter(data -> data instanceof BadanUsaha).count();
        assertEquals("jumlah nasabah badan usaha", 1, jumlahNasabahBadanUsahah);
        connection.close();
    }
}
