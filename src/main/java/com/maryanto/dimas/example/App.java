package com.maryanto.dimas.example;

import com.maryanto.dimas.example.config.DatasourceConfig;
import com.maryanto.dimas.example.dao.perpustakaan.*;
import com.maryanto.dimas.example.entity.perpustakaan.*;
import com.maryanto.dimas.example.service.perpustakaan.PenulisBukuService;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
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

    public static void testSaveBukuDenganPenulis() throws SQLException {
        DataSource dataSource = config.getDataSource();
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        BukuDao bukuDao = new BukuDao(connection);
        PenulisDao penulisDao = new PenulisDao(connection);
        PenulisBukuDao penulisBukuDao = new PenulisBukuDao(connection);
        PenulisBukuService service = new PenulisBukuService(bukuDao, penulisDao, penulisBukuDao);

        Buku buku = service.saveByBuku(new Buku("001"), new Penulis("003"));
        connection.commit();
        connection.close();
    }

    public static void testSavePenulis() throws SQLException {
        DataSource dataSource = config.getDataSource();
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        BukuDao bukuDao = new BukuDao(connection);
        PenulisDao penulisDao = new PenulisDao(connection);
        PenulisBukuDao penulisBukuDao = new PenulisBukuDao(connection);
        PenulisBukuService service = new PenulisBukuService(bukuDao, penulisDao, penulisBukuDao);

        service.saveByPenulis(new Penulis("001"), new Buku("001"), new Buku("002"), new Buku("003"));
        service.saveByPenulis(new Penulis("002"), new Buku("002"), new Buku("003"));

        connection.commit();
        connection.close();
    }

    public static void testSaveTransaksi() throws SQLException {
        DataSource dataSource = config.getDataSource();
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        log.info("status connected");

        AnggotaDao anggotaDao = new AnggotaDao(connection);
        Optional<Anggota> dimasMaryanto = anggotaDao.findById("001");

        BukuDao bukuDao = new BukuDao(connection);
        Optional<Buku> java1 = bukuDao.findById("001");
        Optional<Buku> java2 = bukuDao.findById("003");

        LocalDate tanggalPinjam = LocalDate.now();

        LocalDate tanggalKembali = tanggalPinjam.plusWeeks(1);

        TransaksiDao dao = new TransaksiDao(connection);
        Transaksi pinjamJava = new Transaksi(
                null,
                Date.valueOf(tanggalPinjam),
                dimasMaryanto.orElse(null),
                Arrays.asList(
                        new TransaksiDetail(
                                null,
                                null,
                                java1.orElse(null),
                                Date.valueOf(tanggalKembali),
                                null,
                                null
                        ),
                        new TransaksiDetail(
                                null,
                                null,
                                java2.orElse(null),
                                Date.valueOf(tanggalKembali),
                                null,
                                null
                        )
                )

        );

        pinjamJava = dao.save(pinjamJava);
        connection.commit();

        Optional<Transaksi> dataSaved = dao.findById(pinjamJava.getId());

        if (dataSaved.isPresent()) {
            Transaksi transaksi = dataSaved.get();
            log.info("data transksi {}", transaksi);
            log.info("data detail transaksi {}", transaksi.getDetails());

        }
        connection.close();
    }

    public static void testUpdateTransaksi() throws SQLException {
        DataSource dataSource = config.getDataSource();
        Connection connection = dataSource.getConnection();
        log.info("status connected");

        TransaksiDao dao = new TransaksiDao(connection);
        Optional<TransaksiDetail> bukuJavaByDimas = dao.findByTransactionIdAndBookId("952b0900-7841-446a-9656-3d46a613cf7c", "001");
        dao.update(bukuJavaByDimas.orElse(null));

        connection.close();

    }

    public static void testHapusTransaksi() throws SQLException {
        DataSource dataSource = config.getDataSource();
        Connection connection = dataSource.getConnection();
        log.info("status connected");

        TransaksiDao dao = new TransaksiDao(connection);
        dao.removeById("952b0900-7841-446a-9656-3d46a613cf7c");

        Optional<Transaksi> removedId = dao.findById("952b0900-7841-446a-9656-3d46a613cf7c");
        log.info("removed : {}", removedId.isPresent());
    }

    public static void main(String[] args) {
        try {
            testSavePenulis();
        } catch (SQLException throwables) {
            log.error("sql error: ", throwables);
        }
    }
}
