package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film) throws ValidationException;

    Film deleteFilmById(int id);

    Film updateFilm(Film film) throws ValidationException;

    Film getFilmById(int id);

    List<Film> getFilms();

}
