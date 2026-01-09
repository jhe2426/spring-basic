package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.Assertions.*;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        assertThat(prototypeBean2.getCount()).isEqualTo(1);
    }

    @Test
    void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(2);

    }

    // 지금 아래의 코드는 싱글톤 빈에서 프로토타입 빈을 새로운 인스턴스의 값을 같도록 작성을 한 코드인데
    // 지금의 문제점은 클라이언트(ClientBean)가 ApplicationContext 스프링 컨테이너를 직접 의존해서 코드를 작성해야하는 것이 지저분한 방법이라서 문제가 된다.
    // 스프링에 너무 의존적이라서
    @Scope("singleton")
    static class ClientBean {

        // ApplicationContext: 스프링 컨테이너 자체이면서, 컨테이너에 등록된 빈 인스턴스와 빈 정의를 조회, 관리할 수 있음
        @Autowired
        ApplicationContext applicationContext;

        public int logic() {
            // 프로토타입 빈은 스프링 컨테이너에서 해당 빈의 인스턴스 값을 저장하지 않고 프로토타입 빈의 정의만 저장하고 있다.
            // 따라서 아래와 같이 컨테이너에 프로토타입 빈을 가져와라는 코드를 작성하면 컨테이너에는 해당 인스턴스를 저장하고 있지 않으므로
            // 항상 아래의 코드를 실행할 때마다 컨테이너가 저장하고 있는 프로토타입 빈의 정의를 가지고 새로운 인스턴스를 생성한 뒤 반환해주게 된다.
            PrototypeBean prototypeBean = applicationContext.getBean(PrototypeBean.class);
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
    }

      // 스프링은 일반적으로 싱글톤 빈을 사용하므로, 싱글톤 빈이 프로토타입 빈을 사용하게 되는데 싱글톤 빈은 생성 시점에만 의존관계를 주입받기 때문에,
      // 프로토타입 빈이 새로 생성되는 하지만 싱글톤 빈에 의존관계를 주입하게 되면 싱글톤 빈과 함께 계속 유지되는 것이 문제이다.
      // 참고: 프로토타입 빈은 싱글톤 빈과 함께 사용될 때에는 각 각 다른 싱글톤 빈에 의존관계가 주입될 때마다 새로운 프로토 타입 빈의 인스턴스가 생성이 된다.
      // 예를 들어 A 싱글톤 빈과 B 싱글톤 빈에 C라는 프로토타입 빈을 각 각 의존관계로 주입을 받는다고 하면 A 싱글톤 빈에는 인스턴스가 001인 프로토타입 빈을 주입받고
      // B 싱글톤 빈에는 002인 프로토타입 빈을 주입받는다. 주입 받을 때 새로운 프로토타입 빈으로 주입을 받지만, 우리가 프로토타입 빈을 사용하는 것은 호출할 때마다 새로운 인스턴스를
      // 가지는 프로토타입 빈을 사용하기 위해서 사용하는 것이므로 우리가 원했던 방향으로 실행이 되는 것이 아니게 된다.
//    @Scope("singleton")
//    static class ClientBean {
          // 싱글톤 빈에 프로토타입 빈을 주입하게 되면 프로토타입 빈은 싱글톤 빈이 생성될 때 같이 한 번 생성되어 주입되어 있는 상태를 유지하고 있으므로
          // getBean으로 여러번 요청해서 각 각의 싱글톤 빈으로 해당 프로토타입 빈의 인스턴스 값을 조회하게 되면 전부 같은 인스턴스 값을 가지고 있다.
          // getBean으로 여러번 요청하는 것 하나의 싱글톤 빈을 여러번 조회하는 것이므로 조회한 싱글톤 빈의 인스턴스 값은 전부 다 같다.
//        private final PrototypeBean prototypeBean; // 생성시점에 최초로 한 번만 생성되어 주입되어 같은 인스턴스를 계속 사용하게 됨
//
//        @Autowired
//        public ClientBean(PrototypeBean prototypeBean) {
//            this.prototypeBean = prototypeBean;
//        }
//
//        public int logic() {
//            prototypeBean.addCount();
//            int count = prototypeBean.getCount();
//            return count;
//        }
//    }

    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init " + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}
