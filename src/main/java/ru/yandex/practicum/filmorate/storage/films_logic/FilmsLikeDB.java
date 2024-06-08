package ru.yandex.practicum.filmorate.storage.films_logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.films_logic.interfaces.FilmsLikeStorage;

@Repository("filmslikeDB")
public class FilmsLikeDB implements FilmsLikeStorage {
    private final JdbcTemplate jdbc;

    @Autowired
    public FilmsLikeDB(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void addLike(int filmId, int userId) {
        jdbc.update("INSERT INTO liked_films (user_id, film_id) VALUES (?, ?)",
                userId, filmId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        jdbc.update("DELETE FROM liked_films WHERE film_id = ? AND user_id = ?",
                filmId, userId);
    }
}
