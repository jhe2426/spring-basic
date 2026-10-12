package hello.selector;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.*;

/*
    staticConfig()
    - staticConfig()는 스프링 컨테이너를 만들고, StaticConfig.class를 초기 설정 정보로 사용했다. 그 결과 HelloBean이 스프링 컨테이너에 잘 등록된 것을 확인할 수 있다.

    selectorConfig()
    - selectorConfig()는 SelectorConfig를 초기 설정 정보로 사용한다.
    - SelectorConfig는 @Import(HelloImportSelector.class)에서 ImportSelector의 구체적인 HelloImportSelector를 사용했다.
    - 스프링은 HelloImportSelector를 실행하고, "hello.selector.HelloConfig"라는 문자르 반환 받는다.
    - 스프링은 이 문자에 맞는 대상을 설정 정보로 사용한다. 따라서 hello.selector.HelloConfig이 설정 정보로 사용된다.
    - 그 결과 HelloBean이 스프링 컨테이너에 잘 등록된 것을 확인할 수 있다.
*/
public class ImportSelectorTest {

    @Test
    void staticConfig() {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(StaticConfig.class);
        HelloBean bean = appContext.getBean(HelloBean.class);
        assertThat(bean).isNotNull();
    }

    @Test
    void selectorConfig() {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(SelectorConfig.class);
        HelloBean bean = appContext.getBean(HelloBean.class);
        assertThat(bean).isNotNull();
    }

    @Configuration
    @Import(HelloConfig.class)
    public static class StaticConfig {
    }

    @Configuration
    @Import(HelloImportSelector.class)
    public static class SelectorConfig {

    }
}
