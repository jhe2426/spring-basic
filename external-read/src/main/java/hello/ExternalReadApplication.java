package hello;

import hello.config.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;

/*
    YAML: 스프링은 설정 데이터를 사용할 때 application.properties뿐만 아니라 application.yml이라는 형식도 지원한다.
    - YAML(YAML Ain't Markup Language)은 사람이 읽기 좋은 데이터 구조를 목표로 한다. 확장자는 yaml, yml이다. 주로 yml을 사용한다.
    - 가장 큰 특징은 사람이 읽기 좋게 계층 구조를 이룬다는 점
    - YAML은 space(공백)로 계층 구조를 만든다. space는 1칸을 사용해도 되는데, 보통 2칸을 사용한다.
        일관성 있게 사용하지 않으면 읽기 어렵거나 구조가 깨질 수 있다.
    - 구분 기호로 :를 사용한다. 만약 값이 있다면 key: value 이처럼 : 이후에 공백을 하나 넣고 값을 넣어주면 된다.
    - 스프링은 YAML의 계층 구조를 properties처럼 평평하게 만들어서 읽어들인다. 쉽게 이야기해서 application.yml가 application.properties 형태로 만들어진다.

    주의
    - application.properties, application.yml을 같이 사용하면 application.properties가 우선권을 가진다.
    - 이것을 둘이 함께 사용하는 것은 일관성이 없으므로 권장하지 않는다.
    - 참고로 실무에서는 설정 정보가 많아서 보기 편한 yml을 선호한다.

    YAML에서 프로필
    - yml은 ---dash(-) 3개를 사용해서 논리 파일을 구분한다.
    - spring.config.active.on-profile을 사용해서 프로필을 적용할 수 있다.
    - 나머지는 application.properties와 동일하다.
*/

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
@SpringBootApplication(scanBasePackages = {"hello.datasource", "hello.pay"})

/*
    @ConfigurationPropertiesScan 예시
    - @ConfigurationProperties를 하나하나 직접 등록할 때에는 @EnableConfigurationProperties를 사용한다.
    - @ConfigurationProperties를 특정 범위로 자동 등록할 떄에는 @ConfigurationPropertiesScan을 사용하면 된다.
*/
//@ConfigurationPropertiesScan
public class ExternalReadApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalReadApplication.class, args);
    }

}
