package memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Slf4j
public class MemoryCondition implements Condition {

    /*
        Condition 인터페이스를 구현해서 다음과 같이 자바 시스템 속성이 memory=on이라고 되어 있을 때에만
            메모리 기능이 동작하도록 아래와 같이 작성
        #VM Options
        #java -Dmemory=on -jar project.jar
    */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // -Dmemory=on
        String memory = context.getEnvironment().getProperty("memory");
        log.info("memory={}", memory);
        return "on".equals(memory);
    }
}
