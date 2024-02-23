package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    private final UserStorage userStorage;


    @Autowired
    public UserController(UserService userService, InMemoryUserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    /**
     * @return the user
     */
    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        return userStorage.getUserFromStorage(id);
    }

    /**
     * @param user body of POST request to put into users map
     * @return dded user
     * @throws ValidationException if the user's values are invalid
     */
    @PostMapping
    public User addUser(@RequestBody User user) throws ValidationException {
        return userStorage.addUserToStorage(user);
    }

    /**
     * @param user body PUT request to update in users map
     * @return updated user
     * @throws ValidationException if the user's values are invalid
     */

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        return userStorage.updateUserInStorage(user);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable int id) {
        return userStorage.deleteUserFromStorage(id);
    }

    @PutMapping("{id}/friends/{friendId}")
    public User addFriend(
            @PathVariable int id,
            @PathVariable int friendId) throws UserFriendException {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friends}")
    public User deleteFriend(
            @PathVariable int id,
            @PathVariable int friendId) throws UserFriendException {
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        return userService.getListOfFriends(id);
    }

    @GetMapping("{id}/friends{friendId}")
    public User getFriend(
            @PathVariable int id,
            @PathVariable int friendId){
        return userService.getFriendById(id, friendId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(
            @PathVariable int id,
            @PathVariable int otherId) {
        return userService.getListOfCommonsFriends(id, otherId);
    }
}
