package ru.yandex.practicum.filmorate.storage.films_logic.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmGenreStorage {

    void addGenreToFilm(Film film, Set<Genre> genres);

    Map<Integer, Set<Genre>> findGenreOfFilm(List<Film> films);

    void removeGenreFromFilm(Film film, List<Genre> genres);

    void removeGenreFromFilm(int id);
}
