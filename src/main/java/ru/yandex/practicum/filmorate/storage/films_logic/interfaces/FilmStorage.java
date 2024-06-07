package ru.yandex.practicum.filmorate.storage.films_logic.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film addFilm(Film film);

    Film deleteFilmById(int id);

    Film updateFilm(Film film);

    Optional<Film> getFilmById(int id);

    List<Film> getFilms();

    List<Film> getPopularFilms(int size);
}
