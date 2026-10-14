package hello.external;

import lombok.extern.slf4j.Slf4j;

/*
    외부 설정 - 커맨드 라인 인수
    - 커맨드 라인 인수(Command line arguments)는 애플리케이션 실행 시점에 외부 설정값을 main(args) 메서드의 args 파라미터로 전달하는 방법
    - 다음과 같이 사용한다.
        - java -jar app.jar dataA dataB
        - 필요한 데이터를 마지막 위치에 스페이스로 구분해서 전달하면 된다.(순서가 중요함) 이 경우 dataA, dataB 2개의 문자가 args에 전달된다.

    key=value 형식 입력
    - 애플리케이션을 개발할 때는 보통 kay=value 형식으로 데이터를 받는 것이 편리하다.
    - 커맨드 라인 인수를 다음과 같이 입력하고 실행하면 url=devdb username=dev_user password=dev_pw
        - 커맨드 라인 인수는 key=value 형식이 아니라 단순히 문자를 여러 개 입력 받는 형식으로 3가지 문자가 입력이 된다.
            - url=devdb
            - username=dev_user
            - password=dev_pw
                - 이것은 파싱되지 않은 통 문자이다.
                - 이 경우 개발자가 = 을 기준으로 직접 데이터를 파싱해서 key=value 형식에 맞도록 분리해야 한다.
                    그리고 형식이 배열이기 때문에 루프를 돌면서 원하는 데이터를 찾아야 하는 번거로움도 발생한다.
                - 실제 애플리케이션을 개발할 때는 주로 key=value 형식을 잦 사용하기 때문에 결국 파싱해서 Map 같은 형식으로
                    변환하도록 직접 개발해야 하는 번거로움이 있다.
*/
@Slf4j
public class CommandLineV1 {

    public static void main(String[] args) {
        for (String arg : args) {
            log.info("arg: {}", arg);
        }
    }

}
