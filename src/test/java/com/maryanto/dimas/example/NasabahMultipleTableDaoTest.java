package com.maryanto.dimas.example;

import com.maryanto.dimas.example.config.DatasourceConfig;
import com.maryanto.dimas.example.dao.bank.NasabahMultiTableDao;
import com.maryanto.dimas.example.entity.bank.BadanUsaha;
import com.maryanto.dimas.example.entity.bank.Nasabah;
import com.maryanto.dimas.example.entity.bank.NasabahImage;
import com.maryanto.dimas.example.entity.bank.Perorangan;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
public class NasabahMultipleTableDaoTest extends TestCase {

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

        NasabahMultiTableDao dao = new NasabahMultiTableDao(connection);
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

        NasabahMultiTableDao dao = new NasabahMultiTableDao(connection);
        List<Nasabah> list = dao.findAll();
        long jumlahNasabahPerorangan = list.stream().filter(data -> data instanceof Perorangan).count();
        assertEquals("jumlah nasabah perorangan", 1, jumlahNasabahPerorangan);

        long jumlahNasabahBadanUsahah = list.stream().filter(data -> data instanceof BadanUsaha).count();
        assertEquals("jumlah nasabah badan usaha", 1, jumlahNasabahBadanUsahah);
        connection.close();
    }

    @Test
    public void testTransferSaldo() throws SQLException {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = dataSource.getConnection();
        log.info("status connected");

        NasabahMultiTableDao dao = new NasabahMultiTableDao(connection);

        dao.transferSaldoNasabahPerorangToBadanUsaha(
                new Perorangan("001"),
                new BadanUsaha("002"),
                new BigDecimal("200000"));
        connection.close();
    }

    @Test
    public void testSimpanImage() throws SQLException {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = dataSource.getConnection();
        log.info("status connected");

        NasabahMultiTableDao dao = new NasabahMultiTableDao(connection);
        InputStream images = this.getClass().getResourceAsStream("/images/test.png");
        NasabahImage nasabah = new NasabahImage(new Perorangan("001"),
                "employee-image.png",
                images,
                "admin",
                Timestamp.valueOf(LocalDateTime.now()));
        dao.save(nasabah);
        connection.close();
    }

    @Test
    public void testReadImageImage() throws SQLException, IOException {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = dataSource.getConnection();
        log.info("status connected");

        NasabahMultiTableDao dao = new NasabahMultiTableDao(connection);
        Optional<NasabahImage> optional = dao.findImageById("e2d8b660-9e03-4552-bc58-2a3a9ec6bb1b");
        assertTrue("find image id", optional.isPresent());
        NasabahImage nasabah = optional.get();

        File locationFile = new File(new StringBuilder(System.getProperty("user.home"))
                .append(File.separator).append("Downloads")
                .append(File.separator).append(nasabah.getFilename()).toString());
        FileOutputStream outputStream = new FileOutputStream(locationFile);
        InputStream inputStream = nasabah.getFile();
        int bytesRead = -1;
        while ((bytesRead = inputStream.read()) != -1) {
            outputStream.write(bytesRead);
        }
        inputStream.close();
        outputStream.close();

        connection.close();
    }
}
