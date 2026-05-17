package daw2.mariomontes.crittic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CritticApplication {

    public static void main(String[] args) {
        SpringApplication.run(CritticApplication.class, args);
    }
}
