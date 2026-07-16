package by.ostapislav.tutorhelper.dto.Lesson;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

@Data
@Component
public class LessonRequestDto {
    private Long studentId;
    private Long scheduleId;
    private Date lessonDate;
    private Time startTime;
    private String topic;
    private BigDecimal price;
}
