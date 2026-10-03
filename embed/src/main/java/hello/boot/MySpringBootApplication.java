package hello.boot;

import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.*;

/*
    컴포넌트 스캔 기능이 추가된 단순한 애노테이션이다.
*/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ComponentScan
public @interface MySpringBootApplication {
}
