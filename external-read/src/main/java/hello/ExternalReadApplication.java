package hello;

import hello.config.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;

/*
    설정 정보를 빈으로 등록해서 사용하기 위해 @Import(MyDataSourceEnvConfig.class)를 추가했다.
    @SpringBootApplication(scanBasePackages = "hello.datasource")
        - 예제에는 @Import로 설정 정보를 계속 변경할 예정이므로, 설정 정보를 바꾸면서 사용하기 위해 hello.config의 위치를 피해서 컴포넌트 스캔 위치를 설정했다.
        - scanBasePackages 설정을 하지 않으면 현재 위치인 hello 패키지부터 그 하위가 모두 컴포넌트 스캔이 된다.
            @Configuration을 포함하고 있는 MyDataSourceEnvConfig이 항상 컴포넌트 스캔의 대상이 된다.
*/
//@Import(MyDataSourceEnvConfig.class)
//@Import(MyDataSourceValueConfig.class)
//@Import(MyDataSourceConfigV1.class)
//@Import(MyDataSourceConfigV2.class)
@Import(MyDataSourceConfigV3.class)
@SpringBootApplication(scanBasePackages = "hello.datasource")

/*
    @ConfigurationPropertiesScan 예시
    - @ConfigurationProperties를 하나하나 직접 등록할 때에는 @EnableConfigurationProperties를 사용한다.
    - @ConfigurationProperties를 특정 범위로 자동 등록할 떄에는 @ConfigurationPropertiesScan을 사용하면 된다.
*/
@ConfigurationPropertiesScan
public class ExternalReadApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalReadApplication.class, args);
    }

}
