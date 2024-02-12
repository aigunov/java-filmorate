package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
@Slf4j
public class Validator {
    static final LocalDate EARLIEST_DATE = LocalDate.of(1895, 12, 28);
    static final Integer MAX_DESCRIPTION_LENGTH = 200;

    /**
     * @param film to check it for correctness
     * @throws ValidationException if some field is not valid for film
     */
    public static void filmValidation(Film film) throws ValidationException {
        if (film.getName().isEmpty() || film.getName().isBlank() ||
                film.getDescription().length() >= MAX_DESCRIPTION_LENGTH ||
                film.getReleaseDate().isBefore(EARLIEST_DATE) ||
                film.getDuration() < 0 || film.getReleaseDate().isAfter(LocalDate.now())) {
            log.warn("The film from request's body is invalid, check it's data {}", film);
            throw new ValidationException("Invalid fields values for film");
        }
    }

    /**
     * @param user to check it for correctness
     * @throws ValidationException if some field is not valid for user
     */
    public static void userValidation(User user) throws ValidationException {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@") || user.getLogin().isEmpty() ||
                user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("The user from request's body is invalid, check it's data {}", user);
            throw new ValidationException("Invalid fields values for user");
        }
    }
}
