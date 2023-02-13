package ru.denusariy.ComixRest.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<?> handleException(BookNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ComicNotFoundException.class)
    public ResponseEntity<?> handleException(ComicNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookNotUpdatedException.class)
    public ResponseEntity<ValidationResponseDTO> handleInvalidArgument(BookNotUpdatedException ex) {
        return getInternalServerErrorResponseEntity(ex);
    }

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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ValidationResponseDTO> handleInvalidArgument(RuntimeException ex) {
        return getInternalServerErrorResponseEntity(ex);
    }

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
