package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.*;

/**
 * Accept POST, PUT, GET requests to /users
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap();
    private int generatedId = 0;

    /**
     * @return list of all users
     */
    @GetMapping
    public List<User> getUsers() {
        log.info("Client get list of all users");
        return new ArrayList<>(users.values());
    }

    /**
     * @param user body of POST request to put into users map
     * @return dded user
     * @throws ValidationException if the user's values are invalid
     */
    @PostMapping
    public User addUser(@RequestBody User user) throws ValidationException {
        try {
            makeUserLoginAlsoName(user);
            Validator.userValidation(user);
            int userId = generatorId();
            user.setId(userId);
            users.put(userId, user);
            log.info("New user has just added {}", user);
        } catch (ValidationException exception) {
            throw exception;
        }
        return user;
    }

    /**
     * @param user body PUT request to update in users map
     * @return updated user
     * @throws ValidationException if the user's values are invalid
     */
    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        try {
            Validator.userValidation(user);
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
                log.info("User's info has just updated {}", user);
            } else {
                throw new NoSuchElementException("This user not exist to update");
            }
        } catch (ValidationException exception) {
            throw exception;
        }
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
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}
