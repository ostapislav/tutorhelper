package by.ostapislav.tutorhelper.service;

import by.ostapislav.tutorhelper.db.entity.Student;
import by.ostapislav.tutorhelper.db.repository.StudentRepository;
import by.ostapislav.tutorhelper.dto.Student.StudentUpdateDto;
import by.ostapislav.tutorhelper.dto.Student.StudentRequestDto;
import by.ostapislav.tutorhelper.dto.Student.StudentResponseDto;

import lombok.RequiredArgsConstructor;
import by.ostapislav.tutorhelper.mapper.StudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Transactional
    public StudentResponseDto save(StudentRequestDto dto){
         Student student=studentMapper.toEntity(dto);
         var saved = studentRepository.save(student);
         return studentMapper.toDto(saved);
    }
    public List<StudentResponseDto> findAll() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toDto).toList();
    }
    public Optional<StudentResponseDto> findById(Long id) {
        return studentRepository.findById(id)
                .map(studentMapper::toDto);
    }
    @Transactional
    public StudentResponseDto update(Long id,StudentUpdateDto dto){
        Student student = studentRepository.findById(id).orElseThrow();
        studentMapper.updateEntity(student,dto);
        studentRepository.save(student);
        return studentMapper.toDto(student);
    }
    @Transactional
    public StudentResponseDto delete(Long id){
        Student student=studentRepository.findById(id).orElseThrow();
        studentRepository.delete(student);
        return studentMapper.toDto(student);
    }

}
