package com.maryanto.dimas.example;

import com.maryanto.dimas.example.config.DatasourceConfig;
import com.maryanto.dimas.example.dao.ExampleTableDao;
import com.maryanto.dimas.example.entity.ExampleTable;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

        Assert.assertFalse("Data Tidak ditemukan", optional.isPresent());

        optional = dao.findById("001");

        log.info("{}", optional.isPresent());
        Assert.assertTrue("Data dengan id 001 ditemukan", optional.isPresent());

        ExampleTable data = optional.orElse(new ExampleTable());
        Assert.assertEquals("Nama dengan id 001 adalah ", "Dimas Maryanto", data.getName());

        connection.close();
    }

    @Test
    public void testPagination() throws SQLException {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = dataSource.getConnection();
        log.info("status connected");
        ExampleTableDao dao = new ExampleTableDao(connection);

        ExampleTable params = new ExampleTable();
        params.setName("dimas");
        params.setActive(true);
        List<ExampleTable> list = dao.findAll(0L, 2L, 0L, "desc", params);

        Assert.assertEquals("Jumlah data list paginationn", 2, list.size());

        list = dao.findAll(2L, 4L, 0L, "desc", params);
        Assert.assertEquals("Jumlah data list pagination ke 2", 4, list.size());

        connection.close();
    }

    @Test
    public void testUpdateData() throws SQLException {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = dataSource.getConnection();
        log.info("status connected");
        ExampleTableDao dao = new ExampleTableDao(connection);

        ExampleTable example = new ExampleTable(
                null,
                "Gilang",
                Date.valueOf(LocalDate.now()),
                Timestamp.valueOf(LocalDateTime.now()),
                true,
                0,
                BigDecimal.ONE,
                "",
                0d
        );

        example = dao.save(example);

        log.info("data return after save: {}", example);
        Assert.assertNotNull("Example id save", example.getId());
        Optional<ExampleTable> optional = dao.findById(example.getId());
        Assert.assertTrue("Data telah di simpan", optional.isPresent());

        dao.removeById(example.getId());
        optional = dao.findById(example.getId());
        Assert.assertFalse("Data telah di hapus", optional.isPresent());

        optional = dao.findById("002");
        Assert.assertTrue("find by id 002 is present", optional.isPresent());

        example = optional.orElse(new ExampleTable());
        String oldName = "Muhamad yusuf";
        Assert.assertEquals("Nama dari id 002 adalah ", oldName, example.getName());

        example.setName("Muhamad Purwadi");
        dao.update(example);

        Optional<ExampleTable> optionalUpdated = dao.findById(example.getId());
        ExampleTable newData = optionalUpdated.orElse(new ExampleTable());

        Assert.assertEquals("Nama dari id 002 adalah ", example.getName(), newData.getName());

        example.setName(oldName);
        dao.update(example);
        connection.close();
    }
}
