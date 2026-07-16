package by.ostapislav.tutorhelper.http.rest;

import by.ostapislav.tutorhelper.dto.Schedule.ScheduleRequestDto;
import by.ostapislav.tutorhelper.dto.Schedule.ScheduleResponseDto;
import by.ostapislav.tutorhelper.dto.Schedule.ScheduleUpdateDto;
import by.ostapislav.tutorhelper.dto.Schedule.WeeklyScheduleDto;
import by.ostapislav.tutorhelper.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@Tag(name = "Управление расписанием", description = "CRUD операции для шаблонов расписания и генерация недельного расписания")
public class ScheduleRestController {

    private final ScheduleService scheduleService;

    @PostMapping
    @Operation(
            summary = "Создать шаблон расписания")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Шаблон успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные или дубликат шаблона"),
            @ApiResponse(responseCode = "404", description = "Ученик не найден")
    })
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @Valid @RequestBody ScheduleRequestDto dto) {
        ScheduleResponseDto created = scheduleService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить шаблон по ID",
            description = "Возвращает шаблон расписания по его идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Шаблон найден"),
            @ApiResponse(responseCode = "404", description = "Шаблон не найден")
    })
    public ResponseEntity<ScheduleResponseDto> getScheduleById(
            @Parameter(description = "ID шаблона", example = "1", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.findById(id));
    }

    @GetMapping
    @Operation(
            summary = "Получить все шаблоны"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список шаблонов получен")
    })
    public ResponseEntity<List<ScheduleResponseDto>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.findAll());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить шаблон",
            description = "Обновляет существующий шаблон расписания. Можно изменить день, время или длительность."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Шаблон успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные или конфликт с другим шаблоном"),
            @ApiResponse(responseCode = "404", description = "Шаблон не найден")
    })
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @Parameter(description = "ID шаблона", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ScheduleUpdateDto dto) {
        ScheduleResponseDto updated = scheduleService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить шаблон")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Шаблон успешно удален"),
            @ApiResponse(responseCode = "404", description = "Шаблон не найден"),
            @ApiResponse(responseCode = "409", description = "Невозможно удалить: есть привязанные занятия")
    })
    public ResponseEntity<Void> deleteSchedule(
            @Parameter(description = "ID шаблона", example = "1", required = true)
            @PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/weekly")
    @Operation(
            summary = "Сгенерировать расписание на неделю",
            description = "На основе активных шаблонов генерирует расписание занятий на неделю. Для каждого занятия показывает, проведено оно или еще нет."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Расписание успешно сгенерировано (может быть пустым)")
    })
    public ResponseEntity<List<WeeklyScheduleDto>> generateWeeklySchedule(
            @Parameter(
                    description = "Дата начала недели (включительно). Формат: yyyy-MM-dd. Если не указана — берется текущий понедельник.",
                    example = "2026-07-13"
            )
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        return ResponseEntity.ok(scheduleService.generateWeeklySchedule(startDate));
    }
}