package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
// @ComponentScan: @Component 에노테이션이 붙은 클래스를 스캔해서 스프링 빈으로 등록한다.
    // 스프링 빈의 기본 이름은 클래스명을 사용하되 맨 앞글자만 소문자를 사용한다.
    // MemberServiceImpl 클래스 -> memberServiceImpl 이름의 빈으로 등록
    // 빈 이름을 직접 지정하고 싶으면 해당 클래스에 @Component("memberService") 이런식으로 이름을 부여하면 된다.
@ComponentScan(
        // 수동으로 빈을 등록하는 예제인 AppConfig 클래스는 빈으로 등록을 하지 않기 위해서 아래와 같은 설정을 줌
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
}
