package ru.denusariy.ComixRest.exception;

import lombok.Getter;

@Getter
public class ComicNotFoundException extends RuntimeException {
    private final String message = "Комикс с данным id не найден!";
}
