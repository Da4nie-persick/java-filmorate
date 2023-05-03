package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public Film create(Film film) {
        validate(film);
        log.debug("Фильм успешно добавлен: {}", film);
        film.setId(generator());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Фильм успешно обновлен: {}", film);
            return film;
        } else {
            log.debug("Такого фильма нет");
            throw new ObjectNotFoundException("Проверьте данные о фильме");
        }
    }

    @Override
    public Collection<Film> allFilms() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @Override
    public Film getFilmId(Integer id) {
        if (films.get(id) == null) {
            throw new ObjectNotFoundException("id пользователя не найдено");
        }
        return films.get(id);
    }

    public void validate(Film film) { 
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

    private int generator() {
        return ++id;
    }
}
