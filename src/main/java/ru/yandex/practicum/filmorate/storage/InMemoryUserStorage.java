package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{

    private final Map<Integer, User> users = new HashMap();

    @Override
    public User addUserToStorage(User user) throws ValidationException {
        log.info("New user has just added {}", user);
        makeUserLoginAlsoName(user);
        Validator.userValidation(user);
        int userId = generatorId();
        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    @Override
    public User deleteUserFromStorage(User user) {
        return null;
    }

    @Override
    public User updateUserInStorage(User user) throws ValidationException {
        log.info("User's info has just updated {}", user);
        Validator.userValidation(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new NoSuchElementException("This user not exist to update");
        }
        return user;
    }
}
