package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public User create(User user) {
        validate(user);
        log.debug("Пользователь успешно создан: {}", user);
        user.setId(generator());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            log.debug("Пользователь обновлен: {}", user);
            users.put(user.getId(), user);
            return user;
        } else {
            log.debug("Такого пользователя нет");
            throw new ObjectNotFoundException("Проверьте данные о пользователе");
        }
    }

    @Override
    public Collection<User> allUsers() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @Override
    public User getUserId(Integer id) {
        return users.entrySet().stream()
                .filter(u -> id.equals(u.getKey())).map(Map.Entry::getValue).findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("id пользователя не найдено"));
    }

    public void validate(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
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
        return ++id;
    }
}
