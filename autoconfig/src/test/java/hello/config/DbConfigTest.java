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


/*

    @AutoConfiguration(
        after = {DataSourceAutoConfiguration.class}
    )
    @ConditionalOnClass({DataSource.class, JdbcTemplate.class})
    @ConditionalOnSingleCandidate(DataSource.class)
    @EnableConfigurationProperties({JdbcProperties.class})
    @Import({DatabaseInitializationDependencyConfigurer.class, JdbcTemplateConfiguration.class, NamedParameterJdbcTemplateConfiguration.class})
    public class JdbcTemplateAutoConfiguration {
    }

    - @AutoConfiguration: 자동 구성을 사용하려면 이 애노테이션을 등록해야 한다.
        - 자동 구성도 내부에 @Configuration이 있어서 빈을 등록하는 자바 설정 파일로 사용할 수 있다.
        - after = DataSourceAutoConfiguration.class
            - 자동 구성이 실행되는 순서를 지정할 수 있다. JdbcTemplate은 DataSource가 필요하기 때문에 DataSource를 자동으로 등록해주는 DataSourceAutoConfiguration 다음에
                실행하도록 설정되어 있다.
                DataSource: Java에서 데이터베이스에 접근하기 위한 Connection을 제공하는 인터페이스
                    - 데이터베이스 연결에 필요한 설정(URL, 사용자명, 비밀번호 등)을 활용한다.
                    - getConnection()을 호출하면 DB와 통신할 수 있는 Connection 객체를 제공한다.
                    - 커넥션 풀을 사용하는 구현체라면 기존 커넥션을 재사용하도록 관리한다.

    - @ConditionalOnClass({ DattaSource.class, JdbcTemplate.class})
        - IF문과 유사한 기능을 제공한다. 이런 클래스가 있는 경우에만 설정이 동작한다. 만약 없으면 여기 있는 설정들이 모두 무효화되고, 빈도 등록되지 않는다.
            - DattaSource.class, JdbcTemplate.class 이 두개 지정한 두 개의 클래스가 존재해야만 다른 애노테이션으로 설정한 설정들이 적용이 되게 된다.
        - @ConditionalXxx 시리즈가 있음
        - JdbcTemplate은 DataSource, JdbcTemplate라는 클래스가 있어야 동작할 수 있다.

    - @Import: 스프링에서 자바 설정을 추가할 때 사용한다.






    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(JdbcOperations.class)
    class JdbcTemplateConfiguration {

        @Bean
        @Primary
        JdbcTemplate jdbcTemplate(DataSource dataSource, JdbcProperties properties) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            JdbcProperties.Template template = properties.getTemplate();
            jdbcTemplate.setFetchSize(template.getFetchSize());
            jdbcTemplate.setMaxRows(template.getMaxRows());
            if (template.getQueryTimeout() != null) {
                jdbcTemplate.setQueryTimeout((int) template.getQueryTimeout().getSeconds());
            }
            return jdbcTemplate;
        }

    }

    - @Configuration: 자바 설정 파일로 사용된다.
    - @ConditionalOnMissingBean(JdbcOperations.class)
        - JdbcOperations 빈이 없을 때 동작한다.
        - JdbcTemplate의 부모 인터페이스가 바로 JdbcOperations이다.
        - 쉽게 이야기 해서 JdbcTemplate이 빈으로 등록되어 있지 않은 경우에만 jdbcTemplate() 메서드가 실행되면서 동작이 된다.
        - 만약 이런 기능이 없으면 내가 등록한 JdbcTemplate과 자동 구성이 등록하는 JdbcTemplate이 중복 등록되는 문제가 발생할 수 있다.
        - 보통 개발자가 직접 빈을 등록하면 개발자가 등록한 빈을 사용하고, 자동 구성은 동작하지 않는다.
    - JdbcTemplate이 몇 가지 설정을 거쳐서 빈으로 등록되는 것을 확인할 수 있다.


    자동 등록 설정
    다음과 같은 자동 구성 기능들이 다음 빈들을 등록해준다.
    - JdbcTemplateAutoConfiguration: JdbcTemplate
    - DataSourceAutoConfiguration: DataSource
    - DataSourceTransactionManagerAutoConfiguration: TransactionManger
    그래서 개발자가 직접 빈으로 등록하지 않아도 JdbcTemplate, DataSource, TransactionManager가 스프링 빈으로 등록된 것이다.

    스프링 부트가 제공하는 자동 구성
    - 스프링 부트는 수많은 자동 구성을 제공하고 spring-boot-autoconfigure에 자동 구성을 모아둔다.
    - 스프링 부트 프로젝트를 사용하면 spring-boot-autoconfigure 라이브러리는 기본적으로 사용된다.

    Auto Configuration 용어는 자동 설정, 자동 구성으로 번역되어 사용이 됨
    - 자동 설정
        - Configuration이라는 단어가 컴퓨터 용어에서는 환경 설정, 설정이라는 뜻으로 자주 사용된다. Auto Configuration은 크게 보면 빈들을 자동으로
            등록해서 스프링이 동작하는 환경을 자동으로 설정해주기 때문에 자동 설정이라는 용어로도 사용이 된다.
    - 자동 구성
        - Configuration이라는 단어는 구성, 배치라는 뜻도 있다.
            예를 들어서 컴퓨터라고 하면 CPU, 메모리등을 배치해야 컴퓨터가 동작한다. 이렇게 배치하는 것을 구성이라 한다.
            스프링도 스프링 실행에 필요한 빈들을 적절하게 배치해야 한다. 자동 구성은 스프링 실행에 필요한 빈들을 자동으로 배치해주는 것이다.
    - 자동설정, 자동 구성 두 용어 모두 맞는 말이다. 자동 설정은 넓게 사용되는 의미이고, 자동 구성은 실행에 필요한 컴포넌트 조각을 자동으로 배치한다는 더 좁은 의미에 가깝다.
    - Auto Configuration은 자동 구성이라는 단어를 주로 사용하고, 문맥에 따라서 자동 설정이라는 단어도 사용이 된다.
    - Configuration이 단독으로 사용될 때에는 설정이라는 단어로 사용이 된다.

    정리
    - 스프링부트가 제공하는 자동 구성 기능을 이해하려면 다음 두 가지 개념을 이해해야 한다.
        - @Conditional: 특정 조건에 맞을 때 설정이 동작하도록 한다.
        - @AutoConfiguration: 자동 구성이 어떻게 동작하는지 내부 원리 이해
*/
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