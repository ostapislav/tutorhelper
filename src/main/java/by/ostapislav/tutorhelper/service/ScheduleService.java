package by.ostapislav.tutorhelper.service;

import by.ostapislav.tutorhelper.db.entity.Schedule;
import by.ostapislav.tutorhelper.db.entity.Student;
import by.ostapislav.tutorhelper.db.repository.LessonRepository;
import by.ostapislav.tutorhelper.db.repository.ScheduleRepository;
import by.ostapislav.tutorhelper.db.repository.StudentRepository;
import by.ostapislav.tutorhelper.dto.Schedule.ScheduleRequestDto;
import by.ostapislav.tutorhelper.dto.Schedule.ScheduleResponseDto;
import by.ostapislav.tutorhelper.dto.Schedule.ScheduleUpdateDto;
import by.ostapislav.tutorhelper.dto.Schedule.WeeklyScheduleDto;
import by.ostapislav.tutorhelper.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    @Autowired
    ScheduleMapper scheduleMapper;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    LessonRepository lessonRepository;
    @Transactional
    public ScheduleResponseDto save(ScheduleRequestDto dto) {
        Student student = studentRepository.findById(dto.studentId())
                .orElseThrow(() -> new IllegalArgumentException("Такого ученика нет"));

        if (scheduleRepository.existsScheduleByStudentAndDayOfWeekAndStartTime(
                studentRepository.findById(dto.studentId()).orElseThrow(),
                DayOfWeek.of(dto.dayOfWeek()),
                dto.startTime())) {
            throw new IllegalArgumentException("такой шаблон уже есть");
        }
        Schedule schedule = scheduleMapper.toEntity(dto);
        var saved = scheduleRepository.save(schedule);
        return scheduleMapper.toDto(saved);
    }
    public List<ScheduleResponseDto> findAll() {
        return scheduleRepository.findAll().stream()
                .map(scheduleMapper::toDto).toList();
    }
    public ScheduleResponseDto findById(Long id) {
        return scheduleRepository.findById(id)
                .map(scheduleMapper::toDto)
                .orElseThrow(()->new IllegalArgumentException("такого ученика нет"));
    }
    @Transactional
    public ScheduleResponseDto update(Long id, ScheduleUpdateDto dto){
        Schedule schedule = scheduleRepository.findById(id).orElseThrow();
        scheduleMapper.updateEntity(schedule,dto);
        return scheduleMapper.toDto(schedule);
    }
    @Transactional
    public ScheduleResponseDto delete(Long id){
        Schedule schedule=scheduleRepository.findById(id).orElseThrow();
        scheduleRepository.delete(schedule);
        return scheduleMapper.toDto(schedule);
    }

    @Transactional(readOnly = true)
    public List<WeeklyScheduleDto> generateWeeklySchedule(LocalDate startDate) {
        if (startDate == null) {
            startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        }
        List<Schedule> schedules = scheduleRepository.findByIsActiveTrue();
        List<WeeklyScheduleDto> result = new ArrayList<>();
        LocalDate endDate = startDate.plusDays(6);

        for (Schedule schedule : schedules) {
            DayOfWeek dayOfWeek = DayOfWeek.of(schedule.getDayOfWeek().getValue());
            LocalDate lessonDate = startDate.with(TemporalAdjusters.nextOrSame(dayOfWeek));

            if (!lessonDate.isBefore(startDate) && !lessonDate.isAfter(endDate)) {
                boolean isCompleted = lessonRepository.existsByStudentIdAndLessonDate(
                        schedule.getStudent().getId(), lessonDate
                );

                WeeklyScheduleDto dto = scheduleMapper.toWeeklyDto(schedule, lessonDate, isCompleted);
                result.add(dto);
            }
        }
        result.sort(Comparator.comparing(WeeklyScheduleDto::getDate).thenComparing(WeeklyScheduleDto::getStartTime));

        return result;
    }
}
