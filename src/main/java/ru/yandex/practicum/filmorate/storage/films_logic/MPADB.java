package ru.yandex.practicum.filmorate.storage.films_logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.films_logic.interfaces.MPAStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository("MPADB")
public class MPADB implements MPAStorage {
    private final JdbcTemplate jdbc;

    @Autowired
    public MPADB(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public MPA addMPA(MPA mpa) {
        new SimpleJdbcInsert(this.jdbc)
                .withTableName("mpa")
                .usingColumns("rating")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKeyHolder(Map.of("rating", mpa.getName())).getKeys();
        return mpa;
    }

    @Override
    public Optional<MPA> getMPA(int id) {
        return jdbc.query("""
                        SELECT rating_id, rating
                        FROM mpa
                        WHERE rating_id = ?
                        """,
                this::mapRowToMPA, id).stream().findFirst();
    }

    @Override
    public MPA deleteMPA(int id) {
        MPA mpa = getMPA(id).get();
        jdbc.update("DELETE FROM mpa WHERE id = ?", id);
        return mpa;
    }

    @Override
    public MPA updateMPA(MPA mpa) {
        jdbc.update("""
                UPDATE mpa SET rating = ?
                WHERE rating_id = ?
                """, mpa.getName(), mpa.getId());
        return mpa;
    }

    @Override
    public List<MPA> getAllMPA() {
        return jdbc.query("SELECT * FROM mpa ORDER BY rating_id", this::mapRowToMPA);
    }

    @Override
    public MPA getMPAofFilm(int filmdId) {
        SqlRowSet rs = jdbc.queryForRowSet("""
                SELECT mpa.rating_id, mpa.rating
                FROM films AS f
                LEFT JOIN mpa ON f.rating_id = mpa.rating_id
                WHERE f.id = ?
                """, filmdId);

        return rs.next() ? MPA.builder().id(rs.getInt("rating_id")).
                name(rs.getString("rating")).build() : null;
    }

    private MPA mapRowToMPA(ResultSet rs, int i) throws SQLException {
        return MPA.builder()
                .name(rs.getString("rating"))
                .id(rs.getInt("rating_id"))
                .build();
    }
}
