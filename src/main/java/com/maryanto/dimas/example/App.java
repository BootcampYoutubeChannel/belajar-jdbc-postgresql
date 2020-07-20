package com.maryanto.dimas.example;

import com.maryanto.dimas.example.config.DatasourceConfig;
import com.maryanto.dimas.example.dao.perpustakaan.BukuDao;
import com.maryanto.dimas.example.dao.perpustakaan.PenerbitDao;
import com.maryanto.dimas.example.entity.perpustakaan.Buku;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Hello world!
 */
@Slf4j
public class App {

    private static DatasourceConfig config;

    static {
        config = new DatasourceConfig();
    }

    public static void testHapusPenerbit() throws SQLException {
        DataSource dataSource = config.getDataSource();
        Connection connection = dataSource.getConnection();
        log.info("status connected");
        PenerbitDao dao = new PenerbitDao(connection);

        dao.removeById("001");

        BukuDao bukuDao = new BukuDao(connection);
        Optional<Buku> bukuOptional = bukuDao.findById("001");
        log.info("buku is present: {}", bukuOptional.isPresent());

        connection.close();
    }

    public static void main(String[] args) {
        try {
            testHapusPenerbit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
