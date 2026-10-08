package hello.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class DbConfigTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    TransactionManager transactionManager;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void checkBean() {
        log.info("dataSource = {}", dataSource);
        log.info("transactionManager = {}", transactionManager);
        log.info("jdbcTemplate = {}", jdbcTemplate);

        /*
            우리가 등록한 JdbcTemplate, DataSource, TransactionManager를 스프링 빈으로 등록하지 않기 위해서 DbConfig의 @Configuration 에노테이션을 주석처리 했지만,
            checkBean()의 테스트가 성공적으로 처리가 되며 dataSource, transactionManager, jdbcTemplate 값이 null이 아닌 값이 들어가 있는 것을 확인해 볼 수 있다.
            이 빈들을 모두 스프링 부트가 자동으로 등록을 해주기 때문에 null이 아닌 게 된 것이다.
        */
        assertThat(dataSource).isNotNull();
        assertThat(transactionManager).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
    }

}