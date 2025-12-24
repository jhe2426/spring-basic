package hello.core.singleton;

public class StatefulService {

    // 아래와 같은 공유 필드는 스프링 빈으로 등록할 클래스에서는 선언해서는 안 된다.
    // 무조건 스프링 빈 클래스들은 무상태이어야 한다.
    private int price; // 상태를 유지하는 필드

    public void order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        this.price = price; // 여기에서 문제가 발생
    }

    public int getPrice() {
        return price;
    }

    // 위와의 방법을 사용한 클래스를 스프링 빈으로 등록하면 큰 문제가 발생하므로 아래와 같이 무상태로 코드를 작성하면 된다.
    // 스프링 빈으로 등록한 순간 디폴트로 싱글톤 패턴이므로 멤버 변수를 선언하게 되면 객체 이름이 달라도 이 객체를 호출을 하게 되면
    // 같은 인스턴스임으로 멤버 변수 값의 변경이 가능하게 되면 예기치 못한 곳에서 에러가 무조건 발생하므로
    // 싱글톤 패턴을 사용할 때에는 상태를 선언해서 사용해서는 안 됨
    public int order2(String name, int price) {
        System.out.println("name = " + name + " price" + price);
        return price;
    }

}
