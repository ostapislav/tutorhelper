package by.ostapislav.tutorhelper.dto.Schedule;

import lombok.Data;

@Data
public class ScheduleResponseDto {
    private Long id;
    private Long studentId;
    private String studentName;
    private Integer dayOfWeek;
    private String dayOfWeekName;
    private String startTime;
    private Short durationMinutes;
    private Boolean isActive;
    private String createdAt;
}