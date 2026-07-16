package by.ostapislav.tutorhelper.dto.Schedule;
import java.sql.Time;



public record ScheduleUpdateDto (
     Integer dayOfWeek,
     Time startTime,
     Short durationMinutes,
     Boolean isActive
){}