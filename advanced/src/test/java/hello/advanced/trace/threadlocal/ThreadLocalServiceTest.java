package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.FieldService;
import hello.advanced.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThreadLocalServiceTest {

    private ThreadLocalService service  = new ThreadLocalService();

    /*
        동시성 문제
        여러 쓰레드가 동시에 같은 인스턴스의 필드 값을 변경하면서 발생하는 문제를 동시성 문제라고 한다.
        이런 동시성 문제는 지역 변수에서는 발생하지 않는다. 지역 변수는 쓰레드마다 각각 다른 메모리 여역이 할당된다.
        동시성 문제가 발생하는 곳은 같은 인스턴스의 필드(주로 싱글톤에서 자주 발생), 또는 static 같은 공용 필드에 접근할 때 발생한다.
        동시성 문제는 값을 읽기만 하면 발생하지 않는다. 어디선가 값을 변경하기 떄문에 발생한다.
    */
    @Test
    void field() throws InterruptedException {
        log.info("main start");
        Runnable userA = () -> {
            service.logic("userA");
        };

        Runnable userB = () -> {
            service.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
//        sleep(2000); // 동시성 문제 발생 X
        sleep(100); // 동시성 문제 발생
        threadB.start();

        /*
            작업 쓰레드의 실행 결과를 확인하기 위해 메인 스레드의 종료를 지연시킴
            start()는 작업 스레드의 실행이 완료될 때까지 기다리지 않으므로, 테스트에서 작업 스레드의 실행 결과를 확인할 수 있도록
            메인 스레드를 일시 정지시킴
            서로의 작업 결과에 의존하지 않는다면 기다릴 필요가 없고, 작업 결과에 의존한다면 명시적으로 동기화하거나 작업 완료를 기다리도록 설계해야 함
        */
        sleep(3000); // 메인 쓰레드 종료 대기
        log.info("main exit");
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
