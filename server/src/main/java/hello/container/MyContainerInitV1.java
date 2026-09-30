package hello.container;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import java.util.Set;

/*
    서블릿은 ServletContainerInitializer라는 초기화 인터페이스를 제공한다.
    이름 그대로 서블릿 컨테이너를 초기화 하는 기능을 제공하는 인터페이스이다.
    서브릿 컨테이너는 실행 시점에 초기화 메서드인 onStartup()을 호출해준다.
    해당 메서드에서 애플리케이션에 필요한 기능들을 초기화 하거나 등록할 수 있다.

    WAS에게 실행할 초기화 클래스를 꼭 알려줘야한다.
    resources/META-INF/services 여기 경로에 jakarta.servlet.ServletContainerInitializer 파일을 생성한 뒤
    해당 파일에 hello.container.MyContainerInitV1이렇게 입력을 해주며 우리가 만든 MyContainerInitV1 클래스를 지정해주면 WAS를 실행할 때 해당 클래스를
    초기화 클래스로 인식하고 로딩 시점에 실행한다.
*/
public class MyContainerInitV1 implements ServletContainerInitializer {

    /*
        Set<Class<?>> set: 조금 더 유연한 초기화 기능을 제공한다. @HandlesTypes 애노테이션과 함께 사용한다.
        ServletContext servletContext: 서블릿 컨테이너 자체의 기능을 제공한다. 이 객체를 통해 필터나 서블릿을 등록할 수 있다.
    */
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        System.out.println("MyContainerInitV1.onStartup");
        System.out.println("MyContainerInitV1 set = " + set);
        System.out.println("MyContainerInitV1 servletContext = " + servletContext);
    }
}
