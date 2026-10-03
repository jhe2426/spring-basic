package hello.embed;

import hello.spring.HelloConfig;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/*
    내장 톰캣 - 빌드와 배포 1
    - 자바의 main() 메서드를 실행하기 위해서는 jar형식으로 빌드를 해야함
    - jar안에는 META-INF/MANIFEST.MF 파일에 실행할 main() 메서드의 클래스를 지정해주어야 함
        - Gradle의 도움을 받으면 이 과정을 쉽게 진행할 수 있음
            build.gradle
                task buildJar(type: Jar) {
                    manifest {
                        attributes 'Main-Class': 'hello.embed.EmbedTomcatSpringMain'
                    }
                    with jar
                }
    - jar 빌드
        - ./gradlew clean buildJar
            - build/libs/embed-0.0.1-SNAPSHOT.jar 다음 위치에 jar라는 파일이 만들어짐

    - jar 파일 실행
        - jar 파일이 있는 폴더로 이동한 후에 다음 명령어로 jar 파일을 실행
        - java -jar embed-0.0.1-SNAPSHOT.jar
        - 실행 결과를 보면 기대했던 내장 톰캣 서버가 실행되는 것이 아니라, 오류가 발생함
        - 오류 메시지를 읽어보면 스프링 관련 클래스를 찾을 수 없다는 오류가 발생됐음

    - jar 파일 실행 시 왜 스프링 관련 클래스를 찾을 수 없다는 오류가 발생했을까?
        - jar 파일의 압축을 풀어보기
            - build/libs 폴더로 이동
            - jar -xvf embed-0.0.1-SNAPSHOT.jar
        - JAR를 푼 결과
            - META-INF
                - MANIFEST.MF
            - hello
                - servlet
                    - HelloServlet.class
                - embed
                    - EmbedTomcatSpringMain.class
                    - EmbedTomcatServletMain.class
                - spring
                    - HelloConfig.class
                    - HelloController.class
        - JAR를 푼 결과를 보면 스프링 라이브러리나 내장 톰캣 라이브러리가 전혀 보이지 않는다. 따라서 해당 오류가 발생한 것이다.

    - 과거 WAR는 분명 내부에 라이브러리 역할을 하는 jar파일을 포함하고 있었다.
        - WAR를 푼 결과
            - WEB-INF
                - classes
                    - hello/servlet/TestServlet.class
                - lib
                    - jakarta.servlet-api-6.0.0.jar
            - index.html

    jar의 사용 용도
    1. main() 메서드를 넣어서 실행 용도로 사용
    2. main() 메서드 없이 다른데에서 import해서 사용하라고 라이브러리로 제공하는 용도

    - jar 파일은 jar 파일을 포함할 수가 없다.
        - WAR와 다르게 jar 파일은 내부에 라이브러리 역할을 하는 jar 파일을 포함할 수 없다. 포함한다고 해도 인식이 안 된다.
            이것이 jar 파일 스펙의 한계이다. 그렇다고 WAR를 사용할 수도 없다. WAR는 웹 애플리케이션 서버(WAS) 위에서만 실행할 수 있다.
        - 대안으로 라이브러리 jar 파일을 모두 구해서 MANIFEST 파일에 해당 경로를 적어주면 인식이 되지만 매우 번거롭고, jar 파일안에
            jar 파일을 포함할 수 없기 때문에 라이브러리 역할을 하는 jar 파일도 항상 함께 가지고 다녀야한다.
            이 방법은 권장하지 않음
*/
public class EmbedTomcatSpringMain {

    /*
        main() 메서드를 실행하면 다음과 같이 동작한다.
        - 내장 톰캣을 생성해서 8080 포트로 연결하도록 설정한다.
        - 스프링 컨테이너를 만들고 필요한 빈을 등록한다.
        - 스프링 MVC 디스패처 서블릿을 만들고 앞서 만든 스프링 컨테이너에 연결한다.
        - 디스패처 서블릿을 내장 톰캣에 등록한다.
        - 내장 톰캣을 실행한다.
    */
    public static void main(String[] args) throws LifecycleException {
        System.out.println("EmbedTomcatSpringMain.main");

        /*
            톰캣 설정
            - 내장 톰캣을 생성하고, 톰캣이 제공하는 커넥터를 사용해서 8080포트에 연결한다.
        */
        Tomcat tomcat = new Tomcat();
        Connector connector = new Connector();
        connector.setPort(8080);
        tomcat.setConnector(connector);

        // 스프링 컨테이너 생성
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(HelloConfig.class);

        // 스프링 MVC 디스패처 서블릿 생성, 스프링 컨테이너 연결
        DispatcherServlet dispatcher = new DispatcherServlet(appContext);

        // 디스패처 서블릿 등록
        Context context = tomcat.addContext("", "/");
        tomcat.addServlet("", "dispatcher", dispatcher);
        context.addServletMappingDecoded("/", "dispatcher");

        tomcat.start();
    }
}
