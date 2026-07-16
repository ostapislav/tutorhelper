package by.ostapislav.tutorhelper.http.rest;

import by.ostapislav.tutorhelper.dto.Student.StudentRequestDto;
import by.ostapislav.tutorhelper.dto.Student.StudentResponseDto;
import by.ostapislav.tutorhelper.dto.Student.StudentUpdateDto;
import by.ostapislav.tutorhelper.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Tag(name = "Управление учениками", description = "CRUD операции для учеников и поиск")
public class StudentRestController {

    private final StudentService studentService;

    @PostMapping
    @Operation(
            summary = "Создать нового ученика"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ученик успешно создан"),
    })
    public ResponseEntity<StudentResponseDto> createStudent(
            @Valid @RequestBody StudentRequestDto dto) {
        StudentResponseDto created = studentService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить ученика по ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ученик найден"),
            @ApiResponse(responseCode = "404", description = "Ученик с указанным ID не найден")
    })
    public ResponseEntity<StudentResponseDto> getStudentById(
            @Parameter(description = "ID ученика", example = "1", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(studentService.findById(id).get());
    }

    @GetMapping
    @Operation(
            summary = "Получить список всех учеников"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список учеников получен")
    })
    public ResponseEntity<List<StudentResponseDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить данные ученика")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ученик успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Ученик с указанным ID не найден")
    })
    public ResponseEntity<StudentResponseDto> updateStudent(
            @Parameter(description = "ID ученика", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody StudentUpdateDto dto) {
        StudentResponseDto updated = studentService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить ученика")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ученик успешно удален"),
            @ApiResponse(responseCode = "404", description = "Ученик с указанным ID не найден")
    })
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "ID ученика", example = "1", required = true)
            @PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}