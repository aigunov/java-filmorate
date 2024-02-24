package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ElementNotFoundException;
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

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    /**
     * @return List of all films
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getFilms(){
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilm(@PathVariable int id) throws ElementNotFoundException {
        return filmService.getFilmById(id);
    }

    /**
     * @param film body of the POST request to put into films map
     * @return added film
     * @throws ValidationException if the film's values are invalid
     */

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        return filmService.addNewFilm(film);
    }

    /**
     * @param film body of the PUT request to update in films map
     * @return updated film
     * @throws ValidationException if the film's values are invalid
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        return filmService.updateFilm(film);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film deleteFilm(@PathVariable int id) throws ElementNotFoundException {
        return filmService.removeFilm(id);
    }

    @PutMapping("{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Film putLike(
            @PathVariable int id,
            @PathVariable int userId) throws FilmLikeException, ElementNotFoundException {
        return filmService.putLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Film removeLike(
            @PathVariable int id,
            @PathVariable int userId) throws FilmLikeException, ElementNotFoundException {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count){
        return filmService.getTopPopularFilms(count);
    }
}
