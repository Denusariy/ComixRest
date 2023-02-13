package ru.denusariy.ComixRest.domain.dto.response.errors;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ValidationResponseDTO {
    @Schema(description = "Время ошибки", example = "08-02-2023 20:05:22")
    private String timestamp;
    @Schema(description = "Код ошибки", example = "400")
    private int httpStatus;
    @Schema(description = "Описание статуса", example = "Bad Request")
    private String statusDescription;
    @Schema(description = "Ошибки")
    private Map<String, String> errors;
}
