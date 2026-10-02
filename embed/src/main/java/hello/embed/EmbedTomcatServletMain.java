package hello.embed;

import hello.servlet.HelloServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

/*
    내장 톰캣: 내장 톰캣은 쉽게 이야기해서 톰캣을 라이브러로 포함하고 자바 코드로 직접 실행하는 것이다.
    - 내장 톰캣을 사용한 덕분에 IDE에 별도의 복잡한 톰캣 설정없이 main() 메서드만 실행하면 톰캣까지 매우 편리하게 실행된다.
    참고
    - 내장 톰캣을 개발자가 직접 다룰일은 거의 없다. 스프링 부트에서 내장 톰캣 관련된 부분을 거의 대부분 자동화해서 제공하기 때문에 내장 톰캣을 깊이 있게 학습하는 것은 권장하지 않음
    - 내장 톰캣이 어떤 방식으로 동작하는지 그 원리를 대략 이해하는 정도면 충분하다.
*/
public class EmbedTomcatServletMain {

    public static void main(String[] args) throws LifecycleException {
        System.out.println("EmbedTomcatServletMain.main");
        /*
            톰캣 설정
            - 내장 톰캣을 생성하고, 톰캣이 제공하는 커넥터를 사용해서 8080포트에 연결한다.
        */
        Tomcat tomcat = new Tomcat();
        Connector connector = new Connector();
        connector.setPort(8080);
        tomcat.setConnector(connector);

        /*
            서블릿 등록
            - Context context = tomcat.addContext("", "/");
                - 톰캣에 사용할 contextPath와 docBase를 지정해야 한다. 이 부분은 크게 중요하지 않으므로 위와 같이 적용하면 됨
            - tomcat.addServlet(contextPath, servletName, 등록할 서블릿의 인스턴스)을 통해서 서블릿을 등록한다.
            - context.addServletMappingDecoded(pattern, name)을 통해서 등록한 서블릿의 경로를 매핑한다.
                - name 파라미터에는 tomcat.addServlet로 지정한 서블릿 이름을 등록해주면 됨
        */
        Context context = tomcat.addContext("", "/");
        tomcat.addServlet("", "helloServlet", new HelloServlet());
        context.addServletMappingDecoded("/hello-servlet", "helloServlet");
        tomcat.start();
    }
}
