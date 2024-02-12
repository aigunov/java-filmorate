package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.*;

/**
 * Accepts POST, PUT, GET requests at /films
 */
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();
    private int generatedId = 0;

    /**
     * @return List of all films
     */
    @GetMapping
    public List<Film> getFilms() {
        log.info("Client get list of all films");
        return new ArrayList<>(films.values());
    }

    /**
     * @param film body of the POST request to put into films map
     * @return added film
     * @throws ValidationException if the film's values are invalid
     */
    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        try {
            Validator.filmValidation(film);
            int filmId = generatorId();
            film.setId(filmId);
            films.put(filmId, film);
            log.info("New film has just added {}", film);
        } catch (ValidationException exception) {
            throw exception;
        }
        return film;
    }

    /**
     * @param film body of the PUT request to update in films map
     * @return updated film
     * @throws ValidationException if the film's values are invalid
     */
    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        try {
            Validator.filmValidation(film);
            if (films.keySet().contains(film.getId())) {
                films.put(film.getId(), film);
            } else {
                throw new NoSuchElementException("This film not exist to update");
            }
            log.info("Film has just updated {}", film);
        } catch (ValidationException exception) {
            throw exception;
        }
        return film;
    }

    /**
     * @return generated id for new film
     */
    private int generatorId() {
        return ++generatedId;
    }
}
