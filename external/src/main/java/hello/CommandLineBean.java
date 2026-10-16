package hello;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/*
    외부 설정 - 커맨드 라인 옵션 인수와 스프링 부트
    - 스프링 부트는 커맨드 라인을 포함해서 커맨드 라인 옵션 인수를 활용할 수 있는 ApplicationArguments를 스프링 빈으로 등록해준다.
        그리고 그 안에 입력한 커맨드 라인을 저장해둔다. 그래서 해당 빈을 주입 받으면 커맨드 라인으로 입력한 값을 어디서든 사용할 수 있다.
*/
@Slf4j
@Component
public class CommandLineBean {

    private final ApplicationArguments arguments;

    public CommandLineBean(ApplicationArguments arguments) {
        this.arguments = arguments;
    }

    @PostConstruct
    public void init() {
        log.info("source {}", List.of(arguments.getSourceArgs()));
        log.info("optionNames {}", List.of(arguments.getOptionNames()));
        Set<String> optionNames = arguments.getOptionNames();
        for (String optionName : optionNames) {
            log.info("option args {} = {}", optionName, arguments.getOptionValues(optionName));
        }

    }
}
