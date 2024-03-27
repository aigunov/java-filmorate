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

    /**
     * the method responsible for the logic of putting a like on a movie
     *
     * @param filmId of the film which user like
     * @param userId of the user who put like
     * @return liked film
     * @throws FilmLikeException
     * @throws ElementNotFoundException
     */
    public Film putLike(Integer filmId, Integer userId) {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(filmId);
        checkIfFilmExist(film);
        checkIfUserExist(user);
        if (user.getLikedFilms().contains(film.getId())) {
            log.error("user can't like the film more than once");
            throw new FilmLikeException(String.format("user with id={} has already like the film with id={}", userId, filmId));
        }
        user.getLikedFilms().add(film.getId());
        film.getLikesId().add(userId);
        log.info("user with id={} just liked film with id={}", userId, filmId);
        return film;
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
    public Film removeLike(Integer filmId, Integer userId) {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(filmId);
        checkIfFilmExist(film);
        checkIfUserExist(user);
        if (!user.getLikedFilms().contains(film.getId())) {
            log.error("user can't remove the like from the film ");
            throw new FilmLikeException(String.format("user with id={} hasn't like the film with id={}", userId, filmId));
        }
        user.getLikedFilms().remove(film.getId());
        film.getLikesId().remove(userId);
        log.error("User with id={} just unliked film with id={}", userId, filmId);
        return film;
    }

    /**
     * return the list most popular films comparing by likes count long in size param
     *
     * @param size of list
     * @return
     */
    public List<Film> getTopPopularFilms(int size) {
        log.info("user has got the list of most popular films");
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparing(film -> ((Film) film).getLikesId().size()).reversed())
                .limit(size)
                .collect(Collectors.toList());
    }


    public List<Film> getFilms() {
        log.info("user has got the list of all films");
        return filmStorage.getFilms();
    }

    public Film getFilmById(Integer id) {
        Film film = filmStorage.getFilmById(id);
        checkIfFilmExist(film);
        return film;
    }

    public Film addNewFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) throws ValidationException {
        return filmStorage.updateFilm(film);
    }

    public Film removeFilm(Integer id) {
        Film film = filmStorage.getFilmById(id);
        checkIfFilmExist(film);
        return filmStorage.deleteFilmById(id);
    }

    public void checkIfFilmExist(Film film) {
        if (film == null) {
            log.error("this film is not exist to return, probably the path variables incorrect");
            throw new ElementNotFoundException("this film is not exist");
        }
    }

    public void checkIfUserExist(User user) {
        if (user == null) {
            log.error("this user is not exist to return, probably the path variables incorrect");
            throw new ElementNotFoundException("this user is not exist");
        }
    }
}
