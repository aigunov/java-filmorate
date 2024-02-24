package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ElementNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserFriendException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

/**
 * Accept POST, PUT, GET requests to /users
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers(){return userService.getUsers();}

    /**
     * @return the user
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable int id) throws ElementNotFoundException {
        return userService.getUserById(id);
    }

    /**
     * @param user body of POST request to put into users map
     * @return dded user
     * @throws ValidationException if the user's values are invalid
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user) throws ValidationException {
        return userService.addUser(user);
    }

    /**
     * @param user body PUT request to update in users map
     * @return updated user
     * @throws ValidationException if the user's values are invalid
     */

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody User user) throws ValidationException {
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User deleteUser(@PathVariable int id) throws ElementNotFoundException {
        return userService.removeUser(id);
    }

    @PutMapping("{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User addFriend(
            @PathVariable int id,
            @PathVariable int friendId) throws UserFriendException, ElementNotFoundException {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User deleteFriend(
            @PathVariable int id,
            @PathVariable int friendId) throws UserFriendException, ElementNotFoundException {
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getFriends(@PathVariable int id) throws ElementNotFoundException {
        return userService.getListOfFriends(id);
    }

    @GetMapping("{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User getFriend(
            @PathVariable int id,
            @PathVariable int friendId) throws ElementNotFoundException {
        return userService.getFriendById(id, friendId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getCommonFriends(
            @PathVariable int id,
            @PathVariable int otherId) throws ElementNotFoundException {
        return userService.getListOfCommonsFriends(id, otherId);
    }
}
