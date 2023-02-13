package ru.denusariy.ComixRest.exception;

public class BookNotUpdatedException extends RuntimeException {
    public BookNotUpdatedException(String message) {
        super(message);
    }
}
