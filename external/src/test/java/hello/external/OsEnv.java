package hello.external;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/*
    외부 설정 - OS 환경 변수
    - OS 환경 변수는 해당 OS를 사용하는 모든 프로그램에서 읽을 수 있는 설정 값이다.
        다른 외부 설정과 비교해서 사용 범위가 가장 넓다.
    - System.getenv()를 사용하면 전체 OS 환경 변수를 Map으로 조회할 수 있다.
    - System.getenv(key)를 사용하면 특정 OS 환경 변수의 값을 String으로 조회할 수 있다.

    - OS 환경 변수를 설정하고, 필요한 곳에서 System.getenv()를 사용하면 외부 설정을 사용할 수 있다.
    - 예를 들어서 개발 서버에서는 DBURL=dev.db.com과 같이 설정하고, 운영 서버에서는 DBURL=prod.db.com와 같이 설정하면
        System.getenv("DBURL")을 조회할 때 각각 환경에 따라서 서로 다른 값을 읽게 된다.
    - 하지만 OS 환경 변수는 이 프록램 뿐만 아니라 다른 프로그램에서도 사용할 수 있다. 전역 변수 같은 효과가 있다.
        여러 프로그램에서 사용하는 것이 맞을 때도 있지만, 해당 애플리케이션을 사용하는 자바 프로그램 안에서만 사용되는
        외부 설정값을 사용하고싶을 때도 있다. 이때에는 특정 자바 프로그램안에서 사용하는 외부 설정을 사용하면 된다.
*/
@Slf4j
public class OsEnv {

    public static void main(String[] args) {
        Map<String, String> envMap = System.getenv();
        for (String key : envMap.keySet()) {
            log.info("env {} = {}", key, System.getenv(key));
        }

        // DBURL=dev.db.com 개발 서버
        // DBURL=prod.db.com 운영 서버
//        String dburl = System.getenv("DBURL");
    }
    
}
