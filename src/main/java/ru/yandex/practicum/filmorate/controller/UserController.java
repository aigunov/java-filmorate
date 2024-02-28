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

    /**
     * @return the list of all users
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        return userService.getUsers();
    }

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

    /**
     * @param id of the user's data to delete
     * @return the deleted user object
     * @throws ElementNotFoundException
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User deleteUser(@PathVariable int id) throws ElementNotFoundException {
        return userService.removeUser(id);
    }

    /**
     * the endpoint method for adding a user as a friend
     * @param id of the user of the subject
     * @param friendId of the user of the object
     * @return object of friendId's data
     * @throws UserFriendException
     * @throws ElementNotFoundException
     */
    @PutMapping("{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User addFriend(
            @PathVariable int id,
            @PathVariable int friendId) throws UserFriendException, ElementNotFoundException {
        return userService.addFriend(id, friendId);
    }

    /**
     * the endpoint method allows one user to remove their friends from the list
     * @param id of the user of the subject
     * @param friendId of the user of the object
     * @return object of friendId's data
     * @throws UserFriendException
     * @throws ElementNotFoundException
     */
    @DeleteMapping("{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User deleteFriend(
            @PathVariable int id,
            @PathVariable int friendId) throws UserFriendException, ElementNotFoundException {
        return userService.removeFriend(id, friendId);
    }

    /**
     * the endpoint method returns a list of the user's friends
     * @param id of the user of the subject
     * @throws ElementNotFoundException
     */
    @GetMapping("{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getFriends(@PathVariable int id) throws ElementNotFoundException {
        return userService.getListOfFriends(id);
    }

    /**
     * the endpoint method return the user's friend by his id
     * @param id of the user who get information
     * @param friendId of the friend-user
     * @return data object of friend
     * @throws ElementNotFoundException
     */
    @GetMapping("{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User getFriend(
            @PathVariable int id,
            @PathVariable int friendId) throws ElementNotFoundException {
        return userService.getFriendById(id, friendId);
    }

    /**
     * the endpoint method return the list of mutual friends for two different users
     * @param id of the user of the subject
     * @param otherId of the user of the object
     * @throws ElementNotFoundException
     */
    @GetMapping("{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getCommonFriends(
            @PathVariable int id,
            @PathVariable int otherId) throws ElementNotFoundException {
        return userService.getListOfCommonsFriends(id, otherId);
    }
}
