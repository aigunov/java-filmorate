package ru.yandex.practicum.filmorate.storage.films_logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.films_logic.interfaces.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository("genreDB")
public class GenreDB implements GenreStorage {
    private final JdbcTemplate jdbc;

    @Autowired
    public GenreDB(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Genre addGenre(Genre genre) {
        Map<String, Object> map = new SimpleJdbcInsert(this.jdbc)
                .withTableName("genre")
                .usingColumns("genre")
                .usingGeneratedKeyColumns("genre_id")
                .executeAndReturnKeyHolder(Map.of("genre", genre.getName())).getKeys();
        return genre;
    }


    @Override
    public Genre deleteGenre(int id) {
        Genre genre = getGenre(id).get();
        String sqlQuery = """
                DELETE FROM genre
                WHERE genre_id = ?
                """;
        jdbc.update(sqlQuery, genre.getId());
        //String sqlQueryFilms = "DELETE FROM films WHERE genre_id = ?";
        //jdbc.update(sqlQueryFilms, genre.getGenreId());
        return genre;
    }

    @Override
    public Genre updateGenre(Genre genre) {
        String sqlQuery = """
                UPDATE genre
                SET genre = ?
                WHERE id = ?
                """;
        jdbc.update(sqlQuery, genre.getName(), genre.getId());
        return genre;
    }

    @Override
    public Optional<Genre> getGenre(int id) {
        List<Genre> genres = jdbc.query("""
                SELECT *
                FROM genre
                WHERE genre_id = ?
                """, this::mapRowToGenre, id);
        return genres.stream().findFirst();
    }

    @Override
    public List<Genre> getGenres() {
        return jdbc.query("""
                SELECT *
                FROM genre           
                """, this::mapRowToGenre);
    }

    private Genre mapRowToGenre(ResultSet rs, int i) throws SQLException {
        return Genre.builder()
                .name(rs.getString("genre"))
                .id(rs.getInt("genre_id")).build();
    }

}
