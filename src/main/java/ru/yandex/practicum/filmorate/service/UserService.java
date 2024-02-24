package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ElementNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserFriendException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired //если одна переменная для внедрения зависимости не легче ли просто к переменной поставить аннотацию?
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(int userId, int friendId) throws UserFriendException, ElementNotFoundException {
        User user = userStorage.getUserFromStorage(userId);
        User friend = userStorage.getUserFromStorage(friendId);
        userNotExistThrows(user);
        userNotExistThrows(friend);
        if (user.getFriends().contains(friendId)) {
            log.error(String.format("client cannot add the %s user to the friends list again", friend.getName()));
            throw new UserFriendException(String.format("User with %d id is already your friend", friendId));
        }
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.error(String.format("client has just added the %s user as a friend", friend.getName()));
        return friend;
    }

    public User removeFriend(Integer userId, Integer friendId) throws UserFriendException, ElementNotFoundException {
        User user = userStorage.getUserFromStorage(userId);
        User friend = userStorage.getUserFromStorage(friendId);
        userNotExistThrows(user);
        if (!user.getFriends().contains(friendId)) {
            log.error(String.format("client cannot remove the %s user who is not his friend from the friends list", friend.getName()));
            throw new UserFriendException(String.format("User with %d id is not your friend", friendId));
        }
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.info(String.format("client has just removed the %s user", friend.getName()));
        return friend;
    }

    public List<User> getListOfFriends(int id) throws ElementNotFoundException {
        User user = userStorage.getUserFromStorage(id);
        userNotExistThrows(user);
        log.info("client has just got the list of his friends");
        return user.getFriends().stream().map(userStorage::getUserFromStorage).collect(Collectors.toList());
    }

    public User getFriendById(int id, int friendId) throws ElementNotFoundException {
        User user = userStorage.getUserFromStorage(id);
        userNotExistThrows(user);
        log.info("client has just got a friend");
        return userStorage.getUserFromStorage(user.getFriends().get(friendId));
    }

    public List<User> getListOfCommonsFriends(int id, int otherId) throws ElementNotFoundException {
        User user = userStorage.getUserFromStorage(id);
        User friend = userStorage.getUserFromStorage(otherId);
        userNotExistThrows(user);
        userNotExistThrows(friend);
        if (user.getFriends() != null && friend.getFriends() != null) {
            log.info(String.format("client has just got the list of common friends with the %s user", friend.getName()));
            return userStorage.getUsers().stream().
                    filter(user1 -> (user1.getFriends().contains(user.getId()) && user1.getFriends().contains(friend.getId())) &&
                            (!user1.equals(user.getId()) && !user1.equals(friend.getId()))).collect(Collectors.toList());
        }else{
            return new ArrayList<User>();
        }

    }

    public User getUserById(int id) throws ElementNotFoundException {
        userNotExistThrows(userStorage.getUserFromStorage(id));
        return userStorage.getUserFromStorage(id);

    }

    public User addUser(User user) throws ValidationException {
        return userStorage.addUserToStorage(user);
    }

    public User updateUser(User user) throws ValidationException {
        return userStorage.updateUserInStorage(user);
    }

    public User removeUser(int id) throws ElementNotFoundException {
        userNotExistThrows(userStorage.getUserFromStorage(id));
        return userStorage.deleteUserFromStorage(id);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public void userNotExistThrows(User user) throws ElementNotFoundException {
        if (user == null) {
            log.error("this user is not exist to return, probably the path variables incorrect");

            throw new ElementNotFoundException("the user is not exist");
        }
    }

}
