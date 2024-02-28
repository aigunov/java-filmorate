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

    /**
     * the method responsible for the logic of putting a like on a movie
     *
     * @param filmId of the film which user like
     * @param userId of the user who put like
     * @return liked film
     * @throws FilmLikeException
     * @throws ElementNotFoundException
     */
    public Film putLike(Integer filmId, Integer userId) throws FilmLikeException, ElementNotFoundException {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(filmId);
        checkIfFilmExist(film);
        checkIfUserExist(user);
        if (user.getLikedFilms().contains(film.getId())) {
            log.error(String.format("client can't like the %s film more than once", film.getName()));
            throw new FilmLikeException(String.format("you already like the %s film", film.getName()));
        }
        user.getLikedFilms().add(film.getId());
        film.getLikesId().add(userId);
        log.info(String.format("client has just like the %s film", film.getName()));
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
    public Film removeLike(Integer filmId, Integer userId) throws FilmLikeException, ElementNotFoundException {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(filmId);
        checkIfFilmExist(film);
        checkIfUserExist(user);
        if (!user.getLikedFilms().contains(film.getId())) {
            log.error(String.format("client can't remove the like from the %s film ", film.getName()));
            throw new FilmLikeException(String.format("you haven't liked the %s film", film.getName()));
        }
        user.getLikedFilms().remove(film.getId());
        film.getLikesId().remove(userId);
        log.error(String.format("client has just remove his like from the %s film", film.getName()));
        return film;
    }

    /**
     * return the list most popular films comparing by likes count long in size param
     *
     * @param size of list
     * @return
     */
    public List<Film> getTopPopularFilms(int size) {
        log.info("client has got the list of most popular films");
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparing(film -> ((Film) film).getLikesId().size()).reversed())
                .limit(size)
                .collect(Collectors.toList());
    }


    public List<Film> getFilms() {
        log.info("client has got the list of all films");
        return filmStorage.getFilms();
    }

    public Film getFilmById(Integer id) throws ElementNotFoundException {
        Film film = filmStorage.getFilmById(id);
        checkIfFilmExist(film);
        return film;
    }

    public Film addNewFilm(Film film) throws ValidationException {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) throws ValidationException {
        List<Film> films = filmStorage.getFilms();
        if (!films.contains(film)) {
            throw new NoSuchElementException("This film not exist to update");
        }
        log.info(String.format("The %s film has been updated", film.getName()));
        return filmStorage.updateFilm(film);
    }

    public Film removeFilm(Integer id) throws ElementNotFoundException {
        Film film = filmStorage.getFilmById(id);
        checkIfFilmExist(film);
        return filmStorage.deleteFilmById(id);
    }

    public void checkIfFilmExist(Film film) throws ElementNotFoundException {
        if (film == null) {
            log.error("this film is not exist to return, probably the path variables incorrect");
            throw new ElementNotFoundException("this film is not exist");
        }
    }

    public void checkIfUserExist(User user) throws ElementNotFoundException {
        if (user == null) {
            log.error("this user is not exist to return, probably the path variables incorrect");
            throw new ElementNotFoundException("this user is not exist");
        }
    }
}
