package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.additions.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeDbStorage likeDbStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage,
                       LikeDbStorage likeDbStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeDbStorage = likeDbStorage;
    }

    public void putLike(Integer filmId, Integer userId) {
        userStorage.getUserId(userId);
        getFilmId(filmId);
        likeDbStorage.putLike(filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        userStorage.getUserId(userId);
        getFilmId(filmId);
        likeDbStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(count);
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
