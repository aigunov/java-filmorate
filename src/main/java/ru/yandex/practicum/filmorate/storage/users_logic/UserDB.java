package ru.yandex.practicum.filmorate.storage.users_logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.users_logic.interfaces.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository("userDB")
public class UserDB implements UserStorage {
    private final JdbcTemplate jdbc;

    @Autowired
    public UserDB(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public User addUser(User user) {
        Map<String, Object> map = new SimpleJdbcInsert(this.jdbc)
                .withTableName("users")
                .usingColumns("email", "login", "name", "birthday")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKeyHolder(Map.of(
                        "email", user.getEmail(),
                        "login", user.getLogin(),
                        "name", user.getName(),
                        "birthday", user.getBirthday()
                )).getKeys();
        user.setId((Integer) map.get("id"));
        return user;
    }

    @Override
    public User deleteUser(Integer id) {
        User user = getUser(id).get();
        jdbc.update("DELETE FROM users WHERE id = ?", id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        jdbc.update("""
                        UPDATE users
                        SET name = ?, login = ?, email = ?, birthday = ?
                        WHERE id = ?
                        """, user.getName(), user.getLogin(), user.getEmail(),
                user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public Optional<User> getUser(Integer id) {
        return jdbc.query("""
                SELECT *
                FROM users
                WHERE id = ?
                """, this::mapRowToUser, id).stream().findFirst();
    }

    @Override
    public List<User> getAllUsers() {
        return jdbc.query("SELECT * FROM users ORDER BY id",
                this::mapRowToUser);
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
}
