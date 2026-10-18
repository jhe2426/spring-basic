package hello;

import hello.config.MyDataSourceEnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/*
    설정 정보를 빈으로 등록해서 사용하기 위해 @Import(MyDataSourceEnvConfig.class)를 추가했다.
    @SpringBootApplication(scanBasePackages = "hello.datasource")
        - 예제에는 @Import로 설정 정보를 계속 변경할 예정이므로, 설정 정보를 바꾸면서 사용하기 위해 hello.config의 위치를 피해서 컴포넌트 스캔 위치를 설정했다.
        - scanBasePackages 설정을 하지 않으면 현재 위치인 hello 패키지부터 그 하위가 모두 컴포넌트 스캔이 된다.
            @Configuration을 포함하고 있는 MyDataSourceEnvConfig이 항상 컴포넌트 스캔의 대상이 된다.
*/
@Import(MyDataSourceEnvConfig.class)
@SpringBootApplication(scanBasePackages = "hello.datasource")
public class ExternalReadApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalReadApplication.class, args);
    }

}
