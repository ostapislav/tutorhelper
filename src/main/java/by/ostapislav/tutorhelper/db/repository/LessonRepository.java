package by.ostapislav.tutorhelper.db.repository;


import by.ostapislav.tutorhelper.db.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson,Long> {

    @Query("SELECT s.name, SUM(l.price), COUNT(l) " +
            "FROM Lesson l JOIN l.student s " +
            "WHERE l.isPaid = false " +
            "GROUP BY s.id, s.name " +
            "HAVING SUM(l.price) > 0")
    List<Object[]> findDebtors();

    boolean existsByStudentIdAndLessonDateAndStartTime(Long studentId, Date lessonDate, Time startTime);

    Collection<Lesson> findByStudentIdOrderByLessonDateDesc(Long studentId);

    Collection<Lesson> findByStudentIdAndIsPaidFalse(Long studentId);

    boolean existsByStudentIdAndLessonDate(Long id, LocalDate lessonDate);
}
