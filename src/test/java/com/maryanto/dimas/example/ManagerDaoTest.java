package com.maryanto.dimas.example;

import com.maryanto.dimas.example.config.DatasourceConfig;
import com.maryanto.dimas.example.dao.hr.ManagerDao;
import com.maryanto.dimas.example.entity.hr.Manager;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class ManagerDaoTest extends TestCase {

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

        ManagerDao dao = new ManagerDao(connection);
        List<Manager> all = dao.findAll();
        all.forEach(ceo -> {
            log.info("ceo: {}, children-count: {}", ceo, ceo.getChildren().size());
            ceo.getChildren().forEach(cto -> {
                log.info("cto: {}, children-count: {}", cto, cto.getChildren().size());
                cto.getChildren().forEach(pm -> {
                    log.info("pm: {}, children-count: {}", pm, pm.getChildren().size());
                    pm.getChildren().forEach(prog -> {
                        log.info("prog: {}, children-count: {}", prog, prog.getChildren().size());
                    });
                });
            });
        });
    }


}
