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

/*
    설정 데이터1 - 외부 파일
    - 지금까지 학습한 OS 환경 변수, 자바 시스템 속성, 커맨드 라인 옵션 인수는 사용해야 하는 값이 늘어날수록 사용하기가 불편해진다.
    - 실무에서는 수십개의 설정값을 사용하기도 하므로 이런 값들을 프로그램을 실행할때마 입력하게 되면 번거롭고, 괸리도 어렵다.
    - 그래서 등장하는 대안으로 설정값을 파일에 넣어서 관리하는 방법이다.
        그리고 애플리케이션 로딩 시점에 해당 파일을 읽어들이면 된다.
        그 중에서도 .properties라는 파일은 key=value 형식을 사용해서 설정값을 관리하기에 아주 적합하다.

    스프링 설정 데이터
    - 개발자가 파일을 읽어서 설정값으로 사용할 수 있도록 개발을 해야겠지만, 스프링 부트는 이미 이런 부분을 다 구현해두었다.
    - 개발자는 application.properties라는 이름의 파일을 자바를 실행하는 위치에 만들어 두기만 하면 된다.
    - 그러면 스프링이 해당 파일을 읽어서 사용할 수 있는 PropertySource의 구현체를 제공한다.
    - 스프링에서는 이러한 application.properties 파일을 설정 데이터(Config data)라 한다.
    - 설정 데이터를 Environment를 통해서 조회할 수 있다.

    참고
    application.properties 대신에 yml형식의 application.yml에도 동일하게 적용된다.

    동작확인
    - ./gradlew clean build
    - build/libs로 이동
    - 해당 위치에 application.properties 파일 생성
        해당 파일에 아래의 내용 작성 후 저장
        url=dev.db.com
        username=dev_user
        password=dev_pw
    - java -jar external-0.0.1-SNAPSHOT.jar 실행
    - 이렇게 각각의 환경에 따라 설정 파일의 내용을 다르게 준비하면 된다. 덕분에 설정값의 내용이 많고 복잡해도 파일로 편리하게 관리할 수 있다.

    남은 문제
    - 외부 설정을 별도의 파일로 관리하게 되면 설정 파일 자체를 관리하기 번거로운 문제가 발생한다.
    - 서버가 10대이면 변경사항이 있을 때 10대 서버의 설정 파일을 모두 가각 변경해야 하는 불편함이 있다.
    - 설정 파일이 별도로 관리되기 때문에 설정값의 변경 이력을 확인하기 어렵다. 특히 설정값의 변경 이력이 프로젝트 코드들과
        어떻게 영향을 주고 받는지 그 이력을 같이 확인하기 어렵다.
*/

/*
    설정 데이터2 - 내부 파일 분리
    - 설정 파일을 외부에 관리하는 것은 상당히 번거로운 일이다. 설정을 변경할 때마다 서버에 들어가서 각각의 변경 사항을 수정해두어야 한다.
    - 이 문제를 해결하는 간단한 방법은 설정 파일을 프로젝트 내부에 포함해서 괸리하는 것이다. 그리고 빌드 시점에 함께 빌드되게 하는 것이다.
    - 이렇게 하면 애플리케이션을 배포할 때 설정 파일의 변경 사항도 함꼐 배포할 수 있따. jar 하나로 설정 데이터까지 포함해서 괸리하는 것이다.

    실행 시점에 내부 설정 파일 조회
    0. 프로젝트 안에 소스 코드 뿐만 아니라 각 환경에 필요한 설정 데이터도 함께 포함해서 관리한다.
        - 개발용 설정 파일: application-dev.properties
        - 운영용 설정 파일: application-prod.properties
    1. 빌드 시점에 개발, 운영 설정 파일을 모두 포함해서 빌드한다.
    2. app.jar는 개발, 운영 두 설정 파일을 모두 가지고 배포한다.
    3. 실행할 때 어떤 설정 데이터를 읽어야 할지 최소한의 구분은 필요하다.
        - 개발 환경이라면 application-dev.properties를 읽어야 하고,
        - 운영 환경이라면 application-prod.properties를 읽어야 한다.
        - 실행할 때 외부 설정을 사용해서 개발 서버는 dev라는 값을 제공하고, 운영 서버는 prod라는 값을 제공하면 된다.
            - dev 프로필이 넘어오면 application-dev.properties를 읽어서 사용한다.
            - prod 프로필이 넘어오면 application-prod.properties를 읽어서 사용한다.
    프로필
    - 스프링은 프로필이라는 개념을 지원한다.
    - spring.profiles.active 외부 설정에 값을 넣으면 해당 프로필을 사용한다고 판단한다.
    - 그리고 프로필에 따라서 다음과 같은 규칙으로 해당 프로필에 맞는 내부 파일(설정 데이터)을 조회한다.
        - application-{profile}.properties
        예)
        - spring.profiles.active=dev
            - dev 프로필이 활성화 되었다.
            - application-dev.properties를 설정 데이터로 사용한다.
        - spring.profiles.active=prod
            - prod 프로필이 활성화 되었다.
            - application-prod.properties를 설정 데이터로 사용한다.

    실행
    - IDE에서 커맨드 라인 옵션 인수 실행
        - --spring.profiles.active=dev
    - IDE에서 자바 시스템 속성 실행
        - -Dspring.profiles.active=dev
    - Jar 실행
        - ./gradlew clean build
        - build/libs로 이동
        - java -Dspring.profiles.active=dev -jar external-0.0.1-SNAPSHOT.jar
        - java -jar external-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

    남은 문제
    - 설정 파일을 각각 분리해서 관리하면 한눈에 전체가 들어오지 않는 단점이 있다.
*/

/*
    설정 데이터3 - 내부 파일 합체
    - 설정 파일을 각각 분리해서 관리하면 한눈에 전체가 들어오지 않는 단점이 있다.
    - 스프링은 이런 단점을 보완하기 위해 물리적인 하나의 파일 안에서 논리적으로 영역을 구분하는 방법을 제공한다.

    설정 데이터 - 하나의 파일로 통합
    - 기존에는 dev 환경은 application-dev.properties, prod환경은 application-prod.properties 파일이 필요했다.
    - 스프링은 하나의 application.properties 파일 안에서 논리적으로 영역을 구분하는 방법을 제공한다.
    - application.properties라는 하나의 파일 안에서 논리적으로 영역을 나눌 수 있다.
        - application.properties 구분 방법 #--- 또는 !---(dash 3)
        - application.yml 구분 방법 ---(dash 3)
    - 프로필에 따라 논리적으로 구분된 설정 데이터를 활성화하는 방법
        - spring.config.activate.on-profile에 프로필 값 지정

    주의!
    - 속성 파일 구분 기호에는 선행 공백이 없어야 하며 정확히 3개의 dash(-) 문자가 있어야 한다.
    - 구분 기호 바로 앞과 뒤의 줄은 주석 접두사가 아니어야 한다.
        ex1)
            #
            #---
        ex2)
            #---
            #
        분할 기호 위, 아래에 주석 기호 #가 존재한다면 문서가 정상적으로 읽히지 않을 수 있다.
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
