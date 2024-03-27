package ru.yandex.practicum.filmorate.exception;

/**
 * This is an exception thrown if the MOJO field of the class is incorrect
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
