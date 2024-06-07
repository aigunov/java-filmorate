package ru.yandex.practicum.filmorate.storage.films_logic.interfaces;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    List<Genre> getGenres();

    Genre addGenre(Genre genre);

    Optional<Genre> getGenre(int id);

    Genre updateGenre(Genre genre);

    Genre deleteGenre(int id);

}
