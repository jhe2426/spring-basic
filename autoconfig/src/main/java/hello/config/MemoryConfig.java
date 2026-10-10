package hello.config;

import memory.MemoryCondition;
import memory.MemoryController;
import memory.MemoryFinder;
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
@Conditional(MemoryCondition.class)
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
