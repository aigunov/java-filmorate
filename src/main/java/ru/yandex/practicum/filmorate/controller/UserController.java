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

    @ExceptionHandler(ValidationException.class)
    @PostMapping
    public User addUser(@RequestBody User user) throws ValidationException {

    }

    /**
     * @param user body PUT request to update in users map
     * @return updated user
     * @throws ValidationException if the user's values are invalid
     */

    @ExceptionHandler(ValidationException.class)
    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {

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
