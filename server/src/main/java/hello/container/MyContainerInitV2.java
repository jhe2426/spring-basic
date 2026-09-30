package hello.container;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/*
    애플리케이션 초기화 과정
    1. @HandlesTypes 애노테이션에 애플리케이션 초기화 인터페이스를 지정한다.
        - 여기서는 앞서 만든 AppInit.class 인터페이스를 지정했음
    2. 서블릿 컨테이너 초기화(ServletContainerInitializer)는 파라미터로 넘어오는 Set<Class<?>> set에 애플리케이션 초기화 인터페이스의 구현체들을 모두 찾아서 클래스 정보로 전달한다.
        - 여기서는 @HandlesTypes(AppInit.class)를 지정했으므로 AppInit.class의 구현체인 AppInitV1Servlet.class 정보가 전달된다.
        - 참고로 객체 인스턴스가 아니라 클래스 정보를 전달하기 때문에 실행하려면 객체를 생성해서 사용해야 한다.
    3. appInitClass.getDeclaredConstructor().newInstance()
        - 리플렉션을 사용해서 객체를 생성한다. 이 코드는 new AppInitV1Servlet()과 같다
            - 리플렉션(Reflection): 프로그램이 자기 자신의 클래스 구조를 실행 중에 들여다보는 기능
                실행 중에 어떤 클래스에 대해 필드, 메서드, 생성자, 애노테이션 같은 구조 정보를 조회하는 기능
    4. appInit.onStartup(servletContext)
        - 애플리케이션 초기화 코드를 직접 실행하면서 서블릿 컨테이너 정보가 담긴 servletContext도 함께 전달한다.

    초기화는 다음 순서로 진행된다.
    1. 서블릿 컨테이너 초기화 실행
        - resources/META-INF/services/jakarta.servlet.ServletContainerInitializer
    2. 애플리케이션 초기화 실행
        - @HandlesTypes(AppInit.class)

    서블릿 컨테이너 초기화만 있어도 될 것 같은데, 왜 이렇게 복잡하게 애플리케이션 초기화라는 개념을 만들었을까?
    - 편리함
        - 서블릿 컨테이너를 초기화 하려면 ServletContainerInitializer 인터페이스를 구현한 코드를 만들어야 한다. 여기에 추가로
            resources/META-INF/services/jakarta.servlet.ServletContainerInitializer 파일에 해당 코드를 직접 지정해주어야 한다.
        - 반면에 애플리케이션 초기화는 특정 인터페이스만 구현하면 된다.
            - 특정 인터페이스만 구현해서 확장을 편리하게 할 수 있음
    - 의존성
        - 애플리케이션 초기화는 서블릿 컨테이너에 상관없이 원하는 모양으로 인터페이스를 만들 수 있다. 이를 통해 애플리케이션 초기화 코드가 서블릿 컨테이너에 대한 의존을 줄일 수 있다.
            특히 ServletContext servletContext가 필요없는 애플리케이션 초기화 코드라면 의존을 완전히 제거할 수도 있다.
            지금은 AppInitAppInit 클래스에 onStartup()메서드에 ServletContext를 매개변수로 받도록 구현했지만, 해당 매개변수를 받지 않도록도 작성할 수 있다라는 의미이다.
*/
@HandlesTypes(AppInit.class)
public class MyContainerInitV2 implements ServletContainerInitializer {

    /*
        애플리케이션 초기화를 진행하려면 먼저 인터페이스를 만들어야 한다. 내용과 형식은 상관없고, 인터페이스는 꼭 필요하다.
        그래서 예제를 위해 AppInit 인터페이스를 생성함

        @HandlesTypes(AppInit.class)이렇게 선언을 해주면 AppInit 구현체들이 set 매개변수에 담겨온다.
    */
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        System.out.println("MyContainerInitV2.onStartup");
        System.out.println("MyContainerInitV2 set = " + set);
        System.out.println("MyContainerInitV2 servletContext = " + servletContext);

        // class hello.container.AppInitV1Servlet 인스턴스가 넘어오는 것이 아니라 클래스 메타 정보만 넘어 옴
        for (Class<?> appInitClass : set) {
            try {
                // 클래스 메타 정보만 넘어오기 때문에 해당 클래스를 사용하기 위해서 인스턴스로 만들기 위해서 아래와 같이 코드를 작성함
                AppInit appInit = (AppInit) appInitClass.getDeclaredConstructor().newInstance();// = new AppInitV1Servlet()와 같은 코드
                appInit.onStartup(servletContext);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
