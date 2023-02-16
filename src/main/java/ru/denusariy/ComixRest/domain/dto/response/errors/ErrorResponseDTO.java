package ru.denusariy.ComixRest.domain.dto.response.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ErrorResponseDTO {
    @Schema(description = "Http статус")
    private final HttpStatus status;
    @Schema(description = "Сообщение ошибки")
    private final String message;
    @Schema(description = "Время ошибки")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private final LocalDateTime time = LocalDateTime.now();

    public ErrorResponseDTO(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
