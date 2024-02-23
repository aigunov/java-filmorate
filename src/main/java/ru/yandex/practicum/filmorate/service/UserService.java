package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserFriendException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired //если одна переменная для внедрения зависимости не легче ли просто к переменной поставить аннотацию?
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(int userId, int friendId) throws UserFriendException {
        User user = userStorage.getUserFromStorage(userId);
        User friend = userStorage.getUserFromStorage(friendId);
        userNotExistThrows(user);
        if(user.getFriends().contains(friend)){
            throw new UserFriendException(String.format("User with %d id is already your friend", friendId));
        }
        user.getFriends().add(friend);
        friend.getFriends().add(user);
        return friend; //Использование метода storage а не обращение к list напрямую
    }

    public User removeFriend(int userId, int friendId) throws UserFriendException {
        User user = userStorage.getUserFromStorage(userId);
        User friend = userStorage.getUserFromStorage(friendId);
        userNotExistThrows(user);
        if(!user.getFriends().contains(friend)){
            throw new UserFriendException(String.format("User with %d id is not your friend", friendId));
        }
        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
        return friend;
    }

    public List<User> getListOfFriends(int id){
        User user = userStorage.getUserFromStorage(id);
        userNotExistThrows(user);
        return user.getFriends();
    }

    public User getFriendById(int id, int friendId){
        User user = userStorage.getUserFromStorage(id);
        userNotExistThrows(user);
        return user.getFriends().get(friendId);
    }

    public List<User> getListOfCommonsFriends(int id, int otherId){
        User user = userStorage.getUserFromStorage(id);
        User friend = userStorage.getUserFromStorage(otherId);
        userNotExistThrows(user);
        userNotExistThrows(friend);
        return userStorage.getUsers().stream().
                            filter(user1 -> (user1.getFriends().contains(user) && user1.getFriends().contains(friend))&&
                                    (!user1.equals(user) && !user1.equals(friend))).collect(Collectors.toList());

    }

    public void userNotExistThrows(User user){
        if (user == null){
            throw new NullPointerException("This user is not exist");
        }
    }
}
