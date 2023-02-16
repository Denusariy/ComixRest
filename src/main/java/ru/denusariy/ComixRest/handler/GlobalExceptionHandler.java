package ru.denusariy.ComixRest.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.denusariy.ComixRest.domain.dto.response.errors.ErrorResponseDTO;
import ru.denusariy.ComixRest.domain.dto.response.errors.ValidationResponseDTO;
import ru.denusariy.ComixRest.exception.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String COMMENT = "comment";
    private static final String DATE_PATTERN = "dd-MM-yyyy HH:mm:ss";

    //Обработка BookNotFoundException c созданием Response DTO
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handle(BookNotFoundException e) {
        return new ErrorResponseDTO(e.getStatus(), e.getMessage());
    }

    //Обработка ComicNotFoundException c созданием Response DTO
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handle(ComicNotFoundException e) {
        return new ErrorResponseDTO(e.getStatus(), e.getMessage());
    }

    //Создание DTO при ошибке валидации
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationResponseDTO> handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity
                .badRequest()
                .body(ValidationResponseDTO.builder()
                        .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_PATTERN)))
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .statusDescription(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .errors(errors)
                        .build());
    }

    //обработка остальных RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ValidationResponseDTO> handleInvalidArgument(RuntimeException ex) {
        return getInternalServerErrorResponseEntity(ex);
    }

    //создание DTO при RuntimeException со статусом 500
    private ResponseEntity<ValidationResponseDTO> getInternalServerErrorResponseEntity(RuntimeException ex) {
        return ResponseEntity
                .internalServerError()
                .body(ValidationResponseDTO.builder()
                        .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_PATTERN)))
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .statusDescription(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .errors(Map.of(COMMENT, ex.getMessage()))
                        .build());
    }
}
