package hello.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/*
    외부설정 사용 - @ConfigurationProperties 시작
    - 스프링은 외부 설정의 묶음 정보를 객체로 변환하는 기능을 제공한다. 이것을 타입 안전한 속성이라고 한다.
    - 객체를 사용하면 타입을 사용할 수 있다. 따라서 실수로 잘못된 타입이 들어오는 문제도 방지할 수 있고, 객체를 통해서 활용할 수 있는 부분들이 많아진다.
    - 쉽게 이야기해서 외부 설정을 자바 코드로 관리할 수 있는 것이다. 그리고 설정 정보 그 자체도 타입을 가지게 된다.

    - 외부 설정을 주입 받을 객체를 생성한다. 그리고 각 필드를 외부 설정의 키 값에 맞추어 준비한다.
    - @ConfigurationProperties이 있으면 외부 설정 주입 받는 객체라는 뜻이다. 여기에 외부 설정 KEY의 묶음 시작점인 my.datasource를 적어준다.
    - 기본 주입 방식은 자바빈 프로퍼티 방식이다. Getter, Setter가 필요하다.

    문제
    - MyDataSourcePropertiesV1은 스프링 빈으로 등록된다. 그런데 Setter를 가지고 있기 때문에 누군가 실수로 값을 변경하는 문제가 발생할 수 있다.
    - 여기에 있는 값들은 외부 설정값을 사용해서 초기에만 설정되고, 이후에는 변경하면 안된다. 이럴 때 Setter를 제고하고 대신에 생성자를 사용하면 중간에 데이터를 변경하는
        실수를 근본적으로 방지할 수 있다.
    - 이런 문제가 없을 것 같지만, 한 번 발생하면 정말 잡기 어려운 버그가 만들어진다.
    - 대부분의 개발자가 MyDataSourcePropertiesV1의 값은 변경하면 안된다고 인지하지만, 어떤 개발자가 자신의 문제를 해결하기 위해 setter를 통해서 값을 변경하게 되면,
        애플리케이션 전체에 심각한 버그를 유발할 수 있다. 좋은 프로그램은 제약이 있는 프로그램이다.
*/
@Data
@ConfigurationProperties("my.datasource")
public class MyDataSourcePropertiesV1 {

    private String url;
    private String username;
    private String password;
    private Etc etc;

    @Data
    public static class Etc {
        private Integer maxConnection;
        private Duration timeout;
        private List<String> options = new ArrayList<>();
    }

}
