package ru.yandex.practicum.filmorate.storage;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.users_logic.FriendshipDB;
import ru.yandex.practicum.filmorate.storage.users_logic.UserDB;
import ru.yandex.practicum.filmorate.storage.users_logic.interfaces.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.users_logic.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserDBTest {

    private final JdbcTemplate jdbcTemplate;
    private User user;


    @BeforeEach
    public void init() {
        user = User.builder()
                .id(1)
                .name("java")
                .email("developer@mail.ru")
                .login("baobab")
                .birthday(LocalDate.of(1999, 12, 1))
                .build();
    }

    @Test
    public void check_addUser_shouldAddUser() {
        UserStorage userDbStorage = new UserDB(jdbcTemplate);
        User newUser = userDbStorage.addUser(user);
        assertEquals(user, newUser);
    }

    @Test
    public void check_updateUser_shouldUpdate() {
        UserStorage userDbStorage = new UserDB(jdbcTemplate);
        User newUser = user.toBuilder().birthday(LocalDate.of(2000, 11, 11)).build();
        userDbStorage.addUser(user);
        User updatedUser = userDbStorage.updateUser(newUser);
        assertEquals(newUser, updatedUser);
    }

    @Test
    public void check_findUser_shouldFind() {
        UserStorage userDbStorage = new UserDB(jdbcTemplate);
        userDbStorage.addUser(user);
        User findUser = userDbStorage.getUser(1).get();
        assertEquals(user, findUser);
    }

    @Test
    public void check_deleteUser_shouldDelete() {
        UserStorage userDbStorage = new UserDB(jdbcTemplate);
        userDbStorage.addUser(user);
        User deletedUser = userDbStorage.deleteUser(1);
        assertEquals(user, deletedUser);
    }

    @Test
    public void check_getAllUsers_shouldReturnListSize_3() {
        UserStorage userDbStorage = new UserDB(jdbcTemplate);
        User user2 = user.toBuilder().name("Alex").email("refds@mail.ru").build();
        User user3 = user.toBuilder().name("loky").email("sdgfsdgafdg@mail.ru").build();

        userDbStorage.addUser(user);
        userDbStorage.addUser(user2);
        userDbStorage.addUser(user3);

        int size = userDbStorage.getAllUsers().size();

        assertEquals(3, size);
    }

    @Test
    public void check_user1_Friendship_to_user2() {
        UserStorage userDbStorage = new UserDB(jdbcTemplate);
        FriendshipStorage friendshipDbStorage = new FriendshipDB(jdbcTemplate);
        User user2 = user.toBuilder().name("Alex").email("refds@mail.ru").build();

        User user1InDb = userDbStorage.addUser(user);
        User user2InDb = userDbStorage.addUser(user2);

        friendshipDbStorage.addFriend(1, 2);

        List<User> listFriendUser1 = friendshipDbStorage.getUserFriend(1);

        int countFriendUser1 = listFriendUser1.size();

        assertEquals(1, countFriendUser1);

        User friend = listFriendUser1.get(0);

        assertEquals(user2InDb, friend);

        int countFriendUser2 = friendshipDbStorage.getUserFriend(2).size();

        assertEquals(0, countFriendUser2);

        friendshipDbStorage.addFriend(2, 1);

        List<User> listFriendUser2 = friendshipDbStorage.getUserFriend(2);

        int size = listFriendUser2.size();

        assertEquals(1, size);

        assertEquals(user1InDb, listFriendUser2.get(0));
    }
}
