package by.ostapislav.tutorhelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "by.ostapislav.tutorhelper.db.repository")
public class TutorHelperApplication {

    public static void main(String[] args) {
       var context= SpringApplication.run(TutorHelperApplication.class, args);
        System.out.println("AAAAAAAAAAAA"+ context);
    }

}
