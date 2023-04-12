package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    int id = 0;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validate(film);
        log.debug("Фильм успешно добавлен");
        film = new Film(generator(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Фильм успешно обновлен");
            return film;
        } else {
            log.debug("Такого фильма нет");
            throw new ValidationException("Проверьте данные о фильме");
        }
    }

    @GetMapping
    public Collection<Film> allFilms() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    private void validate(Film film) {
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

    public int generator() {
        id++;
        return id;
    }
}
