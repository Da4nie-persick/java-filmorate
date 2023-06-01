package ru.yandex.practicum.filmorate.validate;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class ValidateUser {
    public static void validate(User user) {
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
}
