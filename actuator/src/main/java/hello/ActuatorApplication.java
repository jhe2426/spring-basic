package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
    프로덕션 준비 기능이란?
    - 운영 환경에서 서비스할 때 필요한 기능들을 프로덕션 준비 기능이라고 함
        - 지표, 추적, 감사
        - 모니터링
    - 애플리케이션이 현재 살아있는지, 로그 정보는 정상 설정되었는지, 커넥션 풀은 얼마나 사용되고 있는지 등을 확인할 수 있어야함
    - 스프링 부트가 제공하는 엑추에이터는 이런 프로덕션 준비 기능으 매우 편리하게 사용할 수 있는 다양한 편의 기능들을 제공
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
*/
@SpringBootApplication
public class ActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActuatorApplication.class, args);
    }

}
