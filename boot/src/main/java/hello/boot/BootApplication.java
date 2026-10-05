package hello.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
    JAR를 푼 결과
    boot-0.0.1-SNAPSHOT.jar
    - META-INF
        - MANIFEST.MF
    - org/springframework/boot/loader
        - JarLauncher.class: 스프링 부트 main() 실행 클래스
    - BOOT-INF
        - classes: 우리가 개발한 class 파일과 리소스 파일
            - hello/boot/BootApplication.class
            - hello/boot/controller/HelloController.class
            - ....
        - lib: 외부 라이브러리
            - spring-webmvc-6.0.4.jar
            - tomcat-embed-core-10.1.5.jar
            - ...
        - classpath.idx: 외부 라이러리 경로
        - layers.idx: 스플이 부트 구조 경로
    - JAR를 푼 결과를 보면 Fat Jar가 아니라 처음보는 새로운 구조로 만들어져 있다. 심지어 jar 내부에 jar를 담아서 인식하는 것이 불가능한데,
        jar가 포함되어 있고, 인식까지 된다.
*/
@SpringBootApplication
public class BootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);
	}

}
