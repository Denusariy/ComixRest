package ru.denusariy.ComixRest.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ComicNotFoundException extends RuntimeException {
    private final String message = "Комикс с данным id не найден!";
    private final HttpStatus status = HttpStatus.NOT_FOUND;
}
