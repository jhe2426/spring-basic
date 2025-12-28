package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
// @ComponentScan: @Component 에노테이션이 붙은 클래스를 스캔해서 스프링 빈으로 등록한다.
    // 스프링 빈의 기본 이름은 클래스명을 사용하되 맨 앞글자만 소문자를 사용한다.
    // MemberServiceImpl 클래스 -> memberServiceImpl 이름의 빈으로 등록
    // 빈 이름을 직접 지정하고 싶으면 해당 클래스에 @Component("memberService") 이런식으로 이름을 부여하면 된다.
    // 설정:
        //  basePackages: 탐색할 패키지의 시작 위치를 지정, 지정한 위치를 포함해서 하위 패키지 모두를 탐색
        //  basePackageClasses: 지정한 클래스의 패키지를 탐색 시작 위치로 지정
        // 만약 지정하지 않는다면 디폴트로는 @ComponentScan이 붙은 설정 정보 클래스가 생성된 패키지 위치가 시작 위치가 된다.
        // 권장하는 방법은 설정 정보 클래스의 위치를 프로젝트 최상단위에 두어 디폴트로 시작 위치가 되도록 하는 것을 권장한다.
            // 이러면 패키지를 직접 지정하지 않아도 프로젝트의 최상단 위치에 클래스를 선언한 것이므로 하위의 모든 클래스를 검색할 수 있으므로
            // @Component이 붙은 모든 클래스를 빈으로 생성할 수 있기 때문이다.
@ComponentScan(
        basePackages = "hello.core",
        // 수동으로 빈을 등록하는 예제인 AppConfig 클래스는 빈으로 등록을 하지 않기 위해서 아래와 같은 설정을 줌
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
}
