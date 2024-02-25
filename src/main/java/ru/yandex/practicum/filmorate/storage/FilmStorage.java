package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilmToStorage(Film film) throws ValidationException;

    Film deleteFilmFromStorage(int id);

    Film updateFilmInStorage(Film film) throws ValidationException;

    Film getFilmFromStorage(int id);

    List<Film> getFilms();

}
