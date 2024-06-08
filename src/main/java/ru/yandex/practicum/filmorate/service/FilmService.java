package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ElementNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmLikeException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.films_logic.interfaces.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.films_logic.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.films_logic.interfaces.FilmsLikeStorage;
import ru.yandex.practicum.filmorate.storage.films_logic.interfaces.MPAStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.*;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmsDB;
    private final FilmsLikeStorage likedFilmsDB;
    private final MPAStorage mpaDB;
    private final FilmGenreStorage filmGenreDB;

    @Autowired
    public FilmService(FilmStorage filmsDB, FilmsLikeStorage likedFilmsDB, MPAStorage mpaDB, FilmGenreStorage filmGenreDB) {
        this.filmsDB = filmsDB;
        this.likedFilmsDB = likedFilmsDB;
        this.mpaDB = mpaDB;
        this.filmGenreDB = filmGenreDB;
    }

    /**
     * the method responsible for the logic of putting a like on a movie
     *
     * @param filmId of the film which user like
     * @param userId of the user who put like
     * @return liked film
     * @throws FilmLikeException
     * @throws ElementNotFoundException
     */
    public void putLike(Integer filmId, Integer userId) {
        likedFilmsDB.addLike(filmId, userId);
        log.info("Пользователь с id:" + userId + " поставил лайк фильму с id:" + filmId);
    }

    /**
     * the method responsible for the logic of remove a like on a movie
     *
     * @param filmId of the film which user unlike
     * @param userId of the user who remove his like
     * @return unliked film
     * @throws FilmLikeException
     * @throws ElementNotFoundException
     */
    public void removeLike(Integer filmId, Integer userId) {
        likedFilmsDB.removeLike(filmId, userId);
        log.info("Пользователь с id:" + userId + " удалил свой лайк фильму с id:" + filmId);
    }

    /**
     * return the list most popular films comparing by likes count long in size param
     *
     * @param size of list
     * @return
     */
    public List<Film> getTopPopularFilms(int size) {
        List<Film> popularFilms = filmsDB.getPopularFilms(size);
        log.info("Запрос на получение популярных фильмов обработан");
        return popularFilms;
    }


    public List<Film> getFilms() {
        log.info("Обработан запрос на получение списка всех фильмов");
        return filmsDB.getFilms();
    }

    public Film getFilmById(Integer id) {
        Film film = filmsDB.getFilmById(id)
                .orElseThrow(() -> new NoSuchElementException("Фильм с ID: " + id + " не найден."));
        Map<Integer, Set<Genre>> filmsGenres = filmGenreDB.findGenreOfFilm(List.of(film));

        film.setGenres(filmsGenres.get(film.getId()) != null ?
                (LinkedHashSet<Genre>) filmsGenres.get(film.getId()) : new LinkedHashSet<>());

        film.setMpa(mpaDB.getMPAofFilm(film.getId()));
        log.info("Обработан запрос на по поиску фильма. Найден фильм: {}.", film);
        return film;
    }

    public Film addNewFilm(Film film) {
        Validator.filmValidation(film);
        film.setId(filmsDB.addFilm(film).getId());
        filmGenreDB.addGenreToFilm(film, film.getGenres() != null ? film.getGenres() : Set.of());
        log.info("Фильм {} добавлен в базу данных", film);
        return film;
    }

    public Film updateFilm(Film film) throws ValidationException {
        Validator.filmValidation(film);
        if (filmsDB.getFilmById(film.getId()).isEmpty()) {
            throw new NoSuchElementException("Фильма с ID=" + film.getId() + " нет в базе");
        }
        filmGenreDB.removeGenreFromFilm(film.getId());
        filmsDB.updateFilm(film);

        filmGenreDB.addGenreToFilm(film, film.getGenres());

        log.info("Обновлен фильм: {}", film);
        return film;
    }

    public Film removeFilm(Integer id) {
        Optional<Film> film = filmsDB.getFilmById(id);
        if (film.isEmpty()) {
            throw new NoSuchElementException("Фильма с ID=" + film.get().getId() + " нет в базе");
        }

        filmGenreDB.removeGenreFromFilm(id);
        filmsDB.deleteFilmById(id);
        return film.get();
    }

}
