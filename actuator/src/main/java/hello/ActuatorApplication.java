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

/*
    액츄에이터와 보안
    - 액츄에이터가 제공하는 기능들은 우리 애플리케이션의 내부 정보를 너무 많이 노출한다.
    - 그래서 외부 인터넷 망이 공개된 곳에 액츄에이터의 엔드포인트를 공개하는 것은 보안상 좋은 방안이 아니다.
    - 액츄에이터의 엔드포인트들은 외부 인터넷에서 접근이 불가능하게 막고, 내부에서만 접근 가능한 내부망을 사용하는 것이 안전하다.
    - 예를 들어서 외부 인터넷 망을 통해서 8080 포트에만 접근할 수 있고, 다른 포트는 내부망에서만 접근할 수 있다면 액츄에이터에 다른 포트를 설정하면 된다.
    - 액츄에이터의 기능을 애플리케이션 서버와는 다른 포트에서 실행하려면 다음과 같이 설정하면 된다.
        management.server.port=9292
    - 포트를 분리하는 것이 어렵고 외부 인터넷 망을 통해서 접근해야 한다면 /actuator 경로에 서블릿 필터 또는 스프링 시큐리티를 통해서 인증된 사용자만 접근 가능하도록 추가 개발이 필요하다.

    엔드포인트 경로 변경
    - 엔드포인트의 기본 경로를 변경하려면 다음과 같이 설정하면 된다.
        management.endpoints.web.base-path="/manage"
    - /actuator/{엔드포인트} 대신에 /manage/{엔드포인트}로 변경된다.
*/
/*
    마이크로미터
    - CPU, JVM, 커넥션 정보 등을 JMX 툴에 전달한다고 가정을 하면 각각의 정보를 JMX 모니터링 툴이 정한 포맷에 맞추어 측정하고 전달해야 한다.
    - 그런데 중간에 사용하는 모니터링 툴을 변경하면 기존에 측정했던 코드를 모두 변경한 툴에 맞도록 다시 변경해야 한다.
        개발자 입장에서는 단순히 툴 하나를 변경했을뿐인데, 측정하는 코드까지 모두 변경해야 하는 문제가 발생한다.
    - 이런 문제를 해결하는 것이 바로 마이크로미터(Micrometer)라는 라이브러리이다.

    마이크로미터 추상화
    - 마이크로미터는 애플리케이션 메트릭 파사드라고 불리는데, 애플리케이션의 메트릭(측정 지표)을 마이크로미터가 정한 표준 방법으로 모아서 제공해준다.
    - 쉽게 이야기해서 마이크로미터가 추상화를 통해서 구현체를 쉽게 갈아끼울 수 있도록 해두었다.
    - 보통은 스프링이 이런 추상화를 직접 만들어서 제공하지만, 마이크로미터라는 이미 잘 만들어진 추상화가 잇기 때문에 스프링은 이것을 활용한다.
        스프링 부트 액츄에이터는 마이크로미터를 기본으로 내장해서 사용한다.
    - 개발자는 마이크로미터가 정한 표준 방법으로 메트릭(측정 지표)를 전달하면 된다. 그리고 사용하는 모니터링 툴에 맞는 구현체를 선택하면 된다.
        이후에 모니터링 툴이 변경되어도 해당 구현체만 변경하면 된다. 애플리케이션 코드는 모니터링 툴이 변경되어도 그대로 유지할 수 있다.

    메트릭 확인하기
    - CPU, JVM, 커넥션 사용 등등 수 많은 지표들을 어떻게 수집할까?
    - 개발자가 각각의 지표를 직접 수집해서 그것을 마이크로미터가 제공하는 표준 방법에 따라 등록하면 된다.
    - 다행히도 마이크로미터는 다양한 지표 수집 기능을 이미 만들어서 제공한다. 그리고 스프링 부트 액츄에이터는 마이크로미터가 제공하는 지표 수집을 @AutoConfiguration을 통해 자동으로 등록해준다.
    - 쉽게 이야기해서 스프링 부트 액츄에이터를 사용하면 수 많은 메트릭(지표)를 편리하게 사용할 수 있다.

    metrics 엔드포인트
    - http://localhost:8080/actuator/metrics

    자세히 확인하기
    - metrics 엔드포인트는 다음과 같은 패턴을 사용해서 더 자세히 확인할 수 있다.
    - http://localhost:8080/actuator/metrics/{name}

    Tag 필터
    - availableTages를 보면 다음과 같은 항목을 확인할 수 있다.
    - tag: area, values[heep, nonheap]
    - tag: id, value[G1 Survivor Space, ...]
    - 해당 Tag를 기반으로 정보를 필터링해서 확인할 수 있다.
    - tag=KEY:VALUE과 같은 형식을 사용해야 한다.
    - http://localhost:8080/actuator/metrics/jvm.memory.used?tag=area:heap
    - http://localhost:8080/actuator/metrics/jvm.memory.used?tag=area:nonheap

    HTTP 요청수 확인
    - http://localhost:8080/actuator/metrics/http.server.requests
    - HTTP 요청수에서 일부 내용을 필터링해서 확인해보자
        - /log 요청만 필터 (사전에 /log 요청을 해야 확인할 수 있음)
        - http://localhost:8080/actuator/metrics/http.server.requests?tag=url:/log
    - /log 요청 & HTTP Status = 200
        - http://localhost:8080/actuator/metrics/http.server.request?tag=url:/log&tag=status:200
*/

