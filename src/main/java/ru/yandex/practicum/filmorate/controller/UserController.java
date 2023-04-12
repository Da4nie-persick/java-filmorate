package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    int id = 0;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validate(user);
        log.debug("Пользователь успешно создан");
        user = new User(generator(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            log.debug("Пользователь обновлен");
            users.put(user.getId(), user);
            return user;
        } else {
            log.debug("Такого пользователя нет");
            throw new ValidationException("Проверьте данные о пользователе");
        }
    }

    @GetMapping
    public Collection<User> allUsers() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    private void validate(User user) {
        if (user.getEmail() == null || (user.getEmail().indexOf("@") < 0)) {
            log.warn("Переданы не верные данные о почте");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Переданы не верные данные о логине");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now()) || user.getBirthday() == null) {
            log.warn("Переданы не верные данные о дате рождения");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    public int generator() {
        id++;
        return id;
    }
}
