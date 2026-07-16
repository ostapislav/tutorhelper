package by.ostapislav.tutorhelper.dto.Schedule;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeeklyScheduleDto {
    private Long studentId;
    private String studentName;
    private Integer dayOfWeek;
    private String dayOfWeekName;
    private String date;
    private String startTime;
    private Short durationMinutes;
    private Long scheduleId;
    private Boolean isCompleted; // Есть ли уже проведенное занятие на эту дату
}