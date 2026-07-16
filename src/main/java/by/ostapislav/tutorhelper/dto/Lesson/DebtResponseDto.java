package by.ostapislav.tutorhelper.dto.Lesson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtResponseDto {
    private String studentName;
    private BigDecimal totalDebt;
    private Integer lessonsCount; // Количество неоплаченных занятий
}