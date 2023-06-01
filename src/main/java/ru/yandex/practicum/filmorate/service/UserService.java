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
        Optional<User> user = userStorage.getUserId(id);
        Optional<User> friend = userStorage.getUserId(friendId);
        log.debug("Пользователь {}, добавил в друзья {}", user, friend);
        friendDbStorage.addToFriends(id, friendId);
    }

    public void deleteFriends(Integer id, Integer friendId) {
        Optional<User> user = userStorage.getUserId(id);
        Optional<User> friend = userStorage.getUserId(friendId);
        log.debug("Пользователь {}, удалил из друзей {}", user, friend);
        friendDbStorage.deleteFriends(id, friendId);
    }

    public List<User> mutualFriends(Integer id, Integer otherId) {
        Optional<User> user = userStorage.getUserId(id);
        userStorage.getUserId(otherId);
        if (user.get().getFriends() == null) {
            return new ArrayList<>();
        }
        List<User> mutualFriends = friendDbStorage.mutualFriends(id, otherId);
        return mutualFriends;
    }

    public List<User> getMyFriends(Integer id) {
        userStorage.getUserId(id);
        return new ArrayList<>(friendDbStorage.getFriends(id));
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

    public Optional<User> getUserId(Integer id) {
        return userStorage.getUserId(id);
    }
}
