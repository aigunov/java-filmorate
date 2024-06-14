package ru.yandex.practicum.filmorate.storage.films.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {

    void addGenreToFilm(Film film, Set<Genre> genres);

    Map<Integer, Set<Genre>> findGenreOfFilm(List<Film> films);

    void removeGenreFromFilm(Film film, List<Genre> genres);

    void removeGenreFromFilm(int id);

    List<Genre> getGenres();

    Genre addGenre(Genre genre);

    Optional<Genre> getGenre(int id);

    Genre updateGenre(Genre genre);

    Genre deleteGenre(int id);

}
