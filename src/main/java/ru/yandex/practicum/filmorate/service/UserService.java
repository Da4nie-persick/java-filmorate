package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriends(Integer id, Integer friendId) {
        User user = getUserId(id);
        User friend = getUserId(friendId);
        log.debug("Пользователь {}, добавил в друзья {}", user, friend);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
    }

    public void deleteFriends(Integer id, Integer friendId) {
        User user = getUserId(id);
        User friend = getUserId(friendId);
        log.debug("Пользователь {}, удалил из друзей {}", user, friend);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
    }

    public List<User> mutualFriends(Integer id, Integer otherId) {
        User user = getUserId(id);
        User other = getUserId(otherId);
        if (user.getFriends() == null) {
            return new ArrayList<>();
        }
        Set<Integer> mutualFriendsHash = new HashSet<>(user.getFriends());
        mutualFriendsHash.retainAll(other.getFriends());
        List<User> mutualFriendsList = new ArrayList<>();
        for (Integer userId : mutualFriendsHash) {
            mutualFriendsList.add(getUserId(userId));
        }
        log.debug("Количество общих друзей {} и {} : {}", user, other, mutualFriendsList.size());
        return mutualFriendsList;
    }

    public List<User> getMyFriends(Integer id) {
        User user = getUserId(id);
        List<User> myFriendsList = new ArrayList<>();
        for (Integer userId : user.getFriends()) {
            myFriendsList.add(getUserId(userId));
        }
        log.debug("Количество друзей {} : {}", user, myFriendsList.size());
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
