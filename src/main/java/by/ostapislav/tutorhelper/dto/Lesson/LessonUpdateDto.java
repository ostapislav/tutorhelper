package by.ostapislav.tutorhelper.dto.Lesson;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LessonUpdateDto {



    private Long scheduleId;
    private String lessonDate;
    private String startTime;
    private String topic;
    private BigDecimal price;

}
