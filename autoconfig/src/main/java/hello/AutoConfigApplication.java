package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
    스프링 부트 자동 구성 동작 원리
    - 스프링 부트 자동 구성이 동작하는 원리는 @SpringBootApplication -> @EnableAutoConfiguration
        -> @Import(AutoConfigurationImportSelector.class) 순서로 확인할 수 있다.
    - SpringApplication.run(AutoConfigApplication.class, args);
        - run()에 보면 AutoConfigApplication.class를 넘겨주는데, 이 클래스를 설정 정보로 사용한다는 뜻이다.
        - AutoConfigApplication에는 @SpringBootApplication 애노테이션이 있는데, 여기에 중요한 설정 정보들이 들어있다.
    - @SpringBootApplication 내부에 @EnableAutoConfiguration이 존재하는데 해당 애노테이션은 이름 그대로 자동 구성을 활성화하는 기능을 제공한다.
    - @EnableAutoConfiguration 내부에는 @Import(AutoConfigurationImportSelector.class)가 존재하는데
        - @Import는 주로 스프링 설정 정보(@Configuration)를 포함할 때 사용한다.
        - 그런데 AutoConfigurationImportSelector를 열어보면 @Configuration이 아니다.

    ImportSelector
    - @Import에 설정 정보를 추가하는 방법은 2가지가 있다.
        - 정적인 방법: @Import(클래스) 이것은 정적인 방법이다. 코드에 대상이 딱 박혀 있기 때문이다. 설정으로 사용할 대상을 동적으로 변경할 수 없다.
        - 동적인 방법: @Import(ImportSelector) 코드로 프로그래밍해서 설정으로 사용할 대상을 동적으로 선택할 수 있다.
    - 정적인 방법
        - 스프링에서 다른 설정 정보를 추가하고 싶으면 다음과 같이 @Import를 사용하면 된다.
            @Configuration
            @Import({AConfig.class, BConfig.class})
            public class AppConfig {...}
    - 동적인 방법
        - 스프링은 설정 정보 대상을 동적으로 선택할 수 있는 ImportSelector 인터페이스를 제공한다.
            ImportSelector
            package org.springframework.context.annotation;
            public interface ImportSelector {
                String[] selectImports(AnnotationMetadata importingClassMetadata);
            }

    @EnableAutoConfiguration 동작 방식
        @AutoConfigurationPackage
        @Import(AutoConfigurationImportSelector.class)
        public @interface EnableAutoConfiguration {...}
        - AutoConfigurationImportSelector는 ImportSelector의 구현체이다. 따라서 설정 정보를 동적으로 선택할 수 있다.
        - 실제로 이 코드는 모든 라이브러리에 있는 다음 경로의 파일을 확인한다.
        - META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports

    스프링 부트 자동 구성이 동작하는 방식은 다음 순서로 화인할 수 있다.
        - @SpringBootApplication -> @EnableAutoConfiguration -> @Import(AutoConfigurationImportSelector.class)
            -> resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports 파일을 열어서 설정 정보 선택
        - 해당 파일의 설정 정보가 스프링 컨테이너에 등록되고 사용
*/
@SpringBootApplication
public class AutoConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoConfigApplication.class, args);
    }

}
