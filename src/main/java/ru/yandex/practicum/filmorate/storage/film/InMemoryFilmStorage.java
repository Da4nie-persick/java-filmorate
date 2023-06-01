package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.validate.ValidateFilm;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public Film create(Film film) {
        ValidateFilm.validate(film);
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
    public Optional<Film> getFilmId(Integer id) {
        if (films.get(id) == null) {
            throw new ObjectNotFoundException("id пользователя не найдено");
        }
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return null;
    }

    private int generator() {
        return ++id;
    }
}
