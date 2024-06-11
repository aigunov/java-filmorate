package ru.yandex.practicum.filmorate.storage.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.users.interfaces.FriendshipStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository("friendshipDB")
public class FriendshipDB implements FriendshipStorage {
    private final JdbcTemplate jdbc;

    @Autowired
    public FriendshipDB(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void addFriend(int id1, int id2) {
        jdbc.update("""
                INSERT INTO friendship(user_id, friend_id)
                VALUES (?, ?)
                """, id1, id2);
    }

    @Override
    public void deleteFriend(int id1, int id2) {
        jdbc.update("""
                DELETE FROM friendship
                WHERE user_id = ? AND friend_id = ?
                """, id1, id2);
    }


    @Override
    public List<User> getUserFriend(int id) {
        return jdbc.query("""
                SELECT us.*
                FROM friendship AS fr
                LEFT JOIN users AS us ON fr.friend_id = us.id
                WHERE fr.user_id = ?
                """, this::mapRowToUser, id);
    }

    @Override
    public List<User> getMutualFriend(int id1, int id2) {
        String sqlQuery = """
                SELECT fr1.friend_id
                FROM friendship AS fr1
                INNER JOIN friendship AS fr2 ON fr1.friend_id = fr2.friend_id
                WHERE fr1.user_id = ? AND fr2.user_id = ?
                """;
        return getUsers(sqlQuery, new Object[]{id1, id2});
    }

    private User mapRowToUser(ResultSet rs, int i) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }

    private List<User> getUsers(String sqlQuery, Object[] queryParam) {
        SqlRowSet sqlRowSet = jdbc.queryForRowSet(sqlQuery, queryParam);

        List<Integer> listID = new ArrayList<>();

        while (sqlRowSet.next()) {
            listID.add(sqlRowSet.getInt("friend_id"));
        }

        String placeholders = String.join(",", Collections.nCopies(listID.size(), "?"));

        return jdbc.query(String.format("SELECT * FROM users WHERE id IN (%s)", placeholders),
                listID.toArray(),
                this::mapRowToUser);
    }
}
