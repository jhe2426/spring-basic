package hello.core.lifecycle;

//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.InitializingBean;

// 초기화, 소멸 인터페이스 InitializingBean, DisposableBean의 단점
    // 1. 이 인터페이스는 스프링 전용 인터페이스이라서 이 인터페이스를 사용하는 클래스는 스프링 전용 인터페이스에 의존하게 된다.
    // 2. 초기화, 소멸 메서드의 이름을 변경할 수 없다.
    // 3. 내가 코드를 고칠 수 없는 외부 라이브러리에 적용할 수 없다.
// 인터페이스를 사용하는 초기화, 종료 방법은 스프링 초창기에 나온 방법이므로 지금은 거의 사용하지 않는다.
//public class NetworkClient implements InitializingBean, DisposableBean {

public class NetworkClient {

    private String url;

    // 왜 생성자에서 매개변수로 초기 값을 받아서 초기화하는 방법을 권장하지 않을까?
    // 생성자는 필수 정보(파라미터)를 받고, 메모리를 할당해서 객체를 생성하는 책임을 가지고
    // 초기화는 이렇게 생성된 값들을 활용해서 외부 커넥션을 연결하는 등 무거운 동작을 수행한다
    // 따라서 생성자 안에 무거운 초기화 작업을 함께하는 것보다는 객체를 생성하는 부분과 초기화 하는 부분을 명확하게 나누는 것이
    // 유지보수 관점에서 좋다. 물론 초기화 작업이 내부 값들만 약간 변경하는 정도로 단순한 경우에는 생성자에서 한 번에 다 처리하는게 더 나을 수 있다.
    // 이렇게 역할을 생성자와 초기화로 구별하면 장점이 객체는 미리 생성은 해놓고, 외부에서 요청이 오는 순간에 연결을 시켜줄 수 있는 기능을 구현할 수 있다.
        // 객체는 생성이 되어있고 어떠한 요청이 들어올때에 초기화 메서드를 호출해서 연결을 할 수 있도록 구현할 수 있다.
    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 서비스 시작 시 호출
    public void connect() {
        System.out.println("connect: " + url);
    }

    public void call(String message) {
        System.out.println("call: " + url + " message = " + message);
    }

    // 서비스 종료 시 호출
    public void disconnect() {
        System.out.println("close: " + url);
    }

    public void init() throws Exception {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }

    public void close() throws Exception {
        System.out.println("NetworkClient.close");
        disconnect();
    }

//    // afterPropertiesSet(): 이 메서드 이름의 이미는 의존관계가 끝나고 나면 바로 해당 메서드를 호출해 주겠다는 의미
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        System.out.println("NetworkClient.afterPropertiesSet");
//        connect();
//        call("초기화 연결 메시지");
//    }
//
//    // destroy(): 이 빈이 종료가 되면 호출되는 메서드
//    @Override
//    public void destroy() throws Exception {
//        System.out.println("NetworkClient.destroy");
//        disconnect();
//    }
}
