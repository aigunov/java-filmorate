package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ElementNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmLikeException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film putLike(int filmId, int userId) throws FilmLikeException, ElementNotFoundException {
        User user = userStorage.getUserFromStorage(userId);
        Film film = filmStorage.getFilmFromStorage(filmId);
        filmNotExistThrow(film);
        userNotExistThrow(user);
        if (user.getLikedFilms().contains(film)) {
            log.error(String.format("client can't like the %s film more than once", film.getName()));
            throw new FilmLikeException(String.format("you already like the %s film", film.getName()));
        }
        user.getLikedFilms().add(film);
        film.setLikeCount(film.getLikeCount() + 1);
        log.info(String.format("client has just like the %s film", film.getName()));
        return film;
    }

    public Film removeLike(int filmId, int userId) throws FilmLikeException, ElementNotFoundException {
        User user = userStorage.getUserFromStorage(userId);
        Film film = filmStorage.getFilmFromStorage(filmId);
        filmNotExistThrow(film);
        userNotExistThrow(user);
        if (!user.getLikedFilms().contains(film)) {
            log.error(String.format("client can't remove the like from the %s film ", film.getName()));
            throw new FilmLikeException(String.format("you haven't liked the %s film", film.getName()));
        }
        user.getLikedFilms().remove(film);
        film.setLikeCount(film.getLikeCount() - 1);
        log.error(String.format("client has just remove his like from the %s film", film.getName()));
        return film;
    }

    public List<Film> getTopPopularFilms(int size) {
        log.info("client has got the list of most popular films");
        return filmStorage.getFilms().stream().
                sorted(Comparator.comparing(Film::getLikeCount).reversed()).
                limit(size).
                collect(Collectors.toList());
    }


    public List<Film> getFilms() {
        log.info("client has got the list of all films");
        return filmStorage.getFilms();
    }

    public Film getFilmById(int id) throws ElementNotFoundException {
        Film film = filmStorage.getFilmFromStorage(id);
        filmNotExistThrow(film);
        return film;
    }

    public Film addNewFilm(Film film) throws ValidationException {
        return filmStorage.addFilmToStorage(film);
    }

    public Film updateFilm(Film film) throws ValidationException {
        List<Film> films = filmStorage.getFilms();
        if (!films.contains(film)) {
            throw new NoSuchElementException("This film not exist to update");
        }
        log.info(String.format("The %s film has been updated", film.getName()));
        return filmStorage.updateFilmInStorage(film);
    }

    public Film removeFilm(int id) throws ElementNotFoundException {
        Film film = filmStorage.getFilmFromStorage(id);
        filmNotExistThrow(film);
        return filmStorage.deleteFilmFromStorage(id);
    }

    public void filmNotExistThrow(Film film) throws ElementNotFoundException {
        if (film == null) {
            log.error("this film is not exist to return, probably the path variables incorrect");
            throw new ElementNotFoundException("this film is not exist");
        }
    }

    public void userNotExistThrow(User user) throws ElementNotFoundException {
        if (user == null) {
            log.error("this user is not exist to return, probably the path variables incorrect");
            throw new ElementNotFoundException("this user is not exist");
        }
    }
}
