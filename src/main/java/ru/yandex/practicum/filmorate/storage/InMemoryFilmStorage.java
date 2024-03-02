package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int generatedId = 0;

    @Override
    public Film addFilm(Film film) {
        Validator.filmValidation(film);
        int filmId = generatorId();
        film.setId(filmId);
        films.put(filmId, film);
        log.info("The film has just added {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Validator.filmValidation(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new NoSuchElementException("This film not exist to update");
        }
        log.info("Film has just updated {}", film);
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        log.info("Client get the film by ID");
        return films.get(id);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film deleteFilmById(int id) {
        log.info("The film have been removed");
        return films.remove(id);
    }


    /**
     * @return generated id for new film
     */
    private int generatorId() {
        return ++generatedId;
    }

}
