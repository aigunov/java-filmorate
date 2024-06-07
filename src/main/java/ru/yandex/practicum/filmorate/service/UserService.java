package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.time.future.FutureValidatorForReadableInstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ElementNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserFriendException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.users_logic.interfaces.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.users_logic.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userDB;
    private final FriendshipStorage friendshipDB;

    @Autowired
    public UserService(UserStorage userDB, FriendshipStorage friendshipDB) {
        this.userDB = userDB;
        this.friendshipDB = friendshipDB;
    }

    /**
     * the method responsible for the logic of adding another user to friends by the user
     *
     * @param userId   of the user of the subject
     * @param friendId of the user of the object
     * @return user who has just benn added to friends list
     * @throws UserFriendException
     * @throws ElementNotFoundException
     */
    public User addFriend(Integer userId, Integer friendId) {
        User user = userDB.getUser(userId).
                orElseThrow(() -> new NoSuchElementException("Пользователь с ID: " + userId + " не найден"));
        User friend = userDB.getUser(friendId).
                orElseThrow(() -> new NoSuchElementException("Пользователь c ID: " + friendId + " не найден"));
        friendshipDB.addFriend(userId, friendId);
        log.info("Пользователи :" + user.getName() + " и " + friend.getName() + " - подружились");
        return friend;
    }

    /**
     * the method responsible for the logic of removing another user to friends by the user
     *
     * @param userId   of the user of the subject
     * @param friendId of the user of the object
     * @return user who has just been removed from fiends list
     * @throws UserFriendException
     * @throws ElementNotFoundException
     */
    public User removeFriend(Integer userId, Integer friendId) {
        User user = userDB.getUser(userId).
                orElseThrow(() -> new NoSuchElementException("Пользователь с ID: " + userId + " не найден"));
        User friend = userDB.getUser(friendId).
                orElseThrow(() -> new NoSuchElementException("Пользователь c ID: " + friendId + " не найден"));
        friendshipDB.deleteFriend(userId, friendId);
        log.info("Пользователи:" + user.getName() + " и " + friend.getName() + " - прекратили дружбу!");
        return friend;
    }

    /**
     * @param id of the user of the subject
     * @return the list of user's friends
     * @throws ElementNotFoundException
     */
    public List<User> getListOfFriends(Integer id) {
        User user = userDB.getUser(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с ID: " + id + " не найден"));
        List<User> friends = friendshipDB.getUserFriend(id);
        log.info("Пользователь {} получил список всех своих друзей", user.getName());
        return friends;
    }

    /**
     * @param userId   of the user of the subject
     * @param friendId of the user of the object
     * @throws ElementNotFoundException
     */
    public User getFriendById(Integer userId, Integer friendId) {
        User user = userDB.getUser(userId).
                orElseThrow(() -> new NoSuchElementException("Пользователь с ID: " + userId + " не найден"));
        User friend = userDB.getUser(friendId).
                orElseThrow(() -> new NoSuchElementException("Пользователь c ID: " + friendId + " не найден"));
        if(!user.getFriends().contains(friendId)) {
            throw new UserFriendException(String.format("Пользователь {} не дружит с пользователем {}", user, friend));
        }
        log.info("Пользователь {} получил информацию о друге-пользователе {}", user.getName(), friend.getName());
        return friend;
    }

    /**
     * the method responsible for the logic of getCommonsFriends endpoint
     *
     * @param id      of the user of the subject
     * @param otherId of the user of the object
     * @throws ElementNotFoundException
     */
    public List<User> getListOfCommonsFriends(Integer id, Integer otherId) {
        User user1= userDB.getUser(id).
                orElseThrow(() -> new NoSuchElementException("Пользователь с ID: " + id + " не найден"));
        User user2 = userDB.getUser(otherId).
                orElseThrow(() -> new NoSuchElementException("Пользователь c ID: " + otherId + " не найден"));
        List<User> mutualFriends = friendshipDB.getMutualFriend(id, otherId);
        log.info("Запрос на получение списка общих друзей для пользователей обработан");
        return mutualFriends;
    }

    public User getUserById(Integer id) {
        User user = userDB.getUser(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с id = " + id + " не найден"));
        log.info("Обработан запрос по поиску пользователя. Найден пользователь: {}", user);
        return user;
    }

    public User addUser(User user) {
        Validator.userValidation(user);
        user.setId(userDB.addUser(user).getId());
        log.info("Пользователь {} добавлен в базу", user.getName());
        return user;
    }

    public User updateUser(User user) {
        User oldUser = userDB.getUser(user.getId())
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));
//        friendshipDB.deleteUser(user.getId());

        log.info("Обновлен пользователь: {}", user);
        return userDB.updateUser(user);
    }

    public User removeUser(Integer id) {
        User user = userDB.getUser(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с id = " + id + " не найден"));
        log.info("Пользователь {} удален", user.getName());
//        friendshipDB.deleteFriend(user.getId(), id);
        return userDB.deleteUser(id);
    }

    public List<User> getUsers() {
        List<User> allUsers = userDB.getAllUsers();
        log.info("Запрос на получение всех пользователей обработан");
        return allUsers;
    }

}
