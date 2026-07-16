package by.ostapislav.tutorhelper.service;

import by.ostapislav.tutorhelper.db.entity.Lesson;
import by.ostapislav.tutorhelper.db.entity.Schedule;
import by.ostapislav.tutorhelper.db.entity.Student;
import by.ostapislav.tutorhelper.db.repository.LessonRepository;
import by.ostapislav.tutorhelper.db.repository.ScheduleRepository;
import by.ostapislav.tutorhelper.db.repository.StudentRepository;
import by.ostapislav.tutorhelper.dto.Lesson.DebtResponseDto;
import by.ostapislav.tutorhelper.dto.Lesson.LessonRequestDto;
import by.ostapislav.tutorhelper.dto.Lesson.LessonResponseDto;
import by.ostapislav.tutorhelper.dto.Lesson.LessonUpdateDto;
import by.ostapislav.tutorhelper.mapper.LessonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final ScheduleRepository scheduleRepository;
    private final LessonMapper lessonMapper;

    @Transactional
    public LessonResponseDto createLesson(LessonRequestDto dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + dto.getStudentId()));

        if (dto.getStartTime() != null) {
            if (lessonRepository.existsByStudentIdAndLessonDateAndStartTime(
                    dto.getStudentId(), dto.getLessonDate(), dto.getStartTime())) {
                throw new IllegalArgumentException(
                        "Lesson already exists for this student at " + dto.getLessonDate() +
                                " " + dto.getStartTime()
                );
            }
        }

        Schedule schedule = null;
        if (dto.getScheduleId() != null) {
            schedule = scheduleRepository.findById(dto.getScheduleId())
                    .orElseThrow(() -> new IllegalArgumentException("Schedule not found with id: " + dto.getScheduleId()));

            if (!schedule.getStudent().getId().equals(dto.getStudentId())) {
                throw new IllegalArgumentException("Schedule does not belong to this student");
            }

            if (dto.getStartTime() == null) {
                dto.setStartTime(schedule.getStartTime());
            }
        }

        Lesson lesson = lessonMapper.toEntity(dto);
        lesson.setStudent(student);
        lesson.setSchedule(schedule);

        if (lesson.getStartTime() == null) {
            lesson.setStartTime(Time.valueOf(LocalTime.of(10, 0)));
        }

        Lesson saved = lessonRepository.save(lesson);
        return lessonMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public LessonResponseDto getLessonById(Long id) {
        Lesson lesson = findLessonOrThrow(id);
        return lessonMapper.toDto(lesson);
    }

    @Transactional(readOnly = true)
    public List<LessonResponseDto> getAllLessons() {
        return lessonRepository.findAll().stream()
                .map(lessonMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LessonResponseDto> getLessonsByStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new IllegalArgumentException("Student not found with id: " + studentId);
        }

        return lessonRepository.findByStudentIdOrderByLessonDateDesc(studentId).stream()
                .map(lessonMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LessonResponseDto> getUnpaidLessonsByStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new IllegalArgumentException("Student not found with id: " + studentId);
        }

        return lessonRepository.findByStudentIdAndIsPaidFalse(studentId).stream()
                .map(lessonMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public LessonResponseDto updateLesson(Long id, LessonUpdateDto dto) {
        Lesson lesson = findLessonOrThrow(id);

        if (dto.getScheduleId() != null) {
            Schedule schedule = scheduleRepository.findById(dto.getScheduleId())
                    .orElseThrow(() -> new IllegalArgumentException("Schedule not found with id: " + dto.getScheduleId()));

            if (!schedule.getStudent().getId().equals(lesson.getStudent().getId())) {
                throw new IllegalArgumentException("Schedule does not belong to this student");
            }
            lesson.setSchedule(schedule);
        }

        lessonMapper.updateEntity(lesson, dto);
        Lesson updated = lessonRepository.save(lesson);
        return lessonMapper.toDto(updated);
    }

    @Transactional
    public void deleteLesson(Long id) {
        Lesson lesson = findLessonOrThrow(id);
        lessonRepository.delete(lesson);
    }

    @Transactional
    public LessonResponseDto markAsPaid(Long lessonId) {
        Lesson lesson = findLessonOrThrow(lessonId);

        if (lesson.getIsPaid()) {
            throw new IllegalArgumentException("Lesson is already paid");
        }

        lesson.setIsPaid(true);
        lesson.setPaidDate(Date.valueOf(LocalDate.now()));
        return lessonMapper.toDto(lesson);
    }

    @Transactional
    public LessonResponseDto markAsUnpaid(Long lessonId) {
        Lesson lesson = findLessonOrThrow(lessonId);

        if (!lesson.getIsPaid()) {
            throw new IllegalArgumentException("Lesson is already unpaid");
        }

        lesson.setIsPaid(false);
        lesson.setPaidDate(null);

        Lesson saved = lessonRepository.save(lesson);
        return lessonMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<DebtResponseDto> getDebtReport() {
        List<Object[]> results = lessonRepository.findDebtors();
        return results.stream()
                .map(lessonMapper::toDebtDto)
                .collect(Collectors.toList());
    }

    private Lesson findLessonOrThrow(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + id));
    }
}