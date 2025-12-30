package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

    // 필드 주입
    // 코드를 작성함에 있어 간결하여 옛날에는 개발자들이 필드 주입 방식을 많이 사용했으나,
    // 스프링 부트를 사용하지 않고 테스트를 할 때에는 각 각의 필드에 인스턴스를 넣는 방법이 없어 테스트가 불가하여
    // 다시 setter 메서드를 만들게 되어 잘 사용하지 않게 됐다. setter 메서드를 작성해야한다는 것은 코드의 간결성 장점이 없어지는 것이므로
    // 차라리 필드 주입을 사용하지 않고 setter 주입을 사용하는 것이 낫다.
    // 단, 애플리케이션의 실제 코드와 관계 없는 테스트 코드에서는 사용해도 무관한다. 하지만 실제 서비스에서는 사용하지 않는다.
//    @Autowired private MemberRepository memberRepository;
//    @Autowired private DiscountPolicy discountPolicy;
    // 필드 주입을 스프링 부트를 사용하지 않고 외부에서 테스트 하기 위해 꼭 필요한 setter 메서드들
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
//    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
//        this.discountPolicy = discountPolicy;
//    }

    private final MemberRepository memberRepository;
    // 문제 발생
    // DIP(의존성 역전 원칙): 구체 구현에 의존하지 말고 항상 추상에 의존해야한다
    // OCP(개방-폐쇄 원칙): 기능을 확장할 때에는 코드를 열어두고 기존 코드는 수정하지 못하게 닫아둬야한다
    // OrderServiceImpl은 Discountpolicy 인터페이스 뿐만 아니라 FixDiscountPolicy인 구체 클래스도 함께 의존하고 있어 DIP 위반이다.
    // DIP를 위반했으므로 새로운 할인 정책을 적용하려고 하는 순간 FixDiscountPolicy를 RateDiscountPolicy로 변경하는 순간
    // OrderServiceImpl의 소스코드도 함께 변경이 되어 OCP를 위반하게 된다.
//    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
//    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();

    // 위의 문제 해결 방법
    private final DiscountPolicy discountPolicy;


    // setter 주입
        // 선택적으로 주입을 할 수 있음
            // @Autowired의 기본 동작은 주입할 대상이 없으면 오류가 발생하므로 @Autowired(required = false) 옵션을 지정하여
            // 주입할 대상이 없어도 오류가 발생하지 않게 되고 선택적으로 주입을 할 수 있게 된다.
        // 외부에서 강제로 호출하여 의존관계를 변경할 수 있음
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
//    @Autowired
//    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
//        this.discountPolicy = discountPolicy;
//    }

    // @Autowired: 스프링 빈으로 등록되는 클래스의 생성자가 1개이면 생략해도 의존성 주입을 자동으로 해줌
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        System.out.println("memberRepository = " + memberRepository);
        System.out.println("discountPolicy = " + discountPolicy);
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
