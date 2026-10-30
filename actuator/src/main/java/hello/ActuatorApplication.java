package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/*
    프로덕션 준비 기능이란?
    - 운영 환경에서 서비스할 때 필요한 기능들을 프로덕션 준비 기능이라고 함
        - 지표, 추적, 감사
        - 모니터링
    - 애플리케이션이 현재 살아있는지, 로그 정보는 정상 설정되었는지, 커넥션 풀은 얼마나 사용되고 있는지 등을 확인할 수 있어야함
    - 스프링 부트가 제공하는 엑추에이터는 이런 프로덕션 준비 기능을 매우 편리하게 사용할 수 있는 다양한 편의 기능들을 제공
    - 더 나아가서 마이크로미터, 프로메테우스, 그라파나 같은 모니터링 시스템과 매우 쉽게 연동할 수 있는 기능도 제공

    - 엔드포인트 설정
        1. 엔드포인트 활성화
        2. 엔드포인트 노출
    - 엔드포인트를 활성화 한다는 것은 해당 기능 자체를 사용할지 말지 on, off를 선택하는 것이다.
    - 엔드포인트를 노출하는 것은 활성화된 엔드포인트를 HTTP에 노출할지 아니면 JMX에 노출할지 선택하는 것이다.
        JMX: 실행 중인 자바 애플리케이션의 상태를 모니터링하고 관리할 수 있도록 지원하는 자바 기술
            - HTTP URL을 호출하는 대신, 자바 애플리케이션 내부에 등록된 관리 객체(MBean)에 접근해서 정보를 확인하는 방식

    - 각각의 엔드포인트는 /actuator/{엔드포인트명}과 같은 형식으로 접근할 수 있다.
        - http://localhost:8080/actuator/health: 애플리케이션 헬스 정보를 보여준다.
        - http://localhost:8080/actuator/beans: 스프링 컨테이너에 등록된 빈을 보여준다.

    - 서버의 동작을 바꾸는 요청또한 POST 요청으로 보내야 함 그래서 shutdown 엔드 포인트는 POST 요청을 해야 함 서버를 끄는 서버의 변경 작업이 발생하므로


    다양한 엔드포인트 목록
    - beans: 스프링 컨테이너에 등록된 스프링 빈을 보여준다.
    - conditions: condition을 통해서 빈을 등록할 때 평가 조건과 일치하거나 일치하지 않는 이유를 표시한다.
    - configprops: @ConfigurationProperties를 보여준다.
    - env: Environment 정보를 보여준다.
    - health: 애플리케이션 헬스 정보를 보여준다.
    - httpexchanges: HTTP 호출 응답 정보를 보여준다. HttpExchangeRepository를 구현한 빈을 별도로 등록해야 한다.
    - info: 애플리케이션 정보를 보여준다.
    - loggers: 애플리케이션 로거 설정을 보여주고 변경도 할 수 있다.
    - metrics: 애플리케이션의 메트릭 정보를 보여준다.
    - mappings: @RequestMapping 정보를 보여준다.
    - threaddump: 쓰레드 덤프를 실행해서 보여준다.
    - shutdown: 애플리케이션을 종료한다. 이 기능은 기본으로 비활성 되어 있다.

    헬스 정보
    - 헬스 정보를 사용하면 애플리케이션에 문제가 발생했을 때 문제를 빠르게 인지할 수 있다.
    - 헬스 정보는 단순히 애플리케이션이 요청에 응답을 할 수 있는지 판단하는 것을 넘어서 애플리케이션이 사용하는 데이터베이스가 응답하는지,
        디스크 사용량에는 문제가 없는지 같은 다양한 정보들을 포함해서 만들어진다.
    - 헬스 정보를 자세히 보려면 management.endpoint.health.show-details=always 옵션을 지정하면 된다.
    - 위의 옵션은 아주 자세하게 노출이되므로 그게 부담스러우면 show-details 옵션을 제거하고 show-components: always 옵션을 사용하면 된다.

    헬스 이상 상태
    - 헬스 컴포넌트 중에 하나라도 문제가 있으면 전체 상태는 DOWN이 된다.
    - 참고로 액츄에이터는 db, mongo, redis, diskspace, ping과 같은 수 많은 헬스 기능을 기본으로 제공한다.
*/
@SpringBootApplication
public class ActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActuatorApplication.class, args);
    }

    /*
        HTTP 요청 응답 기록
        - HTTP 요청과 응답의 과거 기록을 확인하고 싶다면 httpexchanges 엔드포인트를 사용하면 된다.
        - HttpExchangeRepository 인터페이스의 구현체를 빈으로 등록하면 httpexchanges 엔드포인트를 사용할 수 있다.
            (주의 해당 빈을 등록하지 않으면 httpexchanges 엔드포인트가 활성화 되지 않는다)
        - 스프링 부트는 기본으로 InMemoryHttpExchangeRepository 구현체를 제공한다.
        - 이 구현체는 최대 100개의 HTTP 요청을 제공한다. 최대 요청이 넘어가면 과거 요청을 삭제한다. setCapacity()로 최대 요청수를 변경할 수 있다.
        - 참고로 이 기능은 매우 단순하고 기능에 제한이 많기 때문에 개발 단계에서만 사용하고, 실제 운영 서비스에서는 모니터링 툴이나 핀포인트, Zipkin 같은 다른 기술을 사용하는 것이 좋다.
    */
    @Bean
    public InMemoryHttpExchangeRepository httpExchangeRepository() {
        return new InMemoryHttpExchangeRepository();
    }
}
