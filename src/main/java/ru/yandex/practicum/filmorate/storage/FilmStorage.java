package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {

    public Film addFilmToStorage(Film film) throws ValidationException;

    public Film deleteFilmFromStorage(Film film) throws ValidationException;

    public Film updateFilmInStorage(Film film) throws ValidationException;
}
