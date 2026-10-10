package hello.config;

import memory.MemoryCondition;
import memory.MemoryController;
import memory.MemoryFinder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
/*
    @Conditional(MemoryCondition.class)
    - 이제 MemoryConfig의 적용 여부는 @Conditional에 지정한 MemoryCondition의 조건에 따라 달라진다.
    - MemoryCondition의 matches()를 실행해보고 그 결과가 ture이면 MemoryConfig는 정상 동작한다.
        따라서 memoryController.memoryFinder가 빈으로 등록된다.
    - MemoryCondition의 실행 결과가 false이면 MemoryConfig는 무효화 된다.
        그래서 memoryController.memoryFinder 빈은 등록되지 않는다.
*/
//@Conditional(MemoryCondition.class)

/*
    @ConditionalOnProperty(name = "memory", havingValue = "on")
    - 환경 정보가 memory=on 이라는 조건에 맞으면 동작하고, 그렇지 않으면 동작하지 않는다.
    - @ConditionalOnProperty도 우리가 만든 것과 동일하게 내부에는 @Conditional을 사용한다.
        그리고 그 안에 Condition 인터페이스를 구현한 OnPropertyCondition를 가지고 있다.

    @ConditionalOnXxx
    - 스프링은 @Conditional과 관련해서 개발자가 편리하게 사용할 수 있도록 수 많은 @ConditionalOnXxx를 제공한다.
    - @ConditionalOnClass, @ConditionalOnMissingClass
        - @ConditionalOnClass: 클래스가 있는 경우 동작
        - @ConditionalOnMissingClass: 클래스가 없는 경우 동작
    - @ConditionalOnBean, @ConditionalOnMissingBean
        - @ConditionalOnBean: 빈이 등록되어 있는 경우 동작
        - @ConditionalOnMissingBean: 빈이 등록되어 있지 않는 경우 동작
    - @ConditionalOnProperty
        - 환경 정보가 있는 경우 동작한다.
    - @ConditionalOnResource
        - 리소스가 있는 경우 동작한다.
    - @ConditionalOnWebApplication, @ConditionalOnNotWebApplication
        - @ConditionalOnWebApplication: 웹 애플리케이션인 경우 동작한다.
        - @ConditionalOnNotWebApplication: 웹 애플리케이션이 아닌 경우 동작한다.
    - @ConditionalOnExpression: SpEL 표현식에 만족하는 경우 동작한다.
*/
@ConditionalOnProperty(name = "memory", havingValue = "on")
public class MemoryConfig {

    @Bean
    public MemoryController memoryController() {
        return new MemoryController(memoryFinder());
    }

    @Bean
    public MemoryFinder memoryFinder() {
        return new MemoryFinder();
    }

}