/*
    다양한 메트릭
    - 마이크로미터와 액츄에이터가 기본으로 제공하는 다양한 메트릭은 아래와 같다.
        - JVM 메트릭
        - 시스템 메트릭
        - 애플리케이셔 시작 메트릭
        - 스프링 MVC 메트릭
        - 톰캣 메트릭
        - 데이터 소스 메트릭
        - 로그 메트릭
        - 기타 수 많은 메트릭이 존재
        - 사용자가 메트릭을 직접 정의하는 것도 가능

    JVM 메트릭
    - JVM 관련 메트릭을 제공한다. jvm.으로 시작한다.
        - 메모리 및 버퍼 풀 세부 정보
        - 가비지 수집 관련 통계
        - 스레드 활용
        - 로드 및 언로드된 클래스 수
        - JVM 버전 정보
        - JIT 컴파일 시간

    시스템 메트릭
    - 시스템 메트릭을 제공한다. system., process., disk. 으로 시작한다.
        - CPU 지표
        - 파일 디스크립터 메트릭
        - 가동 시간 메트릭
        - 사용 가능한 디스크 공간

    애플리케이션 시작 메트릭
    - 애플리케이션 시작 시간 메트릭을 제공한다.
        - application.started.time: 애플리케이션을 시작하는 데 걸리는 시간(ApplicationStartedEvent로 측정)
        - application.ready.time: 애플리케이션이 요청을 처리할 준비가 되는데 걸리는 시간(ApplicationReadyEvent로 측정)
    - 스프링은 내부에 여러 초기화 단계가 있고 각 단계별로 내부에서 애플리케이션 이벤트를 발행한다.
        - ApplicationStartedEvent: 스프링 컨테이너가 완전히 실행된 상태이다. 이후에 커맨드 라인 러너가 호출된다.
        - ApplicationReadyEvent: 커맨드 라인 러너가 실행된 이후에 호출된다.

    스프링 MVC 메트릭
    - 스프링 MVC 컨트롤러가 처리하는 모든 요청을 다룬다.
    - 메트릭 이름: http.server.requests
    - TAG를 사용해서 다음 정보를 분류해서 확인할 수 있다.
        - uri: 요청 URI
        - method: GET, POST 같은 HTTP 메서드
        - status: 200, 400, 500 같은 HTTP Status 코드
        - exception: 예외
        - outcome: 상태코드를 그룹으로 모아서 확인 1xx:INFORMATIONAL, 2xx:SUCCESS, 3xx:REDIRECTION, 4xx:CLIENT_ERROR, 5xx:SERVER_ERROR

    데이터소스 메트릭
    - DataSource, 커넥션 풀에 관한 메트릭을 확인할 수 있다.
    - jdbc.connections.으로 시작한다.
    - 최대 커넥션, 최소 커넥션, 활성 커넥션, 대기 커넥션 수 등을 확인할 수 있다.
    - 히카리 커넥션 풀을 사용하면 hikaricp. 를 통해 히카리 커넥션 풀의 자세한 메트릭을 확인할 수 있다.

    로그 메트릭
    - logback.events: logback 로그에 대한 메트릭을 확인할 수 있다.
    - trace, debug, info, warn, error 각각의 로그 레벨에 따른 로그 수를 확인할 수 있다.
    - 예를 들어서 error 로그 수가 급격히 높아진다면 위험한 신호로 받아드릴 수 있다.

    톰캣 메트릭
    - 톰캣 메트릭은 tomcat. 으로 시작한다.
    - 톰캣 메트릭을 모두 사용하려면 다음 옵션을 켜야한다. (옵션을 켜지 않으면 tomcat.session. 관련 정보만 노출된다.)
        server.tomcat.mbeanregistry.enabled: true
    - 톰캣의 최대 쓰레드, 사용 쓰레드 수를 포함한 다양한 메트릭을 확인할 수 있다.

    기타
    - HTTP 클라이언트 메트릭(RestTemplate, WebClient)
    - 캐시 메트릭
    - 작업 실행과 스케줄 메트릭
    - 스프링 데이터 리포지토리 메트릭
    - 몽고DB 메트릭
    - 레디스 메트릭

    사용자 정의 메트릭
    - 사용자가 직접 메트링르 정의할 수도 있다. 예를 들어서 주문수, 취소수를 메트릭으로 만들 수 있다.
    - 사용자 정의 메트릭을 만들기 위해서는 마이크로미터의 사용법을 먼저 이해해야 한다.
*/

