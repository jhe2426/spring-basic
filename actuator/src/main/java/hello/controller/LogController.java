package hello.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    로거
    - loggers 엔드포인트를 사용하면 로깅과 관련된 정보를 확인하고, 또 실시간으로 변경할 수 있다.
    - 로그를 별도로 설정하지 않으면 스프링 부트는 기본으로 INFO를 사용한다. loggers 엔드포인트를 사용하면 ROOT의
        configuredLevel이 INFO인 것을 확인할 수 있다. 그래서 그 하위 모두 INFO 레빌이 적용된다.
    - 우리는 hello.controller를 DEBUG로 설정했다. 그래서 해당 부분에서는 configuredLevel이 DEBUG로 설정된 것을 확인할 수 있다.
        그리고 그 하위도 DEBUG 레벨이 적용된다.
    - 다음과 같은 패턴을 사용해서 특정 로그 이름을 기준으로 조회할 수 있다.
        http://localhost:8080/actuator/loggers/{로거이름}
        ex) http://localhost:8080/actuator/loggers/hello.controller

    실시간 로그 레벨 변경
    - 개발 서버는 보통 DEBUG 로그를 사용하지만, 운영 서버는 보통 요청이 아주 많다. 따라서 로그도 너무 많이 남기 때문에 DEBUG 로그까지
        모두 출력하게 되면 성능이나 디스크에 영향을 주게 된다. 그래서 운영 서버는 중요하다고 판단되는 INFO 로그 레벨을 사용한다.
    - 그런데 서비스 운영중에 문제가 있어서 급하게 DEBUG나 TRACE 로그를 남겨서 확인하고 싶다면 어떻게 해야할까?
        - 일반적으로는 로깅 설정을 변경하고, 서버를 다시 시작해야 한다.
        - 하지만 loggers 엔드포인트를 사용하면 애플리케이션을 다시 시작하지 않고, 실시간으로 로그 레벨을 변경할 수 있다.
    - POST http://localhost:8080/actuator/loggers/hello.controller
        POST로 전달하는 내용
        {
            "configuredLevel": "TRACE"
        }
        - 요청에 성공하면 204 응답이 온다.(별도의 응답 메시지는 없다.)
    - 하지만 해당 요청으로 변경한 로그레벨은 해당 프로그램이 실행된 상태에서 메모리를 변경해서 로그 레벨을 변경한 것으로, 서버를 다시 재시작하게 되면
        기존에 환경 파일에 설정한 로그 레벨로 다시 적용이 된다.
*/
@Slf4j
@RestController
public class LogController {

    @GetMapping("/log")
    public String log() {
        log.trace("trace log");
        log.debug("debug log");
        log.info("info log");
        log.warn("warn log");
        log.error("error log");
        return "ok";
    }

}
