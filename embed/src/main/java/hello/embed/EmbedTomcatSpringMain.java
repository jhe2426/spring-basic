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

/*
    내장 톰캣 - 빌드와 배포 2
    - Jar파일 내부에 Jar를 포함하지 못하므로 라이브러리를 포함하기 위해서 나온 대안이 FarJar이다.
    - fat jar 또는 uber jar라고 불리는 방법이다.
    - Jar안에는 Jar를 포함할 수 없다. 하지만 클래스는 얼마든지 포함할 수 있다.
    - 라이브러리에 사용되는 Jar를 풀면 class들이 나온다. 이 class를 뽑아서 새로 만드는 jar에 포함하는 방법이다.
        이렇게 하면 수많은 라이브러리에서 나오는 class 때문에 뚱뚱한(fat) jar가 탄생한다. 그래서 Fat Jar라고 부르는 것이다.

    - build.gradle - buildFatJar
        task buildFatJar(type: Jar) {
            manifest {
                attributes 'Main-Class': 'hello.embed.EmbedTomcatSpringMain'
            }
            duplicatesStrategy = DuplicatesStrategy.WARN
            from { configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) } }
            with jar
        }

    - jar 빌드
        ./gradlew clean buildFatJar

    - jar 파일 실행
        - jar 파일이 있는 폴더로 이동한 후 java -jar embed-0.0.1-SNAPSHOT.jar

    - Fat Jar 압축 풀기
        - Jar를 풀어보면 우리가 만든 클래스를 포함해서 수 많은 라이브러리에서 제공되는 클래스들이 포함되어 있는 것을 확인할 수 있다.

    - Fat Jar의 장점
        - Far Jar 덕분에 하나의 jar 파일에 필요한 라이브러리들을 내장할 수 있게 되었다.
        - 내장 톰캣 라이브러리를 jar 내부에 내장할 수 있게 되었다.
        - 덕분에 하나의 jar 파일로 배포부터, 웹 서버 설치 + 실행까지 모든 것을 단순화할 수 있다.

    - WAR 단점과 해결
        - 톰캣 같은 WAS를 별도로 설치해야 한다.
            - 해결: WAS를 별도로 설치하지 않아도 된다. 톰캣 같은 WAS가 라이브러리로 jar 내부에 포함되어 있다.
        - 개발 환경 설정이 복잡하다.
            - 단순한 자바라면 별도의 설정을 고민하지 않고, main() 메서드만 실행하면 된다.
            - 웹 애플리케이션은 WAS를 연동하기 위한 복잡한 설정이 들어간다.
            - 해결: IDE에 복잡한 WAS 설정이 필요하지 않다. 단순히 main() 메서드만 실행하면 된다.
        - 배포 과정이 복잡하다. WAR를 만들고 이것을 또 WAS에 전달해서 배포해야 한다.
            - 해결: 배포 과정이 단순하다. JAR를 만들고 이것을 원하는 위치에서 실행만 하면 된다.
        - 톰캣의 버전을 업데이트 하려면 톰캣을 다시 설치해야 한다.
            - 해결: gradle에 내장 톰캣 라이브러리 버전만 변경하고 빌드 후 실행하면 된다.

    - Fat Jar의 단점
        - 어떤 라이브러리가 포함되어 있는지 확인하기 어렵다.
            - 모두 class로 풀려있으니 어떤 라이브러리가 사용되고 있는지 추적하기 어렵다.
        - 파일명 중복을 해결할 수 없다.
            - 클래스나 리소스 명이 같은 경우 하나를 포기해야 한다. 이것은 심각한 문제를 발생한다.
            - META-INF/services/jakarta.servlet.ServletContainerInitializer이 파일이 여러 라이브러리(jar)에 있을 수 있다.
            - A 라이브러리와 B라이브러리 둘다 해당 파일을 사용해서 서블릿 컨테이너 초기화를 시도한다. 둘다 해당 파일을 jar안에 포함한다.
            - Fat Jar를 만들면 파일명이 같으므로 A, B 라이브러리 둘다 가지고 있는 파일 중에 하나의 파일만 선택된다.
                결과적으로 나머지 하나는 포함되지 않으므로 정상 동작하지 않는다.
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
