package hello.container;

import hello.spring.HelloConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/*
    Tomcat: 애플리케이션에 등록된 Servlet과 URL 매핑 정보를 관리하다가 HTTP 요청이 들어오면 해당 URL을 담당하는 Servlet을 찾아 호출한다.
    - Tomcat은 웹 서버이자 Servlet Container로서 사용자의 HTTP 요청을 받고, 해당 요청에 매핑된 Servlet에게 처리를 위임한다.
    - Servlet이 요청을 처리해서 만든 결과를 Tomcat이 HTTP 응답 형태로 사용자에게 전달한다.

    Tomcat은 HTTP 요청을 받아 URL에 매핑된 DispatcherServlet을 호출하고, DispatcherServlet은 Spring Container가 관리하는 MVC 인프라와
    Controller Bean을 이용하여 적절한 Controller를 찾아 실행한다. Spring Container는 Controller뿐 아니라 Service, Repository 등의 Bean을
    생성하고 의존관계를 연결하며 생명주기를 관리한다.
        MVC 인프라
            - 개발자가 직접 만든 Controller가 HTTP 요청을 처리할 수 있도록 주변에서 찾아주고, 호출해주고, 결과를 변환하고, 예외를 처리해주는 Spring MVC 내부 객체들의 집합

    따라서 Tomcat은 URL에 맞는 Servlet을 찾고, Spring MVC에서는 그 Servlet이 보통 DispatcherServlet이다.
    DispatcherServlet은 요청 URL과 HTTP 메서드 등에 맞는 Controller의 메서드를 찾도록 Spring MVC에 위임한다.

    스프링 컨테이너
    - Bean 생성: @Component, @Service, @Repository, @Controller, @Bean 등으로 등록된 객체를 만든다.
    - 의존관계 주입(DI): Controller -> Service -> Repository처럼 필요한 객체를 서로 연결해준다.
    - Bean 조회/관리: 필요한 Bean을 이름이나 타입 기준으로 찾아서 제공한다.
    - 생명주기 관리: Bean 생성부터 초기화, 사용, 종료까지 관리한다.
    - 스코프 관리: Singleton, Prototype, Request, Session 같은 Bean의 생성 범위를 관리한다.
    - 후처리(BeanPostProcessor): Bean 생성 과정 중간에 개입해서 객체를 가공할 수 있다.
    - AOP/프록시 적용: @Transactional, @Async, 보안 같은 기능을 사용할 때 필요한 프록시 객체를 만들어 적용한다.
    - 설정 정보 관리: @Configuration, @Bean, 컴포넌트 스캔 등을 해석해서 어떤 객체를 Bean으로 만들지 결정한다.
    따라서 스프링 컨테이너는 애플리케이션 객체를 생성하고, 서로 연결하고, 필요한 기능을 덧붙이며, 생성부터 소멸까지 관리하는 객체 관리 시스템이다.
*/
public class AppInitV2Spring implements AppInit {

    @Override
    public void onStartup(ServletContext servletContext) {
        System.out.println("AppInitV2Spring.onStartup");

        // 스프링 컨테이너 생성
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(HelloConfig.class);

        // 스프링 MVC 디스패치 서블릿 생성, 스프링 컨테이너 연결
        DispatcherServlet dispatcher = new DispatcherServlet(appContext);

        // 디스패처 서블릿을 서블릿 컨테이너에 등록 (중복된 이름으로 등록하면 에러 발생하므로 등록할 때 이름 주의하기)
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcherV2", dispatcher);

        // /spring/* 요청이 디스패처 서블릿을 통하도록 설정
        servlet.addMapping("/spring/*");
    }
}
