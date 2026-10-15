package hello;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/*
    외부 설정 - 스프링 통합
    - 지금까지 살펴본, 커맨드 라인 옵션 인수, 자바 시스템 속성, OS 환경변수는 모두 외부 설정을 key=value 형식으로 사용할 수 있는 방법이다.
    - 그런데 이 외부 설정값을 읽어서 사용하는 개발자 입장에서 단순하게 생각해보면, 모두 key=value 형식이고, 설정값을 외부로 뽑아둔 것이다.
    - 그런데 어디에 있는 외부 설정값을 읽어야 하는지에 따라서 각각 읽는 방법이 다르다는 단점이 있다.
    - 예를 들어서 OS 환경 변수에 두면 System.getenv(key)를 사용해야 하고, 자바 시스템 속성을 사용하면 System.getProperty(key)를 사용해야 한다.
        만약 OS에 환경 변수를 두었는데, 이후에 정책이 변경되어서 자바 시스템 속성에 환경 변수를 두기로 했다고 가정하면 해당 코드들을 모두 변경해야 하는 번거로움이 발생한다.
    - 외부 설정값이 어디에 위치하든 상관없이 일관성 있고, 편리하게 key=value 형식의 외부 설정값을 읽을 수 있므면 사용하는 개발자 입장에서 더 편리하고 외부 설정값을 설정하는 방법도
        더 유연해질 수 있다. 예를 들어 외부 설정 값을 OS 환경변수를 사용하다가 자바 시스템 속성으로 변경하는 경우에 소수코드를 다시 빌드하지 않고 그대로 사용할 수 있다.
    - 스프링은 이 문제를 Environment와 PropertySource라는 추상화를 통해서 해결한다.

    PropertySource
    - org.springframework.core.env.PropertySource
    - 스프링은 PropertySource라는 추상 클래스를 제공하고, 각각의 외부 설정을 조회하는 XxxPropertySource 구현체를 만들어두었다.
        예)
        CommandLinePropertySource
        SystemEnvironmentPropertySource
        커맨드 라인 옵션 인수
        자바 시스템 속성
        OS 환경변수
        설정 데이터 (파일)
            application.properties, application.yml도 PropertySource에 추가된다.
    - 스프링은 로딩 시점에 필요한 PropertySource들을 생성하고, Environment에서 사용할 수 있게 연결해둔다.

    Environment
    - org.springframework.core.env.Environment
    - Environment를 통해서 특정 외부 설정에 종속되지 않고, 일관성 있게 key=value 형식의 외부 설정에 접근할 수 있다.
        - environment.getProperty(key)를 통해서 값을 조회할 수 있다.
        - Environment는 내부에서 여러 과정을 거쳐서 PropertySource들에 접근한다.
        - 같은 값이 있을 경우를 대비해서 스프링은 미리 우선순위를 정해두었다.
    - 모든 외부 설정은 이제 Environment를 통해서 조회하면 된다.

    정리
    - 스프링은 Environment를 통해서 외부 설정을 읽는 방법을 추상화했다. 덕분에 자바 시스템 속성을 사용하다가 만약 커맨드 라인 옵션 인수를 사용하도록
        읽는 방법이 변경되어도, 개발 소스 코드는 전혀 변경하지 않아도 된다.

    우선 순위
    - 예를 들어서 커맨드 라인 옵션 인수와 자바 시스템 속성을 다음과 같이 중복해서 설정하면 어떻게 될까?
    - 커맨드 라인 옵션 인수 실행
        - --url-proddb --username=prod_user --password=prod_pw
    - 자바 시스템 속성 실행
        - -Durl=devdb -Dusername=dev_user -Dpassword=dev_pw
    - 우선 순위는 딱 2가지만 기억하면 된다.
        - 더 유연한 것이 우선권을 가진다. (변경하기 어려운 파일보다 실행 시 원하는 값을 줄 수 있는 자바 시스템 속성이 더 우선권을 가진다.)
            - 변경하기 쉬운 설정에 더 높은 우선순위를 부여하면 기존 설정을 수정하지 않고도 상황에 맞게 값을 덮어쓸 수 있기 때문
            - 수정하기 어렵다: 값을 변경하는 데 많은 작업이나 비용이 필요하다는 의미
            
        - 범위가 넓은 것보다 좁은 것이 우선권을 가진다. (자바 시스템 속성은 해당 JVM 안에서 모두 접근할 수 있다.
            반면에 커맨드 라인 옵션 인수는 main의 arg를 통해서 들어오기 때문에 접근 범위가 좁다.)
    - 자바 시스템 속성과 커맨드 라인 옵션 인수의 경우 커맨드 라인 옵션 인수의 범위가 더 좁기 때문에 커맨드 라인 옵션 인수가 우선권을 가진다.
*/
@Slf4j
@Component
public class EnvironmentCheck {

    private final Environment env;

    public EnvironmentCheck(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void init() {
        String url = env.getProperty("url");
        String username = env.getProperty("username");
        String password = env.getProperty("password");
        log.info("env url={}", url);
        log.info("env username={}", username);
        log.info("env password={}", password);
    }
}
