package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film putLike(Integer filmId, Integer userId) {
        if (userStorage.getUserId(userId) == null) {
            log.warn("Передан неверный id пользователя");
            throw new ObjectNotFoundException("Пользователя с таким id нет");
        }
        Film film = getFilmId(filmId);
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        log.debug("Поставлен лайк на фильм: {}", film);
        film.getLikes().add(userId);
        update(film);
        return getFilmId(filmId);
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        if (userStorage.getUserId(userId) == null) {
            log.warn("Передан неверный id пользователя");
            throw new ObjectNotFoundException("Пользователя с таким id нет");
        }
        Film film = getFilmId(filmId);
        log.debug("Удален лайк на фильм: {}", film);
        film.getLikes().remove(userId);
        update(film);
        return getFilmId(filmId);
    }

    public List<Film> getPopularFilms(Integer count) {
        log.debug("Текущее количество фильмов в топе: {}", count);
        return allFilms().stream().sorted(Comparator.comparingInt((Film film) -> {
                            if (film.getLikes() == null) {
                                film.setLikes(new HashSet<>());
                            }
                            return film.getLikes().size();
                        }
                ).reversed())
                .limit(count).collect(Collectors.toList());
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Collection<Film> allFilms() {
        return filmStorage.allFilms();
    }

    public Film getFilmId(Integer id) {
        return filmStorage.getFilmId(id);
    }
}
