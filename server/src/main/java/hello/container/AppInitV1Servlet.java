package hello.container;

import hello.servlet.HelloServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;


/*
    서블릿을 등록하는 2가지 방법
    - @WebServlet 애노테이션
    - 프로그래밍 방식

    프로그래밍 방식을 사용하는 이유
    - @WebServlet을 사용하면 애노테이션 하나로 서브릿을 편리하게 등록할 수 있다. 하지만 애노테이션 방식을 사용하면 유연하게 변경하는 것이 어렵다.
    - 마치 하드코딩된 것 처럼 동작한다. @WebServlet(urlPatterns = "/test")의 /tes2 경로로 변경하고 싶으면 코드를 직접 변경해야 바꿀 수 있다.
    - 반면에 프로그래밍 방식은 코딩을 더 많이 해야하고 불편하지만 무한한 유연성을 제공한다.
        - /hello-servlet 경로를 상황에 따라서 바꾸어 외부 설정을 읽어서 등록할 수 있다.
        - 서블릿 자체도 특정 조건에 따라서 if문으로 분기해서 등록하거나 뺄 수 있다.
        - 서블릿을 내가 직접 생성하기 때문에 생성자에 필요한 정보를 넘길 수 있다.
*/
public class AppInitV1Servlet implements AppInit {
    @Override
    public void onStartup(ServletContext servletContext) {
        System.out.println("AppInitV1Servlet.onStartup");

        // 순수 서블릿 코드 등록
        ServletRegistration.Dynamic helloServlet = servletContext.addServlet("helloServlet", new HelloServlet());
        helloServlet.addMapping("/hello-servlet");
    }
}
