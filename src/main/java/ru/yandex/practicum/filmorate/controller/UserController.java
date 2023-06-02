package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        log.debug("Добавлен пользователь: {}", user);
        return userService.create(user);
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        log.debug("Обновлен пользователь: {}", user);
        return userService.update(user);
    }

    @GetMapping("/users")
    public Collection<User> allUsers() {
        log.debug("Выведен список всех пользователей");
        return userService.allUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserId(@PathVariable Integer id) {
        log.debug("Получен пользователь с id: {}", id);
        return userService.getUserId(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.debug("Пользователь c id {}, добавил в друзья пользователя с id {}", id, friendId);
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.debug("Пользователь c id {}, удалил из друзей пользователя с id {}", id, friendId);
        userService.deleteFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getMyFriends(@PathVariable Integer id) {
        log.debug("Выведен список друзей пользователя с id {}", id);
        return userService.getMyFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> mutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.debug("Выведен список общих друзьей пользователя с id {} и другого пользователя с id {}", id, otherId);
        return userService.mutualFriends(id, otherId);
    }
}