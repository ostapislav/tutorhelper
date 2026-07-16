package by.ostapislav.tutorhelper.mapper;

import by.ostapislav.tutorhelper.db.entity.Schedule;
import by.ostapislav.tutorhelper.db.entity.Student;
import by.ostapislav.tutorhelper.dto.Schedule.ScheduleUpdateDto;
import by.ostapislav.tutorhelper.dto.Student.StudentRequestDto;
import by.ostapislav.tutorhelper.dto.Student.StudentResponseDto;
import by.ostapislav.tutorhelper.dto.Student.StudentUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
@RequiredArgsConstructor
public class StudentMapper {
    public Student toEntity(StudentRequestDto dto) {
        return Student.builder()
                .name(dto.name())
                .contact(dto.contact())
                .build();
    }
    public StudentResponseDto toDto(Student entity) {
        return new StudentResponseDto(entity.getId(),
                entity.getName(),
                entity.getContact());
    }
    public void updateEntity(Student student, StudentUpdateDto dto) {
        if(dto.name()!=null){
            student.setName(dto.name());
        }
        if(dto.contact()!=null){
            student.setContact(dto.contact());;
        }
    }
}
