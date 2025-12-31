package hello.core;

import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
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

    // 수동으로 빈 등록하기
//    @Bean(name = "memoryMemberRepository")
//    MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//    }
    // 자동 빈 등록 vs 수동 빈 등록
    // 스프링에서는 같은 이름의 빈이 존재한다면 수동으로 빈 등록하는 것이 우선순위를 갖는다. 그래서 수동 빈이 자동 빈을 오버라이딩 한다.
    // 스프링 부트에서는 같은 이름의 빈이 존재한다면 디폴트로 에러를 발생시키도록 되어있다.
        // 개발자가 의도적으로 수동이 우선권을 가지도록 설계를 하고 작성을 하면 좋지만, 현실은 이런 빈들을 수동인지 자동인지 설정하는 것을
        // 기억을 잘 못하고 여러 설정들이 꼬여서 같은 이름의 빈등 록이 발생하는 경우가 많으므로 이러면 정말 잡기 어려운 버그가 만들어지므로
        // 수동 빈 등록과 자동 빈 등록이 충돌나면 스프링 부트에서는 어려운 버그가 만들어 지는 것을 막아주기 위해서 에러를 발생시켜준다.
        // 단, 의도적으로 충돌을 시켜 수동 빈 등록을 우선순위로 하여 등록하고 싶다면 application.properties에
        // spring.main.allow-bean-definition-overriding=true 이런 설정을 해주게 되면
        // 자동 빈, 수동 빈 등록 충돌이 발생해도 수동 빈이 자동 빈을 오버라이딩을 하게 되어 스프링 부트 실행이 정상적으로 된다.
}
