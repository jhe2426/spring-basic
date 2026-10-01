package hello.container;

import hello.spring.HelloConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/*
    지금까지의 과정은 서블릿 컨테이너를 초기화하기 위해 다음과 같은 복잡한 과정을 진행했음
    - ServletContainerInitializer 인터페이스를 구현해서 서블릿 컨테이너 초기화 코드를 만들었다.
    - 여기에 애플리케이션 초기화를 만들기 위해 @HandlesTypes 애노테이션을 적용했다.
    - /META-INF/services/jakarta.servlet.ServletContainerInitializer 파일에 서블릿 컨테이너 초기화 클래스 경로를 등록했다.

    스프링 MVC는 이러한 서블릿 컨테이너 초기화 작업을 이미 만들어두었다. 덕분에 개발자는 서블릿 컨테이너 초기화 과정은 생략하고, 애플리케이션 초기화 코드만 작성하면 된다.
    - 스프링이 지원하는 애플리케이션 초기화를 사용하려면 다음 인터페이스를 구현하면 된다.
        - WebApplicationInitializer

    WebApplicationInitializer 인터페이스를 구현한 부분을 제외하고는 이전의 AppInitV2Spring과 거의 같은 코드이다.
        - WebApplicationInitializer는 스프링이 이미 만들어둔 애플리케이션 초기화 인터페이스이다.
    여기서도 디스패처 서블릿을 새로 만들어서 등록하는데, 이전 코드에서는 dispatcherV2라고 했고, 여기서는 dispatcherV3라고 해주었다.
        참고로 이름이 같은 서블릿을 등록하면 오류가 발생한다.
    servlet.addMapping("/") 코드를 통해 모든 요청이 해당 서블릿을 타도록 했다.
        - 따라서 다음과 요청하면 해당 디스패처 서블릿을 통해 /hello-spring이 매핑된 컨트롤러 메서드가 호출된다.

    스프링 MVC는 어떻게 WebApplicationInitializer 인터페이스 하나로 애플리케이션 초기화가 가능하게 할까?
    - 스프링도 결국 서블릿 컨테이너에서 요구하는 부분을 모두 구현해야 한다.
    - spring-web 라이브러리를 열어보면 서블릿 컨테이너 초기화를 위한 등록 파일을 확인할 수 있다. 그리고 이곳에서 서블릿 컨테이너 초기화 클래스가 등록되어 있다.
        /META-INF/services/jakarta.servlet.ServletContainerInitializer 내부에 다음과 같이 작성되어져 있음
            org.springframework.web.SpringServletContainerInitializer
    - SpringServletContainerInitializer코드를 확인하면 다음과 같게 코드가 작성되어짐
        SpringServletContainerInitializer
            @HandlesTypes(WebApplicationInitializer.class)
            public class SpringServletContainerInitializer implements
            ServletContainerInitializer {}
        - 코드를 보면 우리가 앞서 만든 서블릿 컨테이너 초기화 코드와 비슷한 것을 확인할 수 있다.
        - @HandlesTypes의 대상이 WebApplicationInitializer이다. 그리고 이 인터페이스의 구현체를 생성하고 실행하는
            것을 확인할 수 있다.

    정리
    - 스프링 MVC도 우리가 지금까지 한 것처럼 서블릿 컨테이너 초기화 파일에 초기화 클래스를 등록해두었다. 그리고 WebApplicationInitializer 인터페이스를
        애플리케이션 초기화 인터페이스로 지정해두고, 이것을 생성해서 실행한다.
    - 따라서 스프링 MVC를 사용한다면 WebApplicationInitializer 인터페이스만 구현하면 AppInitV3SpringMvc에서 본 것처럼 편리하게 애플리케이션 초기화를 사용할 수 있다.


*/
public class AppInitV3SpringMvc implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("AppInitV3SpringMvc.onStartup");

        // 스프링 컨테이너 생성
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(HelloConfig.class);

        // 스프링 MVC 디스패치 서블릿 생성, 스프링 컨테이너 연결
        DispatcherServlet dispatcher = new DispatcherServlet(appContext);

        // 디스패처 서블릿을 서블릿 컨테이너에 등록 (중복된 이름으로 등록하면 에러 발생하므로 등록할 때 이름 주의하기)
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcherV3", dispatcher);

        // 모든 요청이 디스패처 서블릿을 통하도록 설정
        servlet.addMapping("/");
    }
}
