package hello.config;

import hello.datasource.MyDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

/*
    @Value의 단점
    - @Value로 하나하나 외부 설정 정보의 키 값을 입력받고, 주입 받아와얗 하는 부분이 번거롭다.
    - 그리고 설정 데이터를 보면 하나하나 분리되어 있는 것이 아니라 정보의 묶음으로 되어있다.
    - my.datasource 부분으로 묶여있다. 이런 부분을 객체로 변환해서 사용할 수 있다면 더 편리하고 좋을 것이다.
*/
@Slf4j
@Configuration
public class MyDataSourceValueConfig {

    @Value("${my.datasource.url}")
    private String url;
    @Value("${my.datasource.username}")
    private String username;
    @Value("${my.datasource.password}")
    private String password;

    /*
        기본값
        - 만약 키를 찾지 못할 경우 코드에서 기본값을 사용하면 다음과 같이 : 뒤에 기본값을 적어주면 된다.
         예) @Value("${my.datasource.etc.max-connection:1}): key가 설정 파일에 없는 경우 해당 key의 값은 1이 된다.
    */
    @Value("${my.datasource.etc.max-connection}")
    private int maxConnection;
    @Value("${my.datasource.etc.timeout}")
    private Duration timeout;
    @Value("${my.datasource.etc.options}")
    private List<String> options;

    @Bean
    public MyDataSource myDataSource1() {
        return new MyDataSource(url, username, password, maxConnection, timeout, options);
    }

    @Bean
    public MyDataSource myDataSource2(
            @Value("${my.datasource.url}") String url,
            @Value("${my.datasource.username}") String username,
            @Value("${my.datasource.password}") String password,
            @Value("${my.datasource.etc.max-connection}") int maxConnection,
            @Value("${my.datasource.etc.timeout}") Duration timeout,
            @Value("${my.datasource.etc.options}") List<String> options
    ) {

        return new MyDataSource(url, username, password, maxConnection, timeout, options);

    }
}
