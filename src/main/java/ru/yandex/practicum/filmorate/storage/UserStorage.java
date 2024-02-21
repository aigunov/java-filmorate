package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {

    public User addUserToStorage(User user) throws ValidationException;

    public User deleteUserFromStorage(User user);

    public User updateUserInStorage(User user) throws ValidationException;
}
