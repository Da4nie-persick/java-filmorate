package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final InMemoryUserStorage memoryUserStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage memoryUserStorage, UserService userService) {
        this.memoryUserStorage = memoryUserStorage;
        this.userService = userService;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return memoryUserStorage.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return memoryUserStorage.update(user);
    }

    @GetMapping
    public Collection<User> allUsers() {
        return memoryUserStorage.allUsers();
    }

    @GetMapping("/{id}")
    public User getUserId(@PathVariable("id") Integer id) {
        return userService.getUserId(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriends(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        return userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriends(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        return userService.deleteFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getMyFriends(@PathVariable("id") Integer id) {
        return userService.getMyFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> mutualFriends(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId) {
        return userService.mutualFriends(id, otherId);
    }
}
