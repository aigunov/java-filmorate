package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUserToStorage(User user) throws ValidationException;

    User deleteUserFromStorage(int user);

    User updateUserInStorage(User user) throws ValidationException;

    User getUserFromStorage(int id);

    List<User> getUsers();
}