/*
    프로메테우스와 그라파나 소개
    - 프로메테우스
        - 애플리케이션에서 발생한 메트릭을 그 순간만 확인하는 것이 아니라 과거 이력까지 함께 확인하려면 메트릭을 보관하는 DB가 필요하다.
        - 이렇게 하려면 어디선가 메트릭을 지속해서 수집하고 DB에 저장해야 한다. 프로메테우스가 바로 이런 역할을 담당한다.
    - 그라파나
        - 프로메테우스가 DB라고 하면, 이 DB에 있는 데이터를 불러서 사용자가 보기 편하게 보여주는 대시보드가 필요하다.
        - 그라파나는 매우 유연하고, 데이터를 그래프로 보여주는 툴이다. 수 많은 그래프를 제공하고, 프로메테우스를 포함한다

    전체 구조
    1. 스프링부트 액츄에이터와 마이크로미터를 사용하면 수 많은 메트릭을 자동으로 생성한다.
    - 마이크로미터 프로메테우스 구현체는 프로메테우스가 읽을 수 있는 포맷으로 메트릭을 생성한다.
    2. 프로메테우스는 이렇게 만들어진 메트릭을 지속해서 수집한다.
    3. 프로메테우스는 수집한 메트릭을 내부 DB에 저장한다.
    4. 사용자는 그라파나 대시보드 툴을 통해 그래프로 편리하게 메트릭을 조회한다. 이때 필요한 데이터는 프로메테우스를 통해서 조회한다.
*/
/*
    프로메테우스 - 기본 기능
    - 태그, 레이블: error, exception, instance, job, method, outcome, status, uri는 각각의 메트릭 정보를 구분해서 사용하기 위한 태그이다.
        마이크로미터에서는 이것을 태그(Tag)라 하고, 프로메테우스에서는 레이블(Label)이라고 한다.

    기본 기능
    - Table -> Evaluation item을 수정해서 과거 시간 조회 가능
    - Graph -> 메트릭을 그래프로 조회 가능

    필터
    - 레이블을 기준으로 필터를 사용할 수 있다. 필터는 중괄호({}) 문법을 사용한다.
    - 레이블 일치 연산자
        = 제공된 문자열과 정확히 동일한 레이블 선택
        != 제공된 문자열과 같지 않은 레이블 선택
        =~ 제공된 문자열과 정규식 일치하는 레이블 선택
        !~ 제공된 문자열과 정규식 일치하지 않는 레이블 선택

        예)
            uri=/log, method=GET 조건으로 필터
                http_server_requests_seconds_count{uri="/log", method="GET"}
            /actuator/prometheus는 제외한 조건으로 필터
                http_server_requests_seconds_count{uri!="/actuator/prometheus"}
            method가 GET, POST인 경우를 포함해서 필터
                http_server_requests_seconds_count{method=~"GET|POST"}
            /actuator로 시작하는 uri는 제외한 조건으로 필터
                http_server_requests.seconds.count{uri~"/actuator.*"}

    연산자 쿼리와 함수
    - +
    - -
    - *
    - /
    - %
    - ^

    sum: 값의 합계를 구함
    예) sum(http_server_requests_seconds_count)

    sum by
    sum by(method, status)(http_server_requests_seconds_count)
        SQL의 group by 기능과 유사하다.

   count
   count(http_server_requests_seconds_count)
   메트릭 자체의 수 카운트

   topk
   topk(3, http_server_request_seconds_count)
   상위 3개 메트릭 조회

   오프셋 수정자
   http_server_requests_seconds_count offset 10m
   offset 10m과 같이 나타낸다. 현재를 기준으로 특정 과거 시점의 데이터를 반환한다.

   범위 벡터 선택기
   http_server_requests_seconds_count[1m]
   - 마지막에 [1m], [60s]와 같이 표현한다. 지난 1분간의 모든 기록값을 선택한다.
   - 참고로 범위 백터 선택기는 차트에 바로 표현할 수 없다. 데이터로는 확인할 수 있다. 범위 벡터 선택의 결과를 차트에 표현하기 위해서는 약간의 가공이 필요
*/
/*
    프로메테우스 - 게이지와 카운터
    메트릭은 크게 보면 게이지와 카운터라는 2가지로 분류할 수 있다.

    게이지 (Gauge)
    - 임의로 오르내일 수 있는 값
    - 예) CPU 사용량, 메모리 사용량, 사용중인 커넥션

    카운터(Counter)
    - 단순하게 증가하는 단윌 누적 값
    - 예) HTTP 요청 수, 로그 발생 수

    쉽게 이야기해서 게이지는 오르락 내리락하는 값이고, 카운터는 특정 이벤트가 발생할때마다 그 수를 계속 누적하는 값이다.

    게이지(Gauge)
    예를 들어서 대표적인 게이지인 CPU 사용량(system_cpu_usage)을 생각해보자 CPU 사용량의 현재 상태를 계속 측정하고 그 값을 그대로
    그래프에 출력하면 과거부터 지금까지의 CPU 사용량을 확인할 수 있다.
    게이지는 가장 단순하고 사용하기 쉬운 메트릭이다.

    카운터(Counter)
    카운터는 단순하게 증가하는 단일 누적 값이다.
    계속 증가하는 그래프
    - http_server_requests_seconds_count(uri="/log")
    - HTTP 요청 메트릭을 그래프로 표현하면 카운터는 계속 누적해서 증가하는 값이다. 따라서 계속 증가하는 그래프만 보개 될 것이다.
        이렇게 증가만 하는 그래프에서는 특정 시간에 얼마나 고객의 요청이 들어왔는지 한눈에 확인하기 매우 어렵다. 이런 문제를 해결하기 위해 increase(), rate() 같은 함수를 지원한다.

    increase()
    - increase()를 사용하면 이런 문제를 해결할 수 있다. 지정한 시간 단위별로 증가르 확인할 수 있다.
    - 마지막에 [시간]을 사용해 범위 벡터를 선택해야 한다.
        increase(http_server_requests_seconds_count{uri="/log"}[1m])
    - 분당 얼마나 고객의 요청이 어느정도 증가했는지 한눈에 파악할 수 있다.

    rate()
    - 범위 백터에서 초당 평균 증가율을 계산한다.
    - increase()가 숫자르 직접 카운트한다면, rate()는 여기에 초당 평균을 나누어서 계산한다.
    - rate(data[1m])에서 [1m] 이라고 하면 60초가 기분이 되므로 60을 나눈 수이다.
    - rate(data[2m])에서 [2m] 이라고 하면 120초가 기준이 되므로 120을 나눈 수이다.

    irate()
    - rate와 유사한데, 범위 벡터에서 초당 순간 증가율을 계산한다. 급격하게 증가한 내용을 확인하기 좋다.

    정리
    - 게이지: 값이 계속 변하는 게이지는 현재 값을 그대로 그래프로 표현하면 된다.
    - 카운터: 값이 단조롭게 증가하는 카운터는 increase(), rate() 등을 사용해서 표현하면 된다. 이렇게 하면 카운터에서 특정 시간에 얼마나 고객의 요청이 들어왔는지 확인할 수 있다.

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
