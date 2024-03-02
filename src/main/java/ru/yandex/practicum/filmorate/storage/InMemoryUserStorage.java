package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap();
    private int generatedId = 0;

    @Override
    public User addUser(User user) {
        log.info("New user has just added {}", user);
        makeUserLoginAlsoName(user);
        Validator.userValidation(user);
        int userId = generatorId();
        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("User's info has just updated {}", user);
        Validator.userValidation(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            log.error(String.format("the %s user is not exist in storage to update him", user.getName()));
            throw new NoSuchElementException("This user not exist to update");
        }
        return user;
    }

    @Override
    public User getUserById(Integer id) {
        log.info("Client get info about the user by ID");
        return users.get(id);
    }

    @Override
    public User deleteUserById(Integer id) {
        User user = users.remove(id);//некрасивый код вышел
        log.info("The user has been deleted ");
        return user;
    }


    /**
     * @return generated id for new user
     */
    private int generatorId() {
        return ++generatedId;
    }

    /**
     * sets the login as a username if the username is empty
     *
     * @param user is object to check if field username if empty
     */
    private void makeUserLoginAlsoName(User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
