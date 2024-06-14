package ru.yandex.practicum.filmorate.storage.users.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User addUser(User user);

    User deleteUser(Integer user);

    User updateUser(User user);

    Optional<User> getUser(Integer id);

    List<User> getAllUsers();
}
