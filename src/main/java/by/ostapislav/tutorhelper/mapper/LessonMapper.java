package by.ostapislav.tutorhelper.mapper;

import by.ostapislav.tutorhelper.db.entity.Lesson;
import by.ostapislav.tutorhelper.db.repository.StudentRepository;
import by.ostapislav.tutorhelper.dto.Lesson.DebtResponseDto;
import by.ostapislav.tutorhelper.dto.Lesson.LessonRequestDto;
import by.ostapislav.tutorhelper.dto.Lesson.LessonResponseDto;
import by.ostapislav.tutorhelper.dto.Lesson.LessonUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.format.DateTimeFormatter;

@Component
public class LessonMapper {

    @Autowired
    private StudentRepository studentRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public LessonResponseDto toDto(Lesson lesson) {
        if (lesson == null) {
            return null;
        }

        LessonResponseDto dto = new LessonResponseDto();
        dto.setId(lesson.getId());
        dto.setStudentId(lesson.getStudent().getId());
        dto.setStudentName(lesson.getStudent().getName());

        if (lesson.getSchedule() != null) {
            dto.setScheduleId(lesson.getSchedule().getId());
        }

        dto.setLessonDate(lesson.getLessonDate().toString().formatted(DATE_FORMATTER));

        if (lesson.getStartTime() != null) {
            dto.setStartTime(lesson.getStartTime().toString().formatted(TIME_FORMATTER));
        }

        dto.setTopic(lesson.getTopic());
        dto.setPrice(lesson.getPrice());
        dto.setIsPaid(lesson.getIsPaid());

        if (lesson.getPaidDate() != null) {
            dto.setPaidDate(lesson.getPaidDate().toString().formatted(DATE_FORMATTER));
        }

        dto.setStatus(lesson.getIsPaid() ? "Оплачено" : "Долг");


        return dto;
    }
    public Lesson toEntity(LessonRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Lesson lesson = new Lesson();
        lesson.setStudent(studentRepository.findById(dto.getStudentId()).orElseThrow());
        lesson.setLessonDate(dto.getLessonDate());
        lesson.setStartTime(dto.getStartTime());
        lesson.setTopic(dto.getTopic());
        lesson.setPrice(dto.getPrice());
        lesson.setIsPaid(false); // По умолчанию не оплачено
        lesson.setPaidDate(null);

        return lesson;
    }
    public void updateEntity(Lesson lesson, LessonUpdateDto dto) {
        if (dto.getLessonDate() != null) {
            lesson.setLessonDate(Date.valueOf(dto.getLessonDate()));
        }
        if (dto.getStartTime() != null) {
            lesson.setStartTime(Time.valueOf(dto.getStartTime()));
        }
        if (dto.getTopic() != null) {
            lesson.setTopic(dto.getTopic());
        }
        if (dto.getPrice() != null) {
            lesson.setPrice(dto.getPrice());
        }
    }
    public DebtResponseDto toDebtDto(Object[] result) {
        // result[0] = studentName, result[1] = totalDebt, result[2] = lessonsCount
        return new DebtResponseDto(
                (String) result[0],
                (BigDecimal) result[1],
                ((Long) result[2]).intValue()
        );
    }
}
