package ru.yandex.practicum.filmorate.storage.users.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {

    void addFriend(int id1, int id2);

    void deleteFriend(int id1, int id2);

    List<User> getUserFriend(int id);

    List<User> getMutualFriend(int id1, int id2);
}
