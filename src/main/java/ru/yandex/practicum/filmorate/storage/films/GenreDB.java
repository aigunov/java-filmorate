package ru.yandex.practicum.filmorate.storage.films;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.films.interfaces.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * @param film
     * @param genres
     */
    @Override
    public void addGenreToFilm(Film film, Set<Genre> genres) {
        jdbc.batchUpdate("INSERT INTO film_genre(film_id, genre_id) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    Iterator<Genre> iterator = genres.iterator();

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, film.getId());
                        Genre genre = iterator.next();
                        ps.setInt(2, genre.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genres.size();
                    }
                });
    }

    /**
     * @param films
     * @return
     */
    @Override
    public Map<Integer, Set<Genre>> findGenreOfFilm(List<Film> films) {
        Map<Integer, Set<Genre>> listGenre = new HashMap<>();

        List<Integer> filmsID = films.stream().map(Film::getId).collect(Collectors.toList());

        String placeholders = String.join(",", Collections.nCopies(filmsID.size(), "?"));

        String sqlQuery = String.format("""
                        SELECT fg.film_id, fg.genre_id, ge.genre
                        FROM film_genre AS fg
                        LEFT JOIN genre AS ge ON fg.genre_id = ge.genre_id
                        WHERE film_id IN (%S)
                        ORDER BY fg.film_id, fg.genre_id
                        """,
                placeholders);

        SqlRowSet rs = jdbc.queryForRowSet(sqlQuery, filmsID.toArray());

        while (rs.next()) {
            int filmID = rs.getInt("film_id");

            Genre genre = Genre.builder()
                    .id(rs.getInt("genre_id"))
                    .name(rs.getString("genre"))
                    .build();

            listGenre.computeIfAbsent(filmID, k -> new LinkedHashSet<>()).add(genre);
        }

        return listGenre;
    }

    @Override
    public void removeGenreFromFilm(Film film, List<Genre> genres) {
        jdbc.batchUpdate("""
                        DELETE FROM film_genre
                        WHERE film_id = ?, genre_id = ?
                        """,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, film.getId());
                        ps.setInt(2, genres.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genres.size();
                    }
                });
    }

    @Override
    public void removeGenreFromFilm(int id) {
        jdbc.update("""
                DELETE FROM film_genre
                WHERE film_id = ?
                """, id);

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
