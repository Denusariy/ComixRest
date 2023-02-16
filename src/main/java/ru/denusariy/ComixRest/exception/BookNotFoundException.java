package ru.denusariy.ComixRest.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class BookNotFoundException extends RuntimeException {
    private final String message = "Книга с данным id не найдена!";
    private final HttpStatus status = HttpStatus.NOT_FOUND;

}
