package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ElementNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmLikeException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

/**
 * Accepts POST, PUT, GET requests at /films
 */
@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;


    /**
     * @return List of all films
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getFilms() {
        log.info("пришел GET запрос /films на извлечения списка всех фильмов");
        List<Film> films = filmService.getFilms();
        log.info("отправлен ответ на GET запрос /films: {}", films);
        return films;
    }


    /**
     * @param id of the film to return
     * @return film by id
     * @throws ElementNotFoundException
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilm(@PathVariable int id) {
        log.info("пришел GET запрос /films/{id} на получение фильма", id);
        Film film = filmService.getFilmById(id);
        log.info("отправлен ответ на GET запрос /films/{id} с телом: {}", id, film);
        return film;
    }

    /**
     * @param film body of the POST request to put into films map
     * @return added film
     * @throws ValidationException if the film's values are invalid
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("пришел запрос POST /films на добавление фильма с телом: {}", film);
        film = filmService.addNewFilm(film);
        log.info("отправлен ответ на POST /films запрос на добавление фильма");
        return film;
    }

    /**
     * @param film body of the PUT request to update in films map
     * @return updated film
     * @throws ValidationException if the film's values are invalid
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("пришел запрос PUT /films на обновление фильма");
        film = filmService.updateFilm(film);
        log.info("отправлен ответ на PUT /films с телом: {}", film);
        return film;
    }

    /**
     * @param id of the film to delete
     * @return deleted film
     * @throws ElementNotFoundException
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film deleteFilm(@PathVariable int id) {
        log.info("пришел запрос DELETE /films/{id} на удаление фильма", id);
        Film film = filmService.removeFilm(id);
        log.info("отправлен запрос DELETE /films/{}: {}", id, film);
        return film;
    }

    /**
     * @param id     of the film to like
     * @param userId of user who like the film
     * @return the film that user liked
     * @throws FilmLikeException
     * @throws ElementNotFoundException
     */
    @PutMapping("{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void putLike(@PathVariable int id, @PathVariable int userId) {
        log.info("пришел запрос PUT /films/{id}/like/{userId} - поставить лайк", id, userId);
        filmService.putLike(id, userId);
        log.info("отправлен ответ на PUT /films/{id}/like/{userId} запрос");
    }

    /**
     * @param id     a film from like was deleted
     * @param userId a user who remove his like
     * @return the film which was unliked
     * @throws FilmLikeException
     * @throws ElementNotFoundException
     */
    @DeleteMapping("{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        log.info("пришел запрос DELETE /films/{id}/like/{userId} - убрать лайку", id, userId);
        filmService.removeLike(id, userId);
        log.info("отправлен ответ на DELETE /films/{id}/like/{userId} запрос", id, userId);
    }

    /**
     * @param count of the most popular films from the list that need to be returned
     * @return the list of most popular films size equal to count @RequestParam
     */
    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("пришел запрос GET /films/popular?{count} на получение списка популярных фильмов", count);
        List<Film> films = filmService.getTopPopularFilms(count);
        log.info("отправлен ответ на GET /films/popular запрос");
        return films;
    }
}
