package by.ostapislav.tutorhelper;

import by.ostapislav.tutorhelper.db.entity.Student;
import by.ostapislav.tutorhelper.db.repository.LessonRepository;
import by.ostapislav.tutorhelper.db.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@Transactional
class TutorHelperApplicationTests {
}
