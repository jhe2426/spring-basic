package hello.external;

import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/*
    외부 설정 - 자바 시스템 속성
    - 자바 시스템 속성은 실행한 JVM 안에서 접근 가능한 외부 설정이다. 추가로 자바가 내부에서 미리 설정해두고 사용하는 속성들도 있다.
    - 자바 시스템 속성은 다음과 같이 자바 프로그램을 실행할 때 사용한다.
        - 예) java -Durl=dev jar app.jar
        - -D VM 옵션을 통해서 key=value 형식을 주면 된다.
        - 순서에 주의해야 한다. -D 옵션이 -jar 보다 앞에 위치해야 한다.

    - System.getProperties()를 사용하면 Map과 유사한(Map의 자식 타입) key=value 형식의 Properties를 받을 수 있다.
        이를 통해서 모든 자바 시스템 속성을 조회할 수 있다.
    - System.getProperty(key)를 사용하면 속성값을 조회할 수 있다.

    Jar 실행
    - jar로 빌드되어 있다면 실행시 다음과 같이 자바 시스템 속성을 추가할 수 있다.
    - java -Durl-devdb -Dusername=dev_user -Dpassword=dev_pwd -jar app.jar

    자바 시스템 속성을 자바 코드로 설정하기
    - 자바 시스템 속성은 앞서 본 것 처럼 -D 옵션을 통해 실행 시점에 전달하는 것도 가능하고, 자바 코드 내부에서 추가하는 것도 가능하다.
        코드에서 추가하면 이후에 조회시에 값을 조회할 수 있다.
        - 설정: System.setProperty(propertyName, "propertyValue")
        - 조회: System.getProperty(propertyName)
    - 참고로 이 방식은 코드 안에서 사용하는 것이기 때문에 외부로 설정을 분리하는 효과는 없다.
*/
@Slf4j
public class JavaSystemProperties {

    public static void main(String[] args) {

        System.setProperty("hello_key", "hello_value");
        String helloKey = System.getProperty("hello_key");
        log.info("hello_key: {}", helloKey);

        Properties properties = System.getProperties();
        for (Object key : properties.keySet()) {
            log.info("prop {} = {}", key, System.getProperty(String.valueOf(key)));
        }

        String url = System.getProperty("url");
        String username = System.getProperty("username");
        String password = System.getProperty("password");

        log.info("url={}", url);
        log.info("username={}", username);
        log.info("password={}", password);

    }

}
