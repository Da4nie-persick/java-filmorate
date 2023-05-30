package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.additions.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendDbStorage friendDbStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendDbStorage friendDbStorage) {
        this.userStorage = userStorage;
        this.friendDbStorage = friendDbStorage;
    }

    public void addToFriends(Integer id, Integer friendId) {
        User user = userStorage.getUserId(id);
        User friend = userStorage.getUserId(friendId);
        log.debug("Пользователь {}, добавил в друзья {}", user, friend);
        friendDbStorage.addToFriends(id, friendId);
    }

    public void deleteFriends(Integer id, Integer friendId) {
        User user = userStorage.getUserId(id);
        User friend = userStorage.getUserId(friendId);
        log.debug("Пользователь {}, удалил из друзей {}", user, friend);
        friendDbStorage.deleteFriends(id, friendId);
    }

    public List<User> mutualFriends(Integer id, Integer otherId) {
        User user = userStorage.getUserId(id);
        User other = userStorage.getUserId(otherId);
        if (user.getFriends() == null) {
            return new ArrayList<>();
        }
        Set<User> mutualFriendsHash = new HashSet<>(friendDbStorage.getFriends(id));
        mutualFriendsHash.retainAll(friendDbStorage.getFriends(otherId));
        return new ArrayList<User>(mutualFriendsHash);
    }

    public List<User> getMyFriends(Integer id) {
        User user = userStorage.getUserId(id);
        List<User> myFriendsList;
        myFriendsList = friendDbStorage.getFriends(id);
        return myFriendsList;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public Collection<User> allUsers() {
        return userStorage.allUsers();
    }

    public User getUserId(Integer id) {
        return userStorage.getUserId(id);
    }
}
