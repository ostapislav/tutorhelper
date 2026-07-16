package by.ostapislav.tutorhelper.db.repository;


import by.ostapislav.tutorhelper.db.entity.Schedule;
import by.ostapislav.tutorhelper.db.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.List;


@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    @Query("""
    select l.id from Schedule s
    join Lesson l 
    where s.dayOfWeek=:dayOfWeek and s.isActive=true 
""")
    List<Long> findLessonsScheduleByDayOfWeek(DayOfWeek dayOfWeek);

    boolean existsScheduleByStudentAndDayOfWeekAndStartTime(Student student, DayOfWeek dayOfWeek, Time startTime);

    List<Schedule> findByIsActiveTrue();
}
