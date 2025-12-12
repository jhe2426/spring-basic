package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {

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

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
