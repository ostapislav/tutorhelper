package by.ostapislav.tutorhelper.dto.Lesson;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LessonResponseDto {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long scheduleId;
    private String lessonDate;
    private String startTime;
    private String topic;
    private BigDecimal price;
    private Boolean isPaid;
    private String paidDate;
    private String status;
    private String createdAt;
}
