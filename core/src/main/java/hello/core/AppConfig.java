package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration: 이 어노테이션을 통해 해당 클래스는 스프링 컨테이너의 설정 파일임을 선언한다.
// @Bean: 해당 어노테이션으로 작성되어진 메서드의 이름이 키 값인 빈 이름이 되고 반환되는 값이 해당 빈의 객체의 값이 되게 된다.
@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    // 아래의 코드에서 static 제어자를 붙이면 해당 메서드는 객체 인스턴스가 아니라 클래스 수준 메서드가 되므로
    // 프록시가 해당 메서드 호출을 가로채서 생성된 인스턴스가 있으면 그 인스턴스를 반환해주는 것을 하지 못하게 된다.
    // 그래서 @Configuration의 싱글톤 패턴이 깨지게 된다.
//    @Bean
//    public static MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//    }

    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }

}
