package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.validate.ValidateUser;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

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
        ValidateUser.validate(user);
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
        if (users.get(id) == null) {
            throw new ObjectNotFoundException("id пользователя не найдено");
        }
        return users.get(id);
    }

    private int generator() {
        return ++id;
    }
}
