package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmLikeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film putLike(int filmId, int userId) throws FilmLikeException {
        User user = userStorage.getUserFromStorage(userId);
        Film film = filmStorage.getFilmFromStorage(filmId);
        filmNotExistThrow(film);
        filmNotExistThrow(user);
        if (user.getLikedFilms().contains(film)) {
            throw new FilmLikeException(String.format("you already like the %s film", film.getName()));
        }
        user.getLikedFilms().add(film);
        film.setLikeCount(film.getLikeCount() + 1);
        return film;
    }

    public Film removeLike(int filmId, int userId) throws FilmLikeException {
        User user = userStorage.getUserFromStorage(userId);
        Film film = filmStorage.getFilmFromStorage(filmId);
        filmNotExistThrow(film);
        filmNotExistThrow(user);
        if (!user.getLikedFilms().contains(film)){
            throw new FilmLikeException(String.format("you haven't liked the %s film", film.getName()));
        }
        user.getLikedFilms().remove(film);
        film.setLikeCount(film.getLikeCount() - 1);
        return film;
    }

    public List<Film> getTopPopularFilms(int size) {
        return filmStorage.getFilms().stream().
                            sorted(Comparator.comparing(Film::getLikeCount)).
                            limit(size).
                            collect(Collectors.toList());
    }

    public void filmNotExistThrow(Film film) {
        if (film == null) {
            throw new NullPointerException("this film is not exist");
        }
    }

    public void filmNotExistThrow(User user) {
        if (user == null) {
            throw new NullPointerException("this user is not exist");
        }
    }
}
