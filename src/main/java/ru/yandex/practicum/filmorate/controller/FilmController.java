package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmLikeException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;

/**
 * Accepts POST, PUT, GET requests at /films
 */
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmService filmService, InMemoryFilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    /**
     * @return List of all films
     */
    @GetMapping
    public List<Film> getFilms(){return filmStorage.getFilms();}

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmStorage.getFilmFromStorage(id);
    }

    /**
     * @param film body of the POST request to put into films map
     * @return added film
     * @throws ValidationException if the film's values are invalid
     */

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        return filmStorage.addFilmToStorage(film);
    }

    /**
     * @param film body of the PUT request to update in films map
     * @return updated film
     * @throws ValidationException if the film's values are invalid
     */
    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        return filmStorage.updateFilmInStorage(film);
    }

    @DeleteMapping("/{id}")
    public Film deleteFilm(@PathVariable int id){
        return filmStorage.deleteFilmFromStorage(id);
    }

    @PutMapping("{id}/like/{userId}")
    public Film putLike(
            @PathVariable int id,
            @PathVariable int userId) throws FilmLikeException {
        return filmService.putLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Film removeLike(
            @PathVariable int id,
            @PathVariable int userId) throws FilmLikeException {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count){
        return filmService.getTopPopularFilms(count);
    }
}
