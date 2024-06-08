package ru.yandex.practicum.filmorate.storage.films_logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.films_logic.interfaces.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository("filmDB")
public class FilmDB implements FilmStorage {
    private final JdbcTemplate jdbc;

    @Autowired
    public FilmDB(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Film addFilm(Film film) {
        Map<String, Object> columns = new SimpleJdbcInsert(this.jdbc)
                .withTableName("films")
                .usingColumns("name", "description", "release_date", "duration", "rate", "rating_id")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKeyHolder(Map.of(
                        "name", film.getName(),
                        "description", film.getDescription(),
                        "release_date", film.getReleaseDate(),
                        "duration", film.getDuration(),
                        "rate", film.getRate(),
                        "rating_id", (film.getMpa() != null && film.getMpa().getId() != 0
                                ? film.getMpa().getId() : 1)
                )).getKeys();
        film.setId((Integer) columns.get("id"));
        film.setMpa(film.getMpa() == null ?
                MPA.builder().id(1).name("PG-13").build() : film.getMpa());
        return film;
    }

    @Override
    public Film deleteFilmById(int id) {
        Film deletedFilm = getFilmById(id).get();
        String sqlQuery = "DELETE FROM films WHERE id = ?";
        jdbc.update(sqlQuery, id);
        return deletedFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET name = ?, description = ?, release_date = ?," +
                " duration = ?, rating_id = ? WHERE id = ?";
        jdbc.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());

        return film;
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        return jdbc.query("""
                        SELECT films.* , mpa.rating_id, mpa.rating 
                        FROM films 
                        JOIN mpa ON films.rating_id = mpa.rating_id 
                        WHERE films.id = ?
                        """, new Object[]{id},
                this::mapRowToFilm).stream().findFirst();
    }

    @Override
    public List<Film> getFilms() {
        return jdbc.query("SELECT f.*, mpa.rating_id AS mpa_id, mpa.rating AS mpa_name FROM films AS f " +
                "INNER JOIN mpa ON f.rating_id = mpa.rating_id", this::mapRowToFilm);
    }

    @Override
    public List<Film> getPopularFilms(int size) {
        return jdbc.query("""
                SELECT f.*, mpa.rating_id AS mpa_id, mpa.rating AS mpa_name, COUNT(lf.user_id) AS likes
                FROM films AS f
                INNER JOIN mpa ON f.rating_id = mpa.rating_id
                INNER JOIN liked_films AS lf ON lf.film_id=f.id
                GROUP BY f.id
                ORDER BY likes DESC LIMIT ?
                """, this::mapRowToFilm, size);
    }


    private Film mapRowToFilm(ResultSet rs, int i) throws SQLException {
        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .mpa(MPA.builder().name(rs.getString("mpa.rating"))
                        .id(rs.getInt("mpa.rating_id")).build())
                .duration(rs.getInt("duration"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .build();
    }


}
