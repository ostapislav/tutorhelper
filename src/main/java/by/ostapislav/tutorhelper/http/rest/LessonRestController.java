package by.ostapislav.tutorhelper.http.rest;

import by.ostapislav.tutorhelper.dto.Lesson.DebtResponseDto;
import by.ostapislav.tutorhelper.dto.Lesson.LessonRequestDto;
import by.ostapislav.tutorhelper.dto.Lesson.LessonResponseDto;
import by.ostapislav.tutorhelper.dto.Lesson.LessonUpdateDto;
import by.ostapislav.tutorhelper.service.LessonService;
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
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
@Tag(name = "Управление занятиями", description = "CRUD операции для занятий, отметки оплаты и отчеты")
public class LessonRestController {

    private final LessonService lessonService;

    @PostMapping
    @Operation(
            summary = "Создать новое занятие",
            description = "Создает запись о проведенном занятии. Можно привязать к шаблону расписания (scheduleId) или создать отдельно."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Занятие успешно создано"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные (неверный формат, отрицательная цена и т.д.)"),
            @ApiResponse(responseCode = "404", description = "Ученик или шаблон расписания не найден"),
            @ApiResponse(responseCode = "409", description = "Занятие на эту дату и время уже существует")
    })
    public ResponseEntity<LessonResponseDto> createLesson(
            @Valid @RequestBody LessonRequestDto dto) {
        LessonResponseDto created = lessonService.createLesson(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить занятие по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Занятие найдено"),
            @ApiResponse(responseCode = "404", description = "Занятие с указанным ID не найдено")
    })
    public ResponseEntity<LessonResponseDto> getLessonById(
            @Parameter(description = "ID занятия", example = "1", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getLessonById(id));
    }

    @GetMapping
    @Operation(
            summary = "Получить список всех занятий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список занятий получен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<LessonResponseDto>> getAllLessons() {
        return ResponseEntity.ok(lessonService.getAllLessons());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить занятие")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Занятие успешно обновлено"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные (неверный формат или отрицательная цена)"),
            @ApiResponse(responseCode = "404", description = "Занятие или связанный шаблон не найдены"),
            @ApiResponse(responseCode = "409", description = "Конфликт: занятие на эту дату и время уже существует у этого ученика")
    })
    public ResponseEntity<LessonResponseDto> updateLesson(
            @Parameter(description = "ID занятия", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody LessonUpdateDto dto) {
        LessonResponseDto updated = lessonService.updateLesson(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить занятие")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Занятие успешно удалено (тело ответа пустое)"),
            @ApiResponse(responseCode = "404", description = "Занятие с указанным ID не найдено")
    })
    public ResponseEntity<Void> deleteLesson(
            @Parameter(description = "ID занятия", example = "1", required = true)
            @PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/pay")
    @Operation(
            summary = "Отметить занятие как оплаченное",
            description = "Устанавливает флаг оплаты в true и фиксирует дату оплаты (сегодняшняя дата). Повторный вызов вернет ошибку."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Занятие успешно отмечено как оплаченное"),
            @ApiResponse(responseCode = "400", description = "Занятие уже было оплачено ранее"),
            @ApiResponse(responseCode = "404", description = "Занятие с указанным ID не найдено")
    })
    public ResponseEntity<LessonResponseDto> markAsPaid(
            @Parameter(description = "ID занятия", example = "1", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(lessonService.markAsPaid(id));
    }

    @PatchMapping("/{id}/unpay")
    @Operation(
            summary = "Отменить оплату занятия",
            description = "Снимает отметку об оплате (устанавливает флаг в false и очищает дату оплаты). Используется для исправления ошибок."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Оплата успешно отменена"),
            @ApiResponse(responseCode = "400", description = "Занятие еще не было оплачено, отменять нечего"),
            @ApiResponse(responseCode = "404", description = "Занятие с указанным ID не найдено")
    })
    public ResponseEntity<LessonResponseDto> markAsUnpaid(
            @Parameter(description = "ID занятия", example = "1", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(lessonService.markAsUnpaid(id));
    }

    @GetMapping("/debts")
    @Operation(
            summary = "Получить отчет по должникам",
            description = "Возвращает список всех учеников с неоплаченными занятиями. Для каждого ученика показывает сумму долга и количество неоплаченных занятий."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отчет успешно сгенерирован (может быть пустым)"),
            @ApiResponse(responseCode = "500", description = "Ошибка при формировании отчета")
    })
    public ResponseEntity<List<DebtResponseDto>> getDebtReport() {
        return ResponseEntity.ok(lessonService.getDebtReport());
    }
}