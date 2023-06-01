package ru.yandex.practicum.filmorate.validate;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class ValidateFilm {
    public static void validate(Film film) {
        if (film.getName().isBlank() || film.getName() == null) {
            log.warn("Передана пустая строка");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200 || film.getDescription() == null) {
            log.warn("Количество символов в строке привысило 200");
            throw new ValidationException("Максимальное количество символов не должно превышать 200");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)) || film.getReleaseDate() == null) {
            log.warn("Передана неверная дата фильма");
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.warn("Передана неверная продолжительность фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
