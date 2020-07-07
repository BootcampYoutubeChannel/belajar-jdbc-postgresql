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
import java.util.ArrayList;
import java.util.Arrays;
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
    public void testFindByIds() throws SQLException {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = dataSource.getConnection();
        log.info("status connected");
        ExampleTableDao dao = new ExampleTableDao(connection);

        List<ExampleTable> list = dao.findByIds("001", "002", "003");
        Assert.assertEquals("Jumlah data by ids", 3, list.size());
        log.info("jumlah data find by ids : {}", list);

        connection.close();
    }

    @Test
    public void testFindByListId() throws SQLException {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = dataSource.getConnection();
        log.info("status connected");
        ExampleTableDao dao = new ExampleTableDao(connection);

        List<String> strings = Arrays.asList("001", "002", "004");
        List<ExampleTable> list = dao.findByIds(strings);
        Assert.assertEquals("Jumlah data by list id", 3, list.size());
        log.info("jumlah data find by list id : {}", list);

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

    @Test
    public void testTransactionCommitRollback() {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = null;
        ExampleTable muhamadPurwadi = new ExampleTable(
                null,
                "Muhamad Purwadi",
                Date.valueOf(LocalDate.now()),
                Timestamp.valueOf(LocalDateTime.now()),
                true,
                0,
                new BigDecimal(100000),
                "test data",
                0D);
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            log.info("status connected");
            ExampleTableDao dao = new ExampleTableDao(connection);

            ExampleTable save1 = dao.save(muhamadPurwadi);
            assertNotNull("employee id not null", save1.getId());
            log.info("employee: {}", save1);

            muhamadPurwadi.setCurrency(new BigDecimal(10));
            dao.update(muhamadPurwadi);

            connection.commit();
            connection.close();
        } catch (SQLException sqle) {
            log.error("sql exception", sqle);
            if (connection != null) {
                try {
                    connection.rollback();
                    log.warn("was rollback");
                    connection.close();
                } catch (SQLException sqlRollbackException) {
                    log.error("failed rollback", sqlRollbackException);
                }
            }
        }

        Connection checkConnection = null;
        try {
            checkConnection = dataSource.getConnection();
            ExampleTableDao dao = new ExampleTableDao(checkConnection);
            Optional<ExampleTable> newDataExist = dao.findById(muhamadPurwadi.getId());
            Assert.assertTrue("Data Baru tersimpan", newDataExist.isPresent());
            if (newDataExist.isPresent()) {
                ExampleTable data = newDataExist.get();
                dao.removeById(data.getId());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testBulkUpload() {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = null;
        List<ExampleTable> list = new ArrayList<>();
        List<String> newId = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            list.add(new ExampleTable(
                    null,
                    "Hari Sapto Adi",
                    Date.valueOf(LocalDate.now()),
                    Timestamp.valueOf(LocalDateTime.now()),
                    true,
                    0,
                    new BigDecimal(100000),
                    "",
                    0D));
            list.add(new ExampleTable(
                    null,
                    "Deni Sutisna",
                    Date.valueOf(LocalDate.now()),
                    Timestamp.valueOf(LocalDateTime.now()),
                    true,
                    0,
                    new BigDecimal(100000),
                    "",
                    0D));
            ExampleTableDao dao = new ExampleTableDao(connection);
            newId = dao.save(list);


            connection.commit();
        } catch (SQLException sqle) {
            log.error("sql exception", sqle);
            if (connection != null) {
                try {
                    connection.rollback();
                    log.warn("was rollback");
                    connection.close();
                } catch (SQLException sqlRollbackException) {
                    log.error("failed rollback", sqlRollbackException);
                }
            }
        }


        Connection checkConnection = null;
        try {
            checkConnection = dataSource.getConnection();
            ExampleTableDao dao = new ExampleTableDao(checkConnection);
            List<ExampleTable> newList = dao.findByIds(newId);

            Assert.assertEquals("Data Baru tersimpan jumlahnya", 2, newList.size());
            dao.removeByListId(newId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
