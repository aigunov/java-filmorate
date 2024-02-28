package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user) throws ValidationException;

    User deleteUserById(Integer user);

    User updateUser(User user) throws ValidationException;

    User getUserById(Integer id);

    List<User> getUsers();
}
