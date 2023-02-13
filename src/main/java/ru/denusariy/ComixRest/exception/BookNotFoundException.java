package ru.denusariy.ComixRest.exception;

import lombok.Getter;

@Getter
public class BookNotFoundException extends RuntimeException {
    private final String message = "Книга с данным id не найдена!";

}
