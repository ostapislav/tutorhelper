package by.ostapislav.tutorhelper.mapper;


import by.ostapislav.tutorhelper.db.entity.Schedule;
import by.ostapislav.tutorhelper.db.repository.StudentRepository;
import by.ostapislav.tutorhelper.dto.Schedule.ScheduleRequestDto;
import by.ostapislav.tutorhelper.dto.Schedule.ScheduleResponseDto;
import by.ostapislav.tutorhelper.dto.Schedule.ScheduleUpdateDto;
import by.ostapislav.tutorhelper.dto.Schedule.WeeklyScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ScheduleMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    @Autowired
    public StudentRepository studentRepository;
    public ScheduleResponseDto toDto(Schedule schedule) {
        if (schedule == null) {
            return null;
        }

        ScheduleResponseDto dto = new ScheduleResponseDto();
        dto.setId(schedule.getId());
        dto.setStudentId(schedule.getStudent().getId());
        dto.setStudentName(schedule.getStudent().getName());
        dto.setDayOfWeek(schedule.getDayOfWeek().getValue());
        dto.setDayOfWeekName(schedule.getDayOfWeek().name());
        dto.setStartTime(schedule.getStartTime().toString().formatted(TIME_FORMATTER));
        dto.setDurationMinutes((schedule.getDurationMinutes()));
        dto.setIsActive(schedule.getIsActive());

        return dto;
    }

    public Schedule toEntity(ScheduleRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Schedule schedule = new Schedule();
        schedule.setStudent(studentRepository.findById(dto.studentId()).orElseThrow());
        schedule.setDayOfWeek(DayOfWeek.of(dto.dayOfWeek()));
        schedule.setStartTime(dto.startTime());
        schedule.setDurationMinutes(dto.duration());
        schedule.setIsActive(true);

        return schedule;
    }

    public void updateEntity(Schedule schedule, ScheduleUpdateDto dto) {
        if (dto.dayOfWeek() != null) {
            schedule.setDayOfWeek(DayOfWeek.of(dto.dayOfWeek()));
        }
        if (dto.startTime() != null) {
            schedule.setStartTime(dto.startTime());
        }
        if (dto.durationMinutes() != null) {
            schedule.setDurationMinutes(dto.durationMinutes());
        }
        if (dto.isActive() != null) {
            schedule.setIsActive(dto.isActive());
        }
    }
    public WeeklyScheduleDto toWeeklyDto(Schedule schedule, LocalDate date, boolean isCompleted) {
        if (schedule == null) {
            return null;
        }

        return WeeklyScheduleDto.builder()
                .studentId(schedule.getStudent().getId())
                .studentName(schedule.getStudent().getName())
                .dayOfWeek(schedule.getDayOfWeek().getValue())
                .dayOfWeekName(dayOfWeekToString(schedule.getDayOfWeek().getValue()))
                .date(date.format(DATE_FORMATTER))
                .startTime(schedule.getStartTime().toString().formatted(TIME_FORMATTER))
                .durationMinutes(schedule.getDurationMinutes())
                .scheduleId(schedule.getId())
                .isCompleted(isCompleted)
                .build();
    }



    private String dayOfWeekToString(Integer day) {
        if (day == null) return null;
        return switch (day) {
            case 1 -> "Понедельник";
            case 2 -> "Вторник";
            case 3 -> "Среда";
            case 4 -> "Четверг";
            case 5 -> "Пятница";
            case 6 -> "Суббота";
            case 7 -> "Воскресенье";
            default -> "Неизвестно";
        };
    }
}