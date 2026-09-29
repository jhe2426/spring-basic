package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PrototypeTest {

    @Test
    void prototypeBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        System.out.println("find prototypeBean1");
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        System.out.println("find prototypeBean2");
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        System.out.println("prototypeBean1 = " + prototypeBean1);
        System.out.println("prototypeBean2 = " + prototypeBean2);
        assertThat(prototypeBean1).isNotSameAs(prototypeBean2);

        // ac.close(): 스프링 컨테이너 자체를 종료하라는 의미
        // 그래서 프로토타입 빈은 스프링 컨테이너 내부에 존재해서 관리되는 빈들이 아니므로
        // 이때 스프링 컨테이너가 종료가 되어도 프로토타입 빈들을 컨테이너는 모르고 있으므로 종료 메서드를 호출할 수가 없는 것이다.
        ac.close();

        // 만약 종료를 해줘야하면 아래와 같이 직접 해당 메서드를 호출해주면 됨
        prototypeBean1.destroy();
        prototypeBean2.destroy();
    }

    // 프로토타입 빈의 특징
        // 1. 스프링 컨테이너에 요청할 떄마다 새로 생성된다.
        // 2. 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입 그리고 초기화까지만 관여한다.
        // 3. 종료 메서드가 호출되지 않는다.
        // 4. 그래서 프로토타입 빈은 프로토타입 빈을 조회한 클라이언트가 괸리해야 한다. 종료 메서드의 호출도 클라이언트가 직접 해야한다.
    @Scope("prototype")
    static class PrototypeBean {

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init");
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}
