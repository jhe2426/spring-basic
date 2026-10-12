package memory;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/*
    @AutoConfiguration
    - 스프링 부트가 제공하는 자동 구성 기능을 적용할 때 사용하는 애노테이션
    @ConditionalOnProperty
    - memory=on이라는 환경 정보가 있을 때 라이브러리를 적용한다. (스프링 빈을 등록한다.)
    - 라이브러리를 가지고 있더라도 상황에 따라서 해당 기능을 켜고 끌 수 있게 유연한 기능을 제공한다.


    자동 구성 대상 지정
    - 스프링 부트 자동 구성을 적용하려면, 다음 파일에 자동 구성 대상을 꼭 지정해주어야 한다.
    - 폴더 위치와 파일 이름이 길기 때문에 오타가 나지 않도록 주의해야 한다.
    - 파일 생성
        - 파일 경로: src/main/resources/META-INF/spring/
        - 파일 이름: org.springframework.boot.autoconfigure.AutoConfiguration.imports
            - 파일 내부 내용
                - memory.MemoryAutoConfig
                    - 앞서 만든 자동 구성인 memory.MemoryAutoConfig를 패키지를 포함해서 지정해준다.
                    - 스프링 부트는 시작 시점에
                        - 모든 라이브러리의 org.springframework.boot.autoconfigure.AutoConfiguration.imports의 정보를 읽어서 자동
                            자동 구성으로 사용한다. 따라서 내부에 있는 MemoryAutoConfig가 자동으로 실행된다.

*/
@AutoConfiguration
@ConditionalOnProperty(name = "memory", havingValue = "on")
public class MemoryAutoConfig {

    @Bean
    public MemoryController memoryController() {
        return new MemoryController(memoryFinder());
    }

    @Bean
    public MemoryFinder memoryFinder() {
        return new MemoryFinder();
    }
}
