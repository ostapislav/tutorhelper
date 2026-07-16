package by.ostapislav.tutorhelper.dto.Schedule;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Time;
import java.time.LocalTime;

public record ScheduleRequestDto (
         Long studentId,
         Integer dayOfWeek,
         Time startTime,
         Short duration
) {}