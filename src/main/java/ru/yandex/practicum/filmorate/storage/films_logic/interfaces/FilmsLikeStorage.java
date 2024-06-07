package ru.yandex.practicum.filmorate.storage.films_logic.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmsLikeStorage {

    void addLike(int filmId, int userId);
    void removeLike(int filmId, int userId);
}
