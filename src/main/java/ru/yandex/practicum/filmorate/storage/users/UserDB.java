package ru.yandex.practicum.filmorate.storage.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.users.interfaces.UserStorage;

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

    /**
     * метод добавляющий пользователя в бд
     *
     * @param user - тело запроса User
     * @return сохраненный в бд пользователь с сгенерированным ID
     */
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

    /**
     * метод удаляет пользователя из бд
     *
     * @param id удаляемого из бд пользователя
     * @return удаляемый пользователь
     */
    @Override
    public User deleteUser(Integer id) {
        User user = getUser(id).get();
        jdbc.update("DELETE FROM users WHERE id = ?", id);
        return user;
    }

    /**
     * метод обновляет пользователя в бд
     *
     * @param user - тело запроса User
     * @return пользователя с обновленными полями
     */
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

    /**
     * метод извлекающий пользователя из бд,
     * в случае если пользователь не найден, вернется пустой Optional
     *
     * @param id пользователя, которого надо извлечь из бд
     * @return Optional<> с извлеченным пользователем
     */
    @Override
    public Optional<User> getUser(Integer id) {
        return jdbc.query("""
                SELECT *
                FROM users
                WHERE id = ?
                """, this::mapRowToUser, id).stream().findFirst();
    }

    /**
     * метод возвращает список всех пользователей
     *
     * @return список пользователей
     */
    @Override
    public List<User> getAllUsers() {
        return jdbc.query("SELECT * FROM users ORDER BY id",
                this::mapRowToUser);
    }

    /**
     * метод маппает сырой ответ от бд в пользователя
     */
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
