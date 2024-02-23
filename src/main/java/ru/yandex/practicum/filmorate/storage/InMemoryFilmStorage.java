package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int generatedId = 0;

    @Override
    public Film addFilmToStorage(Film film) throws ValidationException {
        log.info("New film has just added {}", film);
        Validator.filmValidation(film);
        int filmId = generatorId();
        film.setId(filmId);
        films.put(filmId, film);
        return film;
    }

    @Override
    public Film getFilmFromStorage(int id){
        log.info("Client get the film by ID");
        return films.get(id);
    }

    @Override
    public Film deleteFilmFromStorage(int id) {
        return films.remove(id);
    }

    @Override
    public Film updateFilmInStorage(Film film) throws ValidationException {
        log.info("Film has just updated {}", film);
        Validator.filmValidation(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new NoSuchElementException("This film not exist to update");
        }
        return film;
    }


    /**
     * @return generated id for new film
     */
    private int generatorId() {
        return ++generatedId;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
